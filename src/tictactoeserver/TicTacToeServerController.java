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
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    
    public static ObservableList<String> activePorts;
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
        activePorts = FXCollections.observableArrayList();
        System.out.println("After creating active ports");

        System.out.println("MainScreenUiState created... ");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        uiState = new MainScreenUiState(
//                MainScreenUiState.OFF, "", FXCollections.observableList(new ArrayList<>()), ""
//        );
        activePorts = FXCollections.observableArrayList();
        textServerStatus.setText(uiState.getServerStatus());
        textServerIp.setText(uiState.getServerIp());
        textErrorMessage.setText(uiState.getErrorMessage());
        System.out.println(uiState.getPlayers().toString() + " before ");
        uiState.setPlayers(
                FXCollections.observableList(Server.getOnlinePlayers())
        );
        lvAvailablePlayers.setItems(activePorts);
        System.out.println(uiState.getPlayers().toString() + " After ");
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
        server = new Server();
        isServerRunning = true;
        serverThread = new Thread(() -> {
            server.start();
        });
        serverThread.start();
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
        isServerRunning = false;
//        if (server != null) {
//            server.shutdown(); // Ensure all resources are cleaned up
//        }
        if (serverThread != null) {
            serverThread.interrupt(); // Interrupt the server thread
        }
        uiState.setServerStatus(MainScreenUiState.OFF);
        textServerStatus.setFill(Color.WHITE);
        uiState.setServerIp("");
        btnToggleServer.setText("Turn Sever ON");
    }

}
