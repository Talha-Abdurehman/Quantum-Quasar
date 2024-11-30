package org.multiplayer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import netscape.javascript.JSObject;
import org.json.JSONObject;

public class GameClient {
    private Socket socket;
    String path = "http://localhost:3000";

    public GameClient(String path) {
        this.path = path;
        Connection(path);
    }

    public void Connection(String path) {
        try {
            socket = IO.socket(path);

            // Log all events to check for issues
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Connected");
                }
            });

            socket.on("newPlayer", args -> {
                JSONObject data = (JSONObject) args[0];
                System.out.println("New Player Joined: " + data);
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Error: " + objects[0]);
                }
            });

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println("Disconnected");
                }
            });

            socket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame(JSONObject initialData) {
        socket.emit("playerJoin", initialData);

    }

    public void sendPlayerData(JSONObject playerData) {
        socket.emit("playerUpdate", playerData);
    }

    public void disconnectPlayer() {
        socket.disconnect();
    }

}
