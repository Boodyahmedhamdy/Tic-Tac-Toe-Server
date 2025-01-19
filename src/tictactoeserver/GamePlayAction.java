/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Abdelrahman_Elshreif
 */
public class GamePlayAction implements Serializable {

    Point position;
    Character symbol;

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public Point getPosition() {
        return position;
    }

    public Character getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return this.getPosition() + " " + this.getSymbol();
    }

}
