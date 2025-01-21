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
import network.requests.LoginRequest;
import network.requests.RegisterRequest;
import network.requests.Request;
import network.requests.StartGameRequest;
import network.responses.LoginResponse;
import network.responses.RegisterResponse;
import network.responses.Response;
import network.responses.StartGameResponse;

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
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
               
                Object request = in.readObject();

                if (request instanceof LoginRequest) {
                    handleLogin((LoginRequest) request);
                } else if (request instanceof RegisterRequest) {
                    handleRegister((RegisterRequest) request);
                } else if (request instanceof StartGameRequest) {
                    handleStartGameRequest( (StartGameRequest) request);
                    
                } else if (request instanceof StartGameResponse) {
                    handleStartGameResponse((StartGameResponse) request);
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
                // replace with SuccessLoginResponse
//                response = new LoginResponse(true, "Login successful!");
                
            } else {
                // repalce by FailLoginResponse
//                response = new LoginResponse(false, "Invalid username or password.");
            }

            // the error here will disapear when you write what is above
            // sendResponse(response);
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
                // replace with SuccessRegisterResponse
                // response = new RegisterResponse(true, "Registration successful!");
            } else {
                // replace with FailRegisterResponse
                // response = new RegisterResponse(false, "Username already exists.");
            }
                // the error here will disapear when you write what is above
//            sendResponse(response);
        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendResponse(Response response) {
        try {
            out.writeObject(response);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendRequest(Request request) {
        try {
            out.writeObject(request);
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

    private void handleStartGameRequest(StartGameRequest request) {
        String playerUsername = request.getUsername();
        // find the suitable thread to send the request to
        Server.clientVector.forEach((clientHandler) -> {
            // if the handler's name is the same as user name
            // send the request to. uncomment the next line
            // sendRequest(request);
        });
    }

    private void handleStartGameResponse(StartGameResponse response) {
        String playerUsername = response.getUsername();
        // send the response to the thread with this username
        // sendResponse(response);
    }
}
