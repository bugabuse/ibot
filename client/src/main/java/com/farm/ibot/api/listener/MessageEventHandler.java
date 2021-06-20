package com.farm.ibot.api.listener;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.Widgets;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageEventHandler extends EventHandler {
    private MessageListener listener;
    private ArrayList<String> lastMessages = new ArrayList();

    public MessageEventHandler(MessageListener listener) {
        this.listener = listener;
    }

    public static ArrayList<String> getMessages() {
        ArrayList<String> temp = new ArrayList();
        Widget widget = Widgets.get(162, 59);
        if (widget != null) {
            Widget[] children = widget.getChildren();

            for (int i = 0; i < 200 && i < children.length; i += 2) {
                String text1 = children[i].getText();
                String text2 = children[i + 1].getText();
                String message = (text1 != null && text1.length() > 1 ? text1 : "") + (text2 != null && text2.length() > 1 ? text2 : "");
                if (message.length() > 1) {
                    temp.add(message);
                }
            }
        }

        return temp;
    }

    public int listen() {
        ArrayList<String> messages = getMessages();
        ArrayList<String> messages1 = new ArrayList(messages);
        Iterator var3 = this.lastMessages.iterator();

        String m;
        while (var3.hasNext()) {
            m = (String) var3.next();
            messages.remove(m);
        }

        if (this.lastMessages.size() > 0) {
            var3 = messages.iterator();

            while (var3.hasNext()) {
                m = (String) var3.next();
                this.listener.onMessage(m);
            }
        }

        this.lastMessages = messages1;
        return 300;
    }
}
