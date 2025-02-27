/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Laptop World
 */
public class DataAccessLayer {

    private static Connection con;
    private static ResultSet rs;
    private static PreparedStatement st;

    static {
        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/Server", "player", "player");

            PreparedStatement st = con.prepareStatement("SELECT * FROM Player", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean insert(Player player) throws SQLException {
        boolean finalResult = false;
        PreparedStatement st = con.prepareStatement("INSERT INTO PLAYER (USERNAME,PASSWORD,RANK,NUMBEROFMATCHES,ISONLINE,ISPLAYING) VALUES (?, ?, ?, ?, ?, ?)");
        st.setString(1, player.getUserName());
        st.setString(2, player.getPassword());
        st.setInt(3, player.getRank());
        st.setInt(4, player.getMatchesNum());
        st.setBoolean(5, player.isIsOnLine());
        st.setBoolean(6, player.isIsPlaying());
        int result = st.executeUpdate();
        if (result > 0) {
            finalResult = true;
        }
        return finalResult;
    }

    public static boolean checkUser(String username, String password) throws SQLException {
        boolean finalResult = false;
        PreparedStatement st = con.prepareStatement("SELECT USERNAME FROM PLAYER WHERE USERNAME = ?");
        st.setString(1, username);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            String userName = rs.getString("USERNAME");
            if (userName.equals(userName)) {
                finalResult = true;
            }
        }
        rs.close();
        st.close();
        return finalResult;
    }

    public static boolean checkPassword(String username, String password) throws SQLException {
        boolean finalResult = false;
        PreparedStatement st = con.prepareStatement("SELECT PASSWORD FROM PLAYER WHERE USERNAME = ?");
        st.setString(1, username);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            String storedPassword = rs.getString("PASSWORD");
            if (storedPassword.equals(password)) {
                finalResult = true;
            }
        }
        rs.close();
        st.close();
        return finalResult;
    }

    public static int getRANK(String username, String password) throws SQLException {
        int rank = 0;
        PreparedStatement st = con.prepareStatement("SELECT RANK FROM PLAYER WHERE USERNAME = ?");
        st.setString(1, username);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            rank = rs.getInt("RANK");

        }
        rs.close();
        st.close();
        return rank;
    }
     public static int getOnLinePlayersNumber() throws SQLException {
    int num = 0;
    PreparedStatement st = con.prepareStatement("SELECT COUNT(*) AS online_count FROM PLAYER WHERE ISONLINE = true");
    ResultSet rs = st.executeQuery();
    if (rs.next()) {
        num = rs.getInt("online_count"); 
    }
    rs.close();
    st.close();
    return num; 
}
     public static int getoffLinePlayersNumber() throws SQLException {
    int num = 0;

    PreparedStatement st = con.prepareStatement("SELECT COUNT(*) AS offline_count FROM PLAYER WHERE ISONLINE = false");
    ResultSet rs = st.executeQuery();
    if (rs.next()) {
        num = rs.getInt("offline_count"); 
    }
    rs.close();
    st.close();
    return num; 
}
    
    /**
     * gets a the player with passed username. returns null if any player isn't
     * found
     *
     * @param username
     * @return Player with username or null if it isn't found
     * @throws java.sql.SQLException
     */
    public static Player getPlayerByUsername(String username) throws SQLException {
        PreparedStatement st = con.prepareStatement("SELECT * FROM PLAYER WHERE USERNAME = ?");
        st.setString(1, username);
        ResultSet result = st.executeQuery();
        if (result.next()) {
            Player player = convertResultSetIntoPlayer(result);
            return player;
        }
        return null;
    }

    public static int updateIsOnline(String username, boolean isOnline) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "UPDATE PLAYER SET ISONLINE = ? WHERE USERNAME = ?"
        );
        st.setBoolean(1, isOnline);
        st.setString(2, username);
        int result = st.executeUpdate();
        st.close();
        return result;
    }

    public static int updateIsPlaying(String username, boolean isPlaying) throws SQLException {
        PreparedStatement st = con.prepareStatement(
                "UPDATE PLAYER SET ISPLAYING = ? WHERE USERNAME = ?"
        );
        st.setBoolean(1, isPlaying);
        st.setString(2, username);
        int result = st.executeUpdate();
        st.close();
        return result;
    }

    /**
     * to convert the passed result set into a Player to deal with
     */
    public static Player convertResultSetIntoPlayer(ResultSet result) throws SQLException {
        Player player = new Player(
                result.getString("USERNAME"),
                result.getString("PASSWORD"),
                result.getInt("RANK"),
                result.getInt("NUMBEROFMATCHES"),
                result.getBoolean("ISONLINE"),
                result.getBoolean("ISPLAYING")
        );
        return player;
    }
}
