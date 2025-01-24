package tictactoeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.GamePlayAction;


/*
  @author Abdelrahman_Elshreif
 */
public class Server {

    public ServerSocket server;
    public static final int PORT = 9800;
    private boolean isRunning = false;

    private ArrayList<ClientHandler> activePlayers;
    public static Vector<ClientHandler> clientVector = new Vector<>();
    public static Vector<Thread> clientThreadVector = new Vector<>();
    public static int playerThreadNumber;

    public Server() {
    }

    public void start() {
        isRunning = true;
        try {
            this.server = new ServerSocket(PORT);
            System.out.println("Waiting For Players....");
            while (isRunning) {
                try {
                    Socket playerSocket = server.accept();
                    initPlayerConnection(playerSocket);
                    TicTacToeServerController.activePorts.add(String.valueOf(playerSocket.getPort()));
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

    //turn off the server 
    public void close() {
        isRunning = false;
        TicTacToeServerController.activePorts.clear();
        try {
            if (this.server != null && !this.server.isClosed()) {
                // Close the server socket to unblock the accept() call
                this.server.close();
            }
            synchronized (clientVector) {
                System.out.println("Closing client handlers...");
                for (ClientHandler client : clientVector) {
                    if (client != null) {
                        client.close();
                    }
                }
                clientVector.clear();
            }
            synchronized (clientThreadVector) {
                System.out.println("Interrupting client threads...");
                for (Thread thread : clientThreadVector) {
                    if (thread != null) {
                        thread.interrupt();
                    }
                }
                clientThreadVector.clear();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initPlayerConnection(Socket playerSocket) throws IOException {
        if (playerSocket.isConnected()) {
            ClientHandler clientHandler = new ClientHandler(playerSocket);
            Thread clientThread = new Thread(clientHandler);
            clientThreadVector.add(clientThread);
            clientVector.add(clientHandler);
            clientThread.start();
        }
    }

    public static ArrayList<String> getOnlinePlayers() {
        ArrayList<String> usernames = new ArrayList<>();
        Server.clientVector.forEach((handler) -> {
            usernames.add(handler.username);
        });
        return usernames;
    }
}
