/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

/**
 *
 * @author Abdelrahman Elshreif
 */
public class Player {

    private String userName;
    private String password;
    private int rank;
    private int matchesNum;
    private boolean isOnLine;
    private boolean isPlaying;

    public Player(String userName, String password, int rank, int matchesNum, boolean isOnLine, boolean isPlaying) {
        this.userName = userName;
        this.password = password;
        this.rank = rank;
        this.matchesNum = matchesNum;
        this.isOnLine = isOnLine;
        this.isPlaying = isPlaying;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getRank() {
        return rank;
    }

    public int getMatchesNum() {
        return matchesNum;
    }

    public boolean isIsOnLine() {
        return isOnLine;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setMatchesNum(int matchesNum) {
        this.matchesNum = matchesNum;
    }

    public void setIsOnLine(boolean isOnLine) {
        this.isOnLine = isOnLine;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
