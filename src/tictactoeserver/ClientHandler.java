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
import network.requests.PlayAtRequest;
import network.requests.RegisterRequest;
import network.requests.Request;
import network.requests.SignOutRequest;
import network.requests.StartGameRequest;
import network.requests.ReplayRequest;
import network.responses.FailLoginResponse;
import network.responses.FailRegisterResponse;
import network.responses.FailSignOutResponse;
import network.responses.LoginResponse;
import network.responses.PlayAtResponse;
import network.responses.RegisterResponse;
import network.responses.ReplayResponse;
import network.responses.Response;
import network.responses.StartGameResponse;
import network.responses.SuccessGetAvaialbePlayersResponse;
import network.responses.SuccessLoginResponse;
import network.responses.SuccessRegisterResponse;
import network.responses.SuccessSignOutResponse;

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

                    System.out.println("StartGameRequest received for username: " + ((StartGameRequest) request).getRecieverUsername());
                    handleStartGameRequest((StartGameRequest) request);

                } else if (request instanceof StartGameResponse) {
                    System.out.println("StartGameResponse received for username: " + ((StartGameResponse) request).getRecieverUsername());

                    handleStartGameResponse((StartGameResponse) request);

                } else if (request instanceof SignOutRequest) {

                    System.out.println("SignOutAction received for username: " + ((SignOutRequest) request).getUsername());
                    handleSignOutRequest((SignOutRequest) request);

                } else if(request instanceof PlayAtRequest){
                    System.out.println("PlayAt request received from username: " + ((PlayAtRequest) request).getFrom());
                    handlePlayAt((PlayAtRequest) request);
                  
                }else if(request instanceof ReplayRequest){
                    System.out.println("Replay request received from username: " + ((ReplayRequest) request).getFrom());
                    handleReplay((ReplayRequest) request);
                  
                }else {

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
                this.username = request.getUsername();
                TicTacToeServerController.activePlayers.add(userName);
                DataAccessLayer.updateIsOnline(userName, true);
                
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
            boolean isRegistered = DataAccessLayer.insert(new Player(
                    request.getUsername(),
                    request.getPassword(),
                    0, 0, true, false
            ));

            RegisterResponse response;
            if (isRegistered) {
                response = new SuccessRegisterResponse(userName, DataAccessLayer.getRANK(userName, Password));
                this.username = request.getUsername();
                TicTacToeServerController.activePlayers.add(userName);
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
        System.out.println("Started Closing the Client handler");
        isRunning = false;
        try {
            Server.clientVector.remove(this);
            Server.clientThreadVector.remove(Thread.currentThread());
            Platform.runLater(() -> {
                TicTacToeServerController.activePlayers.remove(this.username);
            });
            
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
        // the player to send to
        String playerUsername = request.getRecieverUsername();

        Server.clientVector.forEach((handler) -> {
            System.out.println("searching for the client name");
            System.out.println("the current one is " + handler.username);

            if (handler.username.equals(playerUsername)) {
                sendRequestOn(request, handler.out);
                System.out.println("StartGameRequest was sent to " + 
                        request.getRecieverUsername() + " from " + request.getSenderUsername());
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
        String playerUsername = response.getRecieverUsername();

        Server.clientVector.forEach((handler) -> {
            System.out.println("searching for the client name");
            System.out.println("the current one is " + handler.username);

            if (handler.username.equals(playerUsername)) {
                try {
                    int senderIsPlaying = DataAccessLayer.updateIsPlaying(response.getSenderUsername(), true);
                    int recieverIsPlaying = DataAccessLayer.updateIsPlaying(response.getRecieverUsername(), true);
                    if(senderIsPlaying > 0 && recieverIsPlaying > 0) {
                        sendResponseOn(response, handler.out);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        System.out.println("Finished handling StartGameResponse");
    }

    /**
     * updates the database as the Player is not playing and not online
     */
    private void handleSignOutRequest(SignOutRequest signOutRequest) {
        try {
            System.out.println("Start handling " + signOutRequest.getClass().getSimpleName());
            int isOnlineResult = DataAccessLayer.updateIsOnline(signOutRequest.getUsername(), false);
            int isPlayingResult = DataAccessLayer.updateIsPlaying(signOutRequest.getUsername(), false);
            
            if(isOnlineResult > 0 && isPlayingResult > 0) {
                System.out.println("Signed Out Successfully");
                SuccessSignOutResponse response = new SuccessSignOutResponse();

                ClientHandler handlerToClose = null;
                for(ClientHandler handler : Server.clientVector) {
                    if(handler.username.equals(signOutRequest.getUsername())) {
                        System.out.println("sending SuccessSignOutResponse to " + signOutRequest.getUsername());
                        sendResponseOn(response, handler.out);
                        handlerToClose = handler;
                        break;
                    }
                }
                handlerToClose.close();
                
            } else {
                System.out.println("Signed Out Failed");
                FailSignOutResponse response = new FailSignOutResponse();
                Server.clientVector.forEach((handler) -> {
                    if(handler.username.equals(signOutRequest.getUsername())) {
                        System.out.println("sending FailSignOutResponse to " + signOutRequest.getUsername());
                        sendResponseOn(response, handler.out);
                    }
                });
            }

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
    

    private void handlePlayAt(PlayAtRequest request) {
        //System.out.println("PlayAt request received for username: " + request.getFrom());
        PlayAtResponse response=new PlayAtResponse(request.getTo(),request.getFrom(),request.getX(),request.getY(),request.getSymbol(),request.IsGameOver());
        Server.clientVector.forEach((handler) -> {
          if(handler.username.equals(request.getTo())){
                sendResponseOn(response, handler.out);
                System.out.println("sending PlayAtRequest To player : " + request.getTo());

            }
        });

    }
    
    private void handleReplay(ReplayRequest request) {
        //System.out.println("Replay request received for username: " + request.getFrom());

        ReplayResponse response=new ReplayResponse(request.getTo(),request.getFrom(),request.isWantToPlayAgain());
        Server.clientVector.forEach((handler) -> {
            if(handler.username.equals(request.getTo())){
                sendResponseOn(response, handler.out);
                System.out.println("sending ReplayRequest To player : " + request.getTo());

            }
        });
        

    }

}
