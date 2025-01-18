package tictactoeserver;

import java.io.IOException;
import tictactoeserver.ConnectedPlayer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
  @author Abdelrahman_Elshreif
 */
public class Server {

    public ServerSocket server;
    public static final int PORT = 9800;
    public static final String STOP_STRING = "##";
    private int playerIdx = 0;
    private boolean isRunning = false;

    public Server() {
    }

    public void start() {
        try {
            this.server = new ServerSocket(PORT);
            System.out.println("Waiting For Players....");
            while (isRunning) {
                try {
                    initPlayerConnection();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Error accepting player connection", ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Failed to start server", ex);
        } finally {
            close();
        }
    }

    // Turning Off Server
    public void close() {
        isRunning = false;

        try {
            if (this.server != null && !this.server.isClosed()) {
                this.server.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initPlayerConnection() throws IOException {
        Socket playerSocket = server.accept();

        if (playerSocket.isConnected()) {
            new Thread(() -> {
                playerIdx++;
                ConnectedPlayer player = new ConnectedPlayer(playerSocket, playerIdx);

                player.readMessages();
                player.close();

            }).start();
        }
    }
}
