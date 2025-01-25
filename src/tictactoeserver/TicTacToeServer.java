/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ThreadPoolExecutor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author HP
 */
public class TicTacToeServer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TicTacToeServer.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Graph.fxml"));

        //  Parent root = FXMLLoader.load(getClass().getResource("TestLogin.fxml"));
        Scene scene = new Scene(root);

        /*
        DriverManager.registerDriver(new ClientDriver());
        // Abdelrahman Elshreif Local Db Connection 
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe", "root", "root");
         */
        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
