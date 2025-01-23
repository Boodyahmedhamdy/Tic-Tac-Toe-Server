/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Laptop World
 */
public class TestLoginController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button registerbtn;
    @FXML
    private Button loginbtn;
    @FXML
    private Label lable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        registerbtn.setOnAction((event)->{
            Player player = new Player(nameField.getText(),passwordField.getText(),0,0,false,false);
            try {
                 boolean result = DataAccessLayer.insert(player);
                    if(result==true){
                         lable.setText("Insert Done"); 
                    }else{
                        lable.setText("Insert Faild"); 
                    }
            } catch (SQLException ex) {
                 lable.setText("Insert Faild Try Another Username "); 
            }
        });
        loginbtn.setOnAction((event) -> {
      String username = nameField.getText();
      String password = passwordField.getText();

       try {
        boolean passwordResult = DataAccessLayer.checkPassword(username, password);
        boolean userResult=DataAccessLayer.checkUser(username, password);
        int result = DataAccessLayer.getRANK(username, password);
        if(userResult && passwordResult){
         lable.setText("Exist and wright password"+ result);
         
        }else if(userResult&& !passwordResult ){
             lable.setText("Exist and wrong password");
        }else if(!userResult){
           lable.setText("Not Exist ");
        }
        
    } catch (SQLException ex) {
        Logger.getLogger(TestLoginController.class.getName()).log(Level.SEVERE, null, ex);
    }
});
         
    }    
    
}
