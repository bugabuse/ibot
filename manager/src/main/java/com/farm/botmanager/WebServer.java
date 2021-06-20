package com.farm.botmanager;

import com.google.gson.Gson;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WebServer implements Container {
    public static final int MAX_THREADS = 20;
    private final Executor executor = Executors.newFixedThreadPool(20);

    public static void start() throws IOException {
        WebServer container = new WebServer();
        ContainerServer server = new ContainerServer(container);
        SocketConnection connection = new SocketConnection(server);
        InetSocketAddress address = new InetSocketAddress(6666);
        connection.connect(address);
        System.out.println("Server active, listening on port 6666.");
    }

    @Override
    public void handle(Request request, Response response) {
        response.setValue("Access-Control-Allow-Origin", request.getValue("Origin"));
        response.setValue("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.setValue("Access-Control-Allow-Credentials", "true");
        response.setValue("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        String command = request.getParameter("command");
        String action = request.getParameter("get");
        String pid = request.getParameter("pid");
        try {
            if (action != null) {
                if (action.equals("data")) {
                    String str = new Gson().toJson(Main.getData());
                    response.getPrintStream().println(str);
                }
                if (action.equals("update")) {
                    BotData.lastUpdate = System.currentTimeMillis();
                    BotData.botsOnline = command;
                    System.out.format("[incoming command] %s%n",request.toString());
                    if (pid != null && pid.length() > 0) {
                        Main.pid = pid;
                    }
                }
            } else if (command != null) {
                response.getPrintStream().println("THX");
                Main.onCommand(command);
            } else {
                response.getPrintStream().println("No data.");
            }

            response.getPrintStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

