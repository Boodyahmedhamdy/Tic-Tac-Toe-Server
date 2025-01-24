/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.responses;

/**
 *
 * @author LENOVO
 */
public class PlayAtResponse extends Response{
    private static final long serialVersionUID = 1L;
    private String from;
    private String to;
    private int x;
    private int y;
    private String symbol;

    public PlayAtResponse(String from, String to, int x, int y, String symbol) {
        this.from = from;
        this.to = to;
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    
    
}
