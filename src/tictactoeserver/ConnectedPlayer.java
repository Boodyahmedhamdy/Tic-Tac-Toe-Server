package tictactoeserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Abdelrahman_Elshreif
 */
public class ConnectedPlayer extends Player {

    public Socket playerSocket;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public int id;

    public ConnectedPlayer(Socket playerSocket, int id) {
        try {
            this.playerSocket = playerSocket;
            this.id = id;
            System.out.println("Player " + id + ": Connected");
            this.in = new ObjectInputStream(new BufferedInputStream(playerSocket.getInputStream()));
            this.out = new ObjectOutputStream(new BufferedOutputStream(playerSocket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public void readMessages() {
        GamePlayAction action = null;
        while (isIsOnLine()) {
            try {
                try {
                    action = (GamePlayAction) in.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Player " + id + " :" + action.toString());
        }
        System.out.println("Player" + id + ": Disconnected");
    }

    public void close() {
        try {
            playerSocket.close();
            out.close();
            in.close();

        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
