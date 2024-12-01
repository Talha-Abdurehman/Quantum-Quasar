package org.multiplayer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

public class GameClient {
    private Socket socket;
    private String path;
    private NetworkManager.NetworkListener playerUpdateListener;

    public GameClient(String path) {
        this.path = path;
        connect(path);
    }

    private void connect(String path) {
        try {
            socket = IO.socket(path);

            socket.on(Socket.EVENT_CONNECT, args ->
                    System.out.println("Connected to server"));

            socket.on("playerUpdate", args -> {
                JSONObject data = (JSONObject) args[0];
                if (playerUpdateListener != null) {
                    playerUpdateListener.onPlayerUpdate(data);
                }
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, args ->
                    System.out.println("Connection Error: " + args[0]));

            socket.on(Socket.EVENT_DISCONNECT, args ->
                    System.out.println("Disconnected from server"));

            socket.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set player update listener
    public void setOnPlayerUpdateListener(NetworkManager.NetworkListener listener) {
        this.playerUpdateListener = listener;
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