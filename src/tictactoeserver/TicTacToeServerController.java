/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import tictactoeserver.ui.states.MainScreenUiState;
import tictactoeserver.Server;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author HP
 */
public class TicTacToeServerController implements Initializable {

    @FXML
    private Text textServerStatus;
    @FXML
    private Text textServerIp;
    @FXML
    private ListView<String> lvAvailablePlayers;

    
    public static ObservableList<String> activePlayers;

    @FXML
    private Button btnToggleServer;
    @FXML
    private Button btnPlayersStatistics;
    @FXML
    private Text textErrorMessage;

    private Thread serverThread;
    Server server;
    private volatile boolean isServerRunning = false;
    public static MainScreenUiState uiState;

    static {
        System.out.println("MainScreenUiState creating... ");
        uiState = new MainScreenUiState(
                MainScreenUiState.OFF, "", FXCollections.observableList(new ArrayList<>()), ""
        );
        System.out.println("before creating active ports");
        activePlayers = FXCollections.observableArrayList();
        System.out.println("After creating active ports");

        System.out.println("MainScreenUiState created... ");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        uiState = new MainScreenUiState(
//                MainScreenUiState.OFF, "", FXCollections.observableList(new ArrayList<>()), ""
//        );

        activePlayers = FXCollections.observableArrayList();
        textServerStatus.setText(uiState.getServerStatus());
        textServerIp.setText(uiState.getServerIp());
        textErrorMessage.setText(uiState.getErrorMessage());
        System.out.println(uiState.getPlayers().toString() + " before ");
        uiState.setPlayers(
                FXCollections.observableList(Server.getOnlinePlayers())
        );

        activePlayers = FXCollections.observableList(Server.getOnlinePlayers());
        lvAvailablePlayers.setItems(activePlayers);
        System.out.println(uiState.getPlayers().toString() + " After ");
        Platform.setImplicitExit(true);

    }

    public void setStage(Stage stage) {
        stage.setOnCloseRequest((WindowEvent event) -> {
            if (isServerRunning) {
                turnSeverOff();
            }
            System.out.println("Server window closed. setStage Method");
        });
    }

    @FXML
    void handleToggleBtn(ActionEvent event) {
        System.out.println("Toggle button clicked");
        toggle();
        textServerStatus.setText(uiState.getServerStatus());
        textServerIp.setText(uiState.getServerIp());
    }

    @FXML
    void handleShowStatisticsBtn(ActionEvent event) {
        System.out.println("Statistics Button Clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Graph.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPlayersStatistics.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void toggle() {
        if (uiState.getServerStatus().equals(MainScreenUiState.OFF)) {
            turnSeverOn();
        } else {
            turnSeverOff();
        }
    }

    void turnSeverOn() {
        if (server == null) {
            server = new Server();
            isServerRunning = true;
        }

        if (serverThread == null) {
            serverThread = new Thread(() -> {
                server.start();
            });
            serverThread.start();
        }

        uiState.setServerStatus(MainScreenUiState.ON);
        textServerStatus.setFill(Color.WHITE);
        String serverIp = getServerIp();
        uiState.setServerIp(serverIp); // example for random ip
        btnToggleServer.setText("Turn Sever OFF");
    }

    String getServerIp() {
        try {
            // Get the local host address
            InetAddress localHost = InetAddress.getLocalHost();
            // Get the IP address as a string
            String ipAddress = localHost.getHostAddress();

            return ipAddress;
        } catch (UnknownHostException ex) {
            Logger.getLogger(TicTacToeServerController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    void turnSeverOff() {
        // Ensure UI updates happen on the JavaFX Application Thread

        // Reset server-related UI elements
        isServerRunning = false;
        uiState.setServerStatus(MainScreenUiState.OFF);
        textServerStatus.setFill(Color.WHITE);
        uiState.setServerIp("");
        btnToggleServer.setText("Turn Server ON");



        // Close server and client connections
        if (server != null) {
            server.close();
        }

        synchronized (Server.clientVector) {
            for (ClientHandler handler : Server.clientVector) {
                if (handler != null) {
                    handler.close();
                }
            }
            Server.clientVector.clear();
        }

        // Ensure the server thread is terminated
        if (serverThread != null) {
            serverThread.interrupt();
            serverThread = null;
        }
    }
}
