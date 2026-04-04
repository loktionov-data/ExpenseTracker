package com.expensetracker.services;

import com.expensetracker.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Side;
import javafx.scene.chart.*;

import java.util.*;

//Service responsible for creating charts
public class ChartService {


    //PieChart builder
    public  PieChart createPieChart(FilteredList<Expense> expenses){

        //Preaggregation
        Map<String, Double> aggregatedData = new HashMap<>();

        for(Expense expense : expenses){
            aggregatedData.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        ObservableList<PieChart.Data> chartList = FXCollections.observableArrayList();

        //Supposed to be in data aggregation function
        for(Map.Entry<String, Double> entry : aggregatedData.entrySet()){
            chartList.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        //Creation of a piechart
        PieChart pieChart = new PieChart(chartList);

        pieChart.setTitle("Expenses on different categories");
        pieChart.setLegendSide(Side.LEFT);

        return pieChart;
    }

    //LineChartBuilder
    public LineChart<Number, Number> createLineChart(FilteredList<Expense> expenses, int year){
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Expenses during year");

        Map<Integer, Double> aggregatedData = new HashMap<>();
        //Pre-aggregation
        for (Expense expense: expenses){
            int month = expense.getDate().getMonthValue();
            double amount = expense.getAmount();
            aggregatedData.merge(month, amount, Double::sum);
        }

        //Supposed to be in data aggregation function
        for(Map.Entry<Integer, Double> entry: aggregatedData.entrySet()){
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        //Final chart creation
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Number of month");
        yAxis.setLabel("Amount of expenses");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);
        String title = "Amount of expenses for year %d";
        title = String.format(title, year);
        lineChart.setTitle(title);
        return lineChart;
    }

    public BarChart<String, Number> createBarChart(FilteredList<Expense> expenses){
        //Bar plot looks like xtick - category ytick - amount, bars - categoryNames

        //Creating a chart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);

        //Data pre-aggregation
        Map<String, HashMap<Integer, Double>> aggregated = new HashMap<>();

        for(Expense expense : expenses){
            String category = expense.getCategory();
            double amount = expense.getAmount();
            int month = expense.getDate().getMonthValue();
            aggregated.computeIfAbsent(category, k->new HashMap<>()).merge(month, amount, Double::sum);
        }

        //Add data to series and add series to plot
        for(Map.Entry<String, HashMap<Integer, Double>> aggData : aggregated.entrySet()){

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(aggData.getKey());
            for(Map.Entry<Integer, Double> data : aggData.getValue().entrySet()){
                series.getData().add(new XYChart.Data<>(String.valueOf(data.getKey()), data.getValue()));
            }
            bc.getData().add(series);
        }

        bc.setTitle("Expenses by different categories each month");
        xAxis.setLabel("Month");
        yAxis.setLabel("Amount");

        return bc;
    }

}
