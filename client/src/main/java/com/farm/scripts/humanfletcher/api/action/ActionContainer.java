package com.farm.scripts.humanfletcher.api.action;

import com.farm.scripts.humanfletcher.api.action.wrapper.Action;

import java.util.ArrayList;
import java.util.Iterator;

public class ActionContainer {
    public boolean isIdling;
    public boolean isReaction;
    public int id;
    private ArrayList<Action> actions;

    public ActionContainer(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public ActionContainer() {
        this(new ArrayList());
    }

    public ArrayList<Action> getActions() {
        return this.actions;
    }

    public ActionContainer splitBefore(Class action) {
        ArrayList<Action> actions = new ArrayList();
        Iterator var3 = this.getActions().iterator();

        Action containerAction;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            containerAction = (Action) var3.next();
            actions.add(containerAction);
        } while (!containerAction.getClass().equals(action));

        return new ActionContainer(actions);
    }

    public ActionContainer splitAfter(Class action) {
        ArrayList<Action> actions = new ArrayList(this.getActions());
        Iterator var3 = this.getActions().iterator();

        Action containerAction;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            containerAction = (Action) var3.next();
            actions.remove(containerAction);
        } while (!containerAction.getClass().equals(action));

        return new ActionContainer(actions);
    }

    public ActionContainer currentOrSplitAfter(Class actionClass) {
        return this.contains(actionClass) ? this.splitAfter(actionClass) : this;
    }

    public Action get(Class element) {
        return (Action) this.actions.stream().filter((a) -> {
            return a.getClass().equals(element);
        }).findFirst().orElse(null);
    }

    public Action getMainAction() {
        return (Action) this.getActions().get(this.getActions().size() - 1);
    }

    public boolean contains(Class element) {
        return this.actions.stream().anyMatch((a) -> {
            return a.getClass().equals(element);
        });
    }
}
