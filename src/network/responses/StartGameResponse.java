/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.responses;

/**
 *
 * @author Abdelrahman_Elshreif
 */

public class StartGameResponse extends Response {
    private String senderUsername;
    private String recieverUsername;

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecieverUsername() {
        return recieverUsername;
    }

    public void setRecieverUsername(String recieverUsername) {
        this.recieverUsername = recieverUsername;
    }

    public StartGameResponse(String senderUsername, String recieverUsername) {
        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
    }
}