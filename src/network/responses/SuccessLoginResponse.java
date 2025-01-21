/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.responses;

/**
 *
 * @author HP
 */
public class SuccessLoginResponse extends LoginResponse{
    
    private String username;
    private Integer score;

    public SuccessLoginResponse(String username, Integer score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public Integer getScore() {
        return score;
    }
    
    
    
}
