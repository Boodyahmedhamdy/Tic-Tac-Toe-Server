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

}
