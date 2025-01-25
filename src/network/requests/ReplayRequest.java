/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.requests;

/**
 *
 * @author LENOVO
 */
public class ReplayRequest extends Request{
    private static final long serialVersionUID = 1L;
    String from;
    String to;
    boolean wantToPlayAgain;

    public ReplayRequest(String from, String to, boolean wantToPlayAgain) {
        this.from = from;
        this.to = to;
        this.wantToPlayAgain = wantToPlayAgain;
    }

    

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    
    public boolean isWantToPlayAgain() {
        return wantToPlayAgain;
    }

    

    
    
}
