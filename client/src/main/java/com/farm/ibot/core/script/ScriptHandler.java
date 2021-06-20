// Decompiled with: CFR 0.150
package com.farm.ibot.core.script;

import com.farm.ibot.api.listener.EventHandler;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.SafeArrayList;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.impl.debuggers.MouseDebug;
import com.farm.ibot.core.script.impl.random.*;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScriptHandler {
    private final Bot bot;
    public LoginRandom loginRandom = new LoginRandom();
    public AntiKick antiKick = new AntiKick();
    public HumanDataSender humanDataSender = new HumanDataSender();
    public DialogueSkipper dialogues = new DialogueSkipper();
    public BreakRandom breakHandler = new BreakRandom();
    public WebNotFoundRandom webNotFoundRandom = new WebNotFoundRandom();
    public FullScreenDisableRandom fullScreenRandom = new FullScreenDisableRandom();
    public ScriptLoader scriptLoader = new ScriptLoader(this);
    private RandomEvent[] randomEvents = new RandomEvent[]{this.breakHandler, this.loginRandom, this.antiKick, this.dialogues, this.webNotFoundRandom, this.fullScreenRandom, this.humanDataSender};
    private BotScript currentlyExecuting;
    private BotScript currentScript;
    private ScheduledExecutorService scriptExecutor;
    private ScheduledExecutorService randomsExecutor;
    private ArrayList<BackgroundScript> backgroundScripts = new ArrayList();
    private SafeArrayList<BotScript> scriptQueue = new SafeArrayList();
    private Thread eventListenersThread = null;

    public ScriptHandler(Bot bot) {
        this.bot = bot;
    }

    public void restart() {
        try {
            this.stop();
            BotScript script = this.scriptLoader.loadLast();
            this.start(script);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {

        if (this.currentScript != null) {
            this.currentScript.removeMouseListeners();
            this.currentScript.onStop();
            this.currentScript = null;
        }
        if (this.scriptExecutor != null) {
            this.stopExecutor();
        }
        this.currentlyExecuting = null;
    }

    public void start(BotScript script) {
        this.addDebug(MouseDebug.class);
        Bot.get().clearCachedObjects();
        Bot.runLater(() -> {
            this.stop();
            Debug.log("OnStart: " + script.getName());
            if (this.bot.getSession().autostartScript != null) {
                // empty if block
            }
            this.bot.proxyManager.setProxy();
            Bot.currentThreadBot = this.bot;
            this.currentScript = script;
            this.currentlyExecuting = script;
            for (RandomEvent event : this.randomEvents) {
                event.active = true;
            }
            script.onStart();
            script.onStartHandled = true;
            this.startRandomEvents();
            this.startQuietly();
            this.startEventListeners();
        });
    }

    private void startRandomEvents() {
        if (this.randomsExecutor != null) {
            return;
        }
        this.randomsExecutor = Executors.newScheduledThreadPool(1, r -> new Thread(this.getBot().getThreadGroup(), r, "RandomExecutorThread"));
        this.randomsExecutor.scheduleAtFixedRate(() -> {
            if (this.currentlyExecuting != null) {
                try {
                    this.detectRandoms();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 1000L, TimeUnit.MILLISECONDS);
    }

    private void startQuietly() {
        this.scriptExecutor = Executors.newScheduledThreadPool(1, r -> new Thread(this.getBot().getThreadGroup(), r, "ScriptExecutorThread"));
        this.scriptExecutor.scheduleAtFixedRate(() -> {

            if (this.currentlyExecuting != null) {
                try {
                    this.listenLoginEvent();
                    this.tryLoop(this.currentlyExecuting);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 10L, TimeUnit.MILLISECONDS);
    }

    private void stopExecutor() {
        this.scriptExecutor.shutdownNow();
        this.scriptExecutor = null;
        this.currentlyExecuting = null;
    }

    private void listenLoginEvent() {
        if (!this.currentlyExecuting.firstLoginHandled && !LoginRandom.isLoggedOut()) {
            this.currentlyExecuting.onStartWhenLoggedIn();
            this.currentlyExecuting.firstLoginHandled = true;
            Bot.get().clearCachedObjects();
        }
    }

    public void detectRandoms() {
        try {
            for (RandomEvent event : this.randomEvents) {
                if (System.currentTimeMillis() <= event.nextCheck && this.currentlyExecuting != event) continue;
                event.nextCheck = System.currentTimeMillis() + event.checkInterval();
                if (!event.active || !event.isEnabled()) continue;
                if (event.isBackground()) {
                    event.onLoop();
                    continue;
                }
                if (this.currentlyExecuting != event) {
                    event.onStart();
                }
                this.currentlyExecuting = event;
                return;
            }
            this.currentlyExecuting = this.currentScript;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startEventListeners() {
        if (this.eventListenersThread != null) {
            return;
        }
        this.eventListenersThread = new Thread(this.bot.getThreadGroup(), () -> {
            while (true) {
                if (this.currentlyExecuting != null) {
                    try {
                        ArrayList<EventHandler> eventHandlers = new ArrayList<EventHandler>(this.currentlyExecuting.getEventHandlers());
                        if (this.dialogues.active) {
                            eventHandlers.add(this.dialogues.messageListenerHandler);
                        }
                        for (EventHandler eventHandler : eventHandlers) {
                            if (eventHandler.listeningThread != null && eventHandler.listeningThread.isAlive())
                                continue;
                            eventHandler.listeningThread = new Thread((ThreadGroup) this.bot.getThreadGroup(), () -> {
                                try {
                                    Time.sleep(eventHandler.listen());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                eventHandler.listeningThread = null;
                            });
                            eventHandler.listeningThread.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Time.sleep(1000);
            }
        }, "Event listeners thread");
        this.eventListenersThread.start();
    }

    private void tryLoop(BotScript script) {
        try {
            Time.sleep(script.onLoop());
        } catch (Exception e) {
            e.printStackTrace();
            Time.sleep(1000);
        }
    }

    public BotScript getCurrentlyExecuting() {
        return this.currentlyExecuting;
    }

    public BotScript getScript() {
        return this.currentScript;
    }

    public void addDebug(Class debugScript) {
        try {
            BackgroundScript script = (BackgroundScript) debugScript.newInstance();
            if (this.backgroundScripts.stream().anyMatch(s -> s.getClass().equals(debugScript))) {
                return;
            }
            this.backgroundScripts.add(script);
            new Thread((ThreadGroup) this.bot.getThreadGroup(), () -> {
                while (this.backgroundScripts.contains(script)) {
                    this.tryLoop(script);
                }
            }).start();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void removeDebug(Class debugScript) {
        new ArrayList<BackgroundScript>(this.backgroundScripts).stream().filter(script -> script.getClass() == debugScript).forEach(this.backgroundScripts::remove);
    }

    public ArrayList<BackgroundScript> getBackgroundScripts() {
        return this.backgroundScripts;
    }

    public void startNextQueuedScript(BotScript callerScript) {
        Debug.log("Script queue: " + this.scriptQueue.toString());
        if (!callerScript.getName().equalsIgnoreCase(this.scriptQueue.get(0).getName())) {
            Debug.log(callerScript.getName() + " attempted to startNextQueuedScript.");
            if (this.currentScript != this.scriptQueue.get(0)) {
                this.start(this.scriptQueue.get(0));
            }
            return;
        }

        Debug.log("Next script:" + this.scriptQueue.get(1));
        this.scriptQueue.remove(0);
        BotScript next = this.scriptQueue.get(0);
        if (next != null) {

            this.start(next);
        } else {
            this.stop();
        }
    }

    public ArrayList<BotScript> getScriptQueue() {
        return this.scriptQueue;
    }

    public void setScriptQueue(SafeArrayList<BotScript> scriptQueue) {
        this.scriptQueue = scriptQueue;
    }

    public BotScript getMainScript() {
        return this.getScriptQueue().get(this.getScriptQueue().size() - 1);
    }

    public ScriptLoader getScriptLoader() {
        return this.scriptLoader;
    }

    public Bot getBot() {
        return this.bot;
    }

    public void setPaused(boolean paused) {
        this.currentlyExecuting = paused ? null : this.currentScript;
    }
}
