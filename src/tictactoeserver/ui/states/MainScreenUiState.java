/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.ui.states;

import javafx.collections.ObservableList;

/**
 *
 * @author HP
 */
public class MainScreenUiState {
    
    public static final String ON = "ON";
    public static final String OFF = "OFF";
    
    String serverStatus;
    String serverIp;
    ObservableList<String> availablePlayers; // will be modified later
    String errorMessage;
    
    public MainScreenUiState(String serverStatus, String serverIp, ObservableList<String> players, String errorMessage) {
        this.serverStatus = serverStatus;
        this.serverIp = serverIp;
        this.availablePlayers = players;
        this.errorMessage = errorMessage;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public ObservableList<String> getPlayers() {
        return availablePlayers;
    }

    public void setPlayers(ObservableList<String> players) {
        this.availablePlayers = players;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    
    
}
