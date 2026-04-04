package com.expensetracker.controllers;

import com.expensetracker.services.ChartService;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import com.expensetracker.model.Expense;
import javafx.collections.transformation.FilteredList;


public class ChartsController {

    //All the necessary fields
    @FXML
    private StackPane charts;

    private TableController tableController;

    private FilteredList<Expense> chartData;

    private ChartService chartService;


    //Connect controller and set listener for the filters
    public void setTableController(TableController tablecontroller){
        this.tableController = tablecontroller;

        this.chartData = tablecontroller.getFilteredData();
        this.tableController.getFilteredData().addListener((ListChangeListener<Expense>) change ->{
            updateCharts();
        });
    }

    private void updateCharts(){
        if(tableController == null){
            return;
        }
        chartData = this.tableController.getFilteredData();

        Class<?> typeofobject = charts.getChildren().getLast().getClass();

        if(typeofobject == BarChart.class){
            buildBarChart();
        }

        else if(typeofobject == LineChart.class){
            buildLineChart();
        }
        else if(typeofobject == PieChart.class){
            buildPieChart();
        }
    }

    //Methods for setting up the service
    public void setChartService(ChartService chartservice){

        this.chartService = chartservice;
    }

    //Chart builders
    public void buildBarChart(){
        BarChart<String, Number> chart = chartService.createBarChart(chartData);
        buildPlot(chart);
    }

    public void buildLineChart(){
        int number = chartData.getFirst().getDate().getYear();
        LineChart<Number, Number> lineChart = chartService.createLineChart(chartData, number);
        buildPlot(lineChart);
    }

    public void buildPieChart(){
        PieChart pieChart = chartService.createPieChart(chartData);
        buildPlot(pieChart);
    }
    public <T extends Chart> void buildPlot(T chart){
        charts.getChildren().clear();
        charts.getChildren().addAll(chart);
    }

    @FXML
    private void initialize(){
        setChartService(new ChartService());
    }


}
