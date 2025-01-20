/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laptop World
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {
               
                Object request = in.readObject();

                if (request instanceof LoginRequest) {
                    handleLogin((LoginRequest) request);
                } else if (request instanceof RegisterRequest) {
                    handleRegister((RegisterRequest) request);
                } else {
                    System.out.println("Unknown request received.");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } finally {
            close();
        }
    }

    private void handleLogin(LoginRequest request) {
        try {
            System.out.println("Login request received for username: " + request.getUsername());

            boolean isUserValid = DataAccessLayer.checkUser(request.getUsername(), request.getPassword());
            boolean isPasswordValid = DataAccessLayer.checkPassword(request.getUsername(), request.getPassword());

            LoginResponse response;
            if (isUserValid && isPasswordValid) {
                response = new LoginResponse(true, "Login successful!");
                //suc
            } else {
                response = new LoginResponse(false, "Invalid username or password.");
            }

            sendResponse(response);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleRegister(RegisterRequest request) {
        try {
            System.out.println("Register request received for username: " + request.getUsername());

            boolean isRegistered = DataAccessLayer.insert(new Player(
                    request.getUsername(),
                    request.getPassword(),
                    0, 0, true, false
            ));

            RegisterResponse response;
            if (isRegistered) {
                response = new RegisterResponse(true, "Registration successful!");
            } else {
                response = new RegisterResponse(false, "Username already exists.");
            }

            sendResponse(response);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendResponse(Object response) {
        try {
            out.writeObject(response);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void close() {
        try {
            Server.clientVector.remove(this); 
            if (clientSocket != null) clientSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
