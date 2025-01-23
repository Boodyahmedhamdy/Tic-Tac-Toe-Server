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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author Laptop World
 */
public class GraphController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            int Online = DataAccessLayer.getOnLinePlayersNumber();
            int offline = DataAccessLayer.getoffLinePlayersNumber();
           
            XYChart.Series<String, Number> onlineSeries = new XYChart.Series<>();
            onlineSeries.setName("Online");
            onlineSeries.getData().add(new XYChart.Data<>("Online", Online));
            
            XYChart.Series<String, Number> offlineSeries = new XYChart.Series<>();
            offlineSeries.setName("Offline");
            offlineSeries.getData().add(new XYChart.Data<>("Offline", offline));
            barChart.getData().addAll(onlineSeries, offlineSeries);
        } catch (SQLException ex) {
            Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
    }    
    
}
