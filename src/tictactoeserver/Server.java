package tictactoeserver;
import java.io.IOException;
import tictactoeserver.ConnectedPlayer;
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
    public static final String STOP_STRING = "##";
    private int playerIdx = 0;
    private boolean isRunning = false;

    private ArrayList<ConnectedPlayer> activePlayers;

    
 public static Vector<ClientHandler> clientVector = new Vector<>();

    public Server() {
    }

    public void start() {
        isRunning = true;
        try {
            this.server = new ServerSocket(PORT);
            System.out.println("Waiting For Players....");
//            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            while (isRunning) {
                try {
                    Socket playerSocket = server.accept();
                    initPlayerConnection(playerSocket);
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
//
//    public void shutdown() {
//        System.out.println("Shutting down server and closing all connections...");
//        isRunning = false;
//        for (ConnectedPlayer player : activePlayers) {
//            try {
//                player.getPlayerSocket().close();
//            } catch (IOException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Error closing player socket", ex);
//            }
//        }
//
//        // Clear the list of active players
//        activePlayers.clear();
//
//        // Close the server socket
//        close();
//    }

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

    public void initPlayerConnection( Socket playerSocket) throws IOException {
       
        if (playerSocket.isConnected()) {
            ClientHandler clientHandler = new ClientHandler(playerSocket);
            Thread playerListenr = new Thread(clientHandler);
            clientVector.add(clientHandler);
            if (isRunning) {
                playerListenr.start();
            } else {
                playerListenr.interrupt();
            }

        }
    }
}
