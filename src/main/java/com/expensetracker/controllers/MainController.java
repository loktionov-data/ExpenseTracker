package com.expensetracker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private TabPane tabpane;

    @FXML
    private void initialize() throws Exception{
        //Loading and getting TableController
        FXMLLoader tabloader = new FXMLLoader(getClass().getResource("/com/expensetracker/table.fxml"));
        Tab tableTab = new Tab("Expenses", tabloader.load());
        TableController tableController = tabloader.getController();


        //Loading and getting ChartsController
        FXMLLoader chartLoader = new FXMLLoader(getClass().getResource("/com/expensetracker/graphs.fxml"));
        Tab chartTab = new Tab("Graphs", chartLoader.load());
        ChartsController chartsController = chartLoader.getController();

        tabpane.getTabs().addAll(tableTab, chartTab);
        chartsController.setTableController(tableController);


    }
}
