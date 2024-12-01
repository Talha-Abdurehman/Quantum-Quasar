package org.multiplayer;

import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkManager implements Runnable {
    private volatile boolean running = true;
    private final ConcurrentLinkedQueue<JSONObject> sendQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<JSONObject> receiveQueue = new ConcurrentLinkedQueue<>();
    private final GameClient gameClient;
    private NetworkListener networkListener;

    public NetworkManager(String serverPath) {
        this.gameClient = new GameClient(serverPath);
        setupClientListeners();
    }

    private void setupClientListeners() {
        // Modify GameClient to add a method for receiving player updates
        gameClient.setOnPlayerUpdateListener(playerData -> {
            if (networkListener != null) {
                networkListener.onPlayerUpdate(playerData);
            }
            receiveQueue.add(playerData);
        });
    }

    // Method to set a listener for network events
    public void setNetworkListener(NetworkListener listener) {
        this.networkListener = listener;
    }

    // Send data to the server
    public void sendPlayerData(JSONObject data) {
        sendQueue.add(data);
    }

    // Retrieve received data
    public JSONObject pollReceivedData() {
        return receiveQueue.poll();
    }

    // Join the game with initial player data
    public void joinGame(JSONObject initialData) {
        gameClient.joinGame(initialData);
    }

    // Stop the network manager
    public void stop() {
        running = false;
        gameClient.disconnectPlayer();
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Process send queue
                while (!sendQueue.isEmpty()) {
                    JSONObject data = sendQueue.poll();
                    if (data != null) {
                        gameClient.sendPlayerData(data);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Interface for network event callbacks
    public interface NetworkListener {
        void onPlayerUpdate(JSONObject playerData);
    }
}