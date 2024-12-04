package org.multiplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkManager implements Runnable {
    private volatile boolean running = true;
    private final ConcurrentLinkedQueue<JSONObject> sendQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<JSONObject> receiveQueue = new ConcurrentLinkedQueue<>();
    private final GameClient gameClient;
    private NetworkListener networkListener;
    private long lastNetworkUpdateTime = 0;
    private static final long NETWORK_UPDATE_INTERVAL = 50; // 50ms between updates (20 Hz)

    public NetworkManager(String serverPath) {
        this.gameClient = new GameClient(serverPath);
        setupClientListeners();
    }

    private void setupClientListeners() {
        gameClient.setOnPlayerUpdateListener(playerData -> {
            try {
                // Add server timestamp if not already present
                if (!playerData.has("serverTimestamp")) {
                    playerData.put("serverTimestamp", System.currentTimeMillis());
                }

                // Log network performance metrics
                long currentTime = System.currentTimeMillis();
                long receivedTimestamp = playerData.getLong("serverTimestamp");
                long networkLatency = currentTime - receivedTimestamp;

                System.out.println("Network Metrics:");
                System.out.println("- Player ID: " + playerData.optString("id", "Unknown"));
                System.out.println("- Network Latency: " + networkLatency + "ms");
                System.out.println("- Server Timestamp: " + receivedTimestamp);
                System.out.println("- Received Timestamp: " + currentTime);

                // Invoke network listener
                if (networkListener != null) {
                    networkListener.onPlayerUpdate(playerData);
                }

                // Add to receive queue
                receiveQueue.add(playerData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Method to set a listener for network events
    public void setNetworkListener(NetworkListener listener) {
        this.networkListener = listener;
    }

    // Send data to the server with controlled update frequency
    public void sendPlayerData(JSONObject data) {
        long currentTime = System.currentTimeMillis();

        // Enforce update frequency
        if (currentTime - lastNetworkUpdateTime >= NETWORK_UPDATE_INTERVAL) {
            try {
                // Add client-side timestamp
                data.put("clientTimestamp", currentTime);

                sendQueue.add(data);
                lastNetworkUpdateTime = currentTime;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Retrieve received data
    public JSONObject pollReceivedData() {
        return receiveQueue.poll();
    }

    // Join the game with initial player data
    public void joinGame(JSONObject initialData) {
        try {
            // Add timestamp to initial join data
            initialData.put("joinTimestamp", System.currentTimeMillis());
            gameClient.joinGame(initialData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Interface for network event callbacks
    public interface NetworkListener {
        void onPlayerUpdate(JSONObject playerData) throws JSONException;
    }
}