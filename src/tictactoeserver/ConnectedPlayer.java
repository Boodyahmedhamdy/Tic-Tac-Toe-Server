package tictactoeserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoeserver.PlayerSocket;
import tictactoeserver.Server;

/**
 * @author Abdelrahman_Elshreif
 */
public class ConnectedPlayer {

    public Socket playerSocket;
    public DataInputStream in;
    public int id;

    public ConnectedPlayer(Socket playerSocket, int id) {
        try {
            this.playerSocket = playerSocket;
            this.id = id;
            System.out.println("Player " + id + ": Connected");
            this.in = new DataInputStream(new BufferedInputStream(playerSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void readMessages() {
        String line = " ";
        while (!line.equals(Server.STOP_STRING)) {
            try {
                line = in.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Player " + id + " :" + line);
        }
        System.out.println("Player" + id + ": Disconnected");
    }

    public void close() {
        try {
            playerSocket.close();
            in.close();

        } catch (IOException ex) {
            Logger.getLogger(ConnectedPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
