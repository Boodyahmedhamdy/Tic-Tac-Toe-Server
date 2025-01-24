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
public class RefuseStartGameResponse extends StartGameResponse {
    
    public RefuseStartGameResponse(String senderUsername, String recieverUsername) {
        super(senderUsername, recieverUsername);
    }
    
}