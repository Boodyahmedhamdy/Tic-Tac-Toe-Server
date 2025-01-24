/*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package tictactoeserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import network.actions.Action;
import network.actions.SignOutAction;
import network.requests.GetAvailablePlayersRequest;
import network.requests.LoginRequest;
import network.requests.RegisterRequest;
import network.requests.Request;
import network.requests.StartGameRequest;
import network.responses.FailLoginResponse;
import network.responses.FailRegisterResponse;
import network.responses.LoginResponse;
import network.responses.RegisterResponse;
import network.responses.Response;
import network.responses.StartGameResponse;
import network.responses.SuccessGetAvaialbePlayersResponse;
import network.responses.SuccessLoginResponse;
import network.responses.SuccessRegisterResponse;

/**
 *
 * @author Laptop World
 */
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    public String username;
    private boolean isRunning;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        isRunning = true;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (isRunning) {
                System.out.println("**************SERVER CLIENT-HANDLER RUN() BEFORE READ OBJECT ************");
                Object request = in.readObject();
                System.out.println("**************SERVER CLIENT-HANDLER RUN() AFTER READ OBJECT ************");

                if (request instanceof LoginRequest) {
                    System.out.println("Login request received for username: " + ((LoginRequest) request).getUsername());
                    handleLogin((LoginRequest) request);

                } else if (request instanceof RegisterRequest) {
                    System.out.println("Register request received for username: " + ((RegisterRequest) request).getUsername());
                    handleRegister((RegisterRequest) request);

                } else if (request instanceof GetAvailablePlayersRequest) {
//                    System.out.println("GetAvailablePlayersRequest request received for username: " + ((GetAvailablePlayersRequest) request).getUsername());
                    System.out.println("GetAvailablePlayersRequest request received for username: " + ((GetAvailablePlayersRequest) request));
                    handleGetAvailablePlayersRequest((GetAvailablePlayersRequest) request);

                } else if (request instanceof StartGameRequest) {

                    System.out.println("StartGameRequest received for username: " + ((StartGameRequest) request).getUsername());
                    handleStartGameRequest((StartGameRequest) request);

                } else if (request instanceof StartGameResponse) {
                    System.out.println("StartGameResponse received for username: " + ((StartGameRequest) request).getUsername());

                    handleStartGameResponse((StartGameResponse) request);

                } else if (request instanceof SignOutAction) {

                    System.out.println("SignOutAction received for username: " + ((SignOutAction) request).getUsername());
                    handleSignOutAction((SignOutAction) request);

                } else {
                    System.out.println("Unknown request received: " + request.getClass().getSimpleName());
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            ex.printStackTrace(); // Add this line to print the stack trace
        } finally {
            close();
        }
    }

    private void handleLogin(LoginRequest request) {
        try {
            System.out.println("Login request received for username: " + request.getUsername());
            String userName = request.getUsername();
            String Password = request.getPassword();
            int rank = DataAccessLayer.getRANK(userName, Password);
            boolean isUserValid = DataAccessLayer.checkUser(userName, Password);
            boolean isPasswordValid = DataAccessLayer.checkPassword(userName, Password);
            LoginResponse response;
            if (isUserValid && isPasswordValid) {

                response = new SuccessLoginResponse(userName, rank);
                username = request.getUsername();
                TicTacToeServerController.activePlayers.add(userName);

            } else {
                response = new FailLoginResponse("Invalid username or password.");
            }

            sendResponseOn(response, out);

        } catch (Exception ex) {
            System.out.println("*************FROM HANDLE LOGIN****************");
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleRegister(RegisterRequest request) {
        try {
            System.out.println("Register request received for username: " + request.getUsername());
            String userName = request.getUsername();
            String Password = request.getPassword();
            int rank = DataAccessLayer.getRANK(userName, Password);
            boolean isRegistered = DataAccessLayer.insert(new Player(
                    request.getUsername(),
                    request.getPassword(),
                    0, 0, true, false
            ));

            RegisterResponse response;
            if (isRegistered) {

                response = new SuccessRegisterResponse(userName, rank);
            } else {
                response = new FailRegisterResponse("Invalid username or password.");
            }

            sendResponseOn(response, out);

        } catch (Exception ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendResponseOn(Response response, ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendRequestOn(Request request, ObjectOutputStream outputStream) {
        try {
            System.out.println("Sending " + request.getClass().getSimpleName() + " from sendRequestOn");

            outputStream.writeObject(request);
            outputStream.flush();

            System.out.println("sent " + request.getClass().getSimpleName() + " from sendRequestOn");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendActionOn(Action action, ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject(action);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        isRunning = false;
        try {
            Server.clientVector.remove(this);
            Server.clientThreadVector.remove(Thread.currentThread());

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * sends the request to the suitable user (whose username is inside the
     * response body)
     */
    private void handleStartGameRequest(StartGameRequest request) {
        System.out.println("start handling StartGameRequest");
        String playerUsername = request.getUsername();

        Server.clientVector.forEach((handler) -> {
            System.out.println("searching for the client name");
            System.out.println("the current one is " + handler.username);

            if (handler.username.equals(playerUsername)) {
                sendRequestOn(request, handler.out);
            }
        });
        System.out.println("Finished handling StartGameRequest");
    }

    /**
     * sends the response to the suitable user (whose username is inside the
     * response body)
     */
    private void handleStartGameResponse(StartGameResponse response) {
        System.out.println("start handling StartGameResponse");
        String playerUsername = response.getUsername();

        Server.clientVector.forEach((handler) -> {
            System.out.println("searching for the client name");
            System.out.println("the current one is " + handler.username);

            if (handler.username.equals(playerUsername)) {
                sendResponseOn(response, handler.out);
            }
        });
        System.out.println("Finished handling StartGameResponse");
    }

    /**
     * updates the database as the Player is not playing and not online
     */
    private void handleSignOutAction(SignOutAction signOutAction) {
        try {
            System.out.println("Start handling " + signOutAction.getClass().getSimpleName());
            DataAccessLayer.updateIsOnline(signOutAction.getUsername(), false);
            DataAccessLayer.updateIsPlaying(signOutAction.getUsername(), false);
//             kill the thread here
            System.out.println("Here Must kill the Thread because signout is handled");

            Platform.runLater(() -> {
                TicTacToeServerController.activePorts.remove(signOutAction.getUsername());
            });
        } catch (SQLException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleGetAvailablePlayersRequest(GetAvailablePlayersRequest getAvailablePlayersRequest) {
        ArrayList<String> usernames = new ArrayList<>();
        Server.clientVector.forEach((handler) -> {

            if(!handler.username.equals(this.username)){
                usernames.add(handler.username);
            }
        });
        System.out.println("got list of usernames " + usernames);
        SuccessGetAvaialbePlayersResponse response = new SuccessGetAvaialbePlayersResponse(usernames);

        System.out.println("resposne list: " + response.getUsernames());
        sendResponseOn(response, this.out);
        System.out.println("sent SuccessGetAvaialbePlayersResponse to client");
       
    }
}
