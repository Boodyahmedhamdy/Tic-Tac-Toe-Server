package tictactoeserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Abdelrahman_Elshreif
 */
public class ConnectedPlayer {

    public Socket playerSocket;
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public int id;

    public ConnectedPlayer(Socket playerSocket, int id) {
        try {
            this.playerSocket = playerSocket;
            this.id = id;
            System.out.println("Player " + id + ": Connected");
            this.in = new ObjectInputStream(playerSocket.getInputStream());
            this.out = new ObjectOutputStream(playerSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void readMessages() {
        String line = " ";
        while (!line.equals(Server.STOP_STRING)) {
            try {
                line = (String) in.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Player " + id + " :" + line);
        }
        System.out.println("Player" + id + ": Disconnected");
    }

    public void close() {
        try {
            if (playerSocket != null) {
                playerSocket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
