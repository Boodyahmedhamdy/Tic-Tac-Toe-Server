/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.net.URL;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import tictactoeserver.ui.states.MainScreenUiState;

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
    @FXML
    private Button btnToggleServer;
    @FXML
    private Button btnPlayersStatistics;
    @FXML
    private Text textErrorMessage;
    
    MainScreenUiState uiState;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        uiState = new MainScreenUiState(
                MainScreenUiState.OFF, "192.168.1.1", FXCollections.observableList(new ArrayList<>()), ""
        );
        textServerStatus.setText(uiState.getServerStatus());
        textServerIp.setText(uiState.getServerIp());
        textErrorMessage.setText(uiState.getErrorMessage());
        lvAvailablePlayers.setItems(uiState.getPlayers());
        
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
    }
    
    void toggle() {
        if(uiState.getServerStatus().equals(MainScreenUiState.OFF)) 
            turnSeverOn();
        else 
            turnSeverOff();
    }
    
    void turnSeverOn() {
        uiState.setServerStatus(MainScreenUiState.ON);
        textServerStatus.setFill(Color.GREEN);
        uiState.setServerIp("12.235.1.99"); // example for random ip
        btnToggleServer.setText("Turn Sever OFF");
    }
    
    void turnSeverOff() {
        uiState.setServerStatus(MainScreenUiState.OFF);
        textServerStatus.setFill(Color.RED);
        uiState.setServerIp("");
        btnToggleServer.setText("Turn Sever ON");
    }
    
}
