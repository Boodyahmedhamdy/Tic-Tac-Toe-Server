package tictactoeserver;

import tictactoeserver.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerSocket {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner scanner;

    public PlayerSocket() {
        try {
            // Initialize the socket and streams
            this.socket = new Socket("127.0.0.1", Server.PORT);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            this.scanner = new Scanner(System.in);

            System.out.println("Connected to the server.");

            // Start a thread to read messages from the server
            new Thread(this::readMessages).start();

            // Start writing messages to the server
            writeMessages();
        } catch (IOException ex) {
            Logger.getLogger(PlayerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeMessages() {
        String line = "";
        while (!line.equals(Server.STOP_STRING)) {
            try {
                // Read input from the user
                line = scanner.nextLine();
                // Send the message to the server
                out.writeUTF(line);
            } catch (IOException ex) {
                Logger.getLogger(PlayerSocket.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }
        close();
    }

    private void readMessages() {
        try {
            while (true) {
                // Read messages from the server
                String message = in.readUTF();
                System.out.println("Server: " + message);

                // Stop if the server sends the stop string
                if (message.equals(Server.STOP_STRING)) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("Disconnected from the server.");
        } catch (IOException ex) {
            Logger.getLogger(PlayerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new PlayerSocket();
    }
}
