package com.expensetracker.controllers;
import com.expensetracker.model.Expense;
import com.expensetracker.services.ExpenseService;
import com.expensetracker.services.FilterService;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TableController {
    @FXML
    private TableView<Expense> expenseTable;

    //Columns for the properties
    @FXML
    private TableColumn<Expense, String > categoryColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;


    //Container for input fields
    @FXML
    private HBox inputBox;

    //Input fields for creating a new expenses
    @FXML
    private TextField categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    //Displays total sum of the expenses
    @FXML
    private Label totalSum;

    //Filter controls
    @FXML
    private ComboBox<String> categoryFilterBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    //Button for resetting filters
    @FXML
    private Button resetButton;

    //Business logic services
    private ExpenseService expenseService;
    private FilteredList<Expense> filteredData;

    //Wrapper for the data(used for dynamic filtering in the tableView)
    private FilterService filterService;

    //Setting a service for handling expense operations
    private void setExpenseService(ExpenseService service){
        this.expenseService = service;

        //Initialize filtered list (No filters applied by default)
        filteredData = new FilteredList<>(expenseService.getExpenses(), p->true);

        //Wrapping filtered table into the sortedList to make sort by date possible
        SortedList<Expense> sortedData = new SortedList<Expense>(filteredData);
        sortedData.comparatorProperty().bind(expenseTable.comparatorProperty());
        expenseTable.setItems(sortedData);
    }

    //Setting up a filter service
    private void setFilterService(FilterService service){
        this.filterService = service;
    }

    public FilteredList<Expense> getFilteredData(){
        return new FilteredList<Expense>(filteredData);
    }
    //Applying filters
    private void applyFilters(){
        //Reading the filter values from UI
        String selectedCategory = categoryFilterBox.getValue();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        //Applying the filters using predicate
        filteredData.setPredicate(filterService.createPredicate(selectedCategory, fromDate, toDate));

    }

    //INIT
    @FXML
    public void initialize(){
        //Setting up front end details
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        expenseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        datePicker.setDayCellFactory(picker-> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);

                if(date.isAfter(LocalDate.now())){
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        categoryField.prefWidthProperty().bind(inputBox.widthProperty().multiply(0.2));
        amountField.prefWidthProperty().bind(inputBox.widthProperty().multiply(0.2));
        datePicker.prefWidthProperty().bind(inputBox.widthProperty().multiply(0.13));

        //Setting up the services
        setExpenseService(new ExpenseService());
        setFilterService(new FilterService());

        //Updating category filters and total sum value
        updateCategoryFilter();
        updateTotalSum();

        //Adding listeners for the filters
        categoryFilterBox.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        fromDatePicker.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        toDatePicker.valueProperty().addListener((obs, oldV, newV) -> applyFilters());

        //Setting up default filters
        categoryFilterBox.setValue("All");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

    }

    //Update total sum function
    @FXML
    private void updateTotalSum(){
        double total = expenseService.getTotalSum();
        totalSum.setText("Total: " + total);
    }

    //Function for adding new expenses
    @FXML
    private void addExpense(){
        //Getting all the values from UI
        String categoryToAdd = categoryField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();

        //Checking if the amount values is possible to add
        double amountToAdd;
        if(categoryToAdd == null || categoryToAdd.isEmpty()){
            return;
        }

        try{
            amountToAdd = Double.parseDouble(amountText);
        }catch (NumberFormatException e)
        {
            return;
        }

        //Adding the expense to the table of expenses and to the json
        expenseService.addExpense(categoryToAdd, amountToAdd, date);

        //Updating sum and resetting filters
        updateTotalSum();
        updateCategoryFilter();

        //Clearing all the UI elements
        categoryField.clear();
        amountField.clear();
        datePicker.setValue(null);
        applyFilters();
    }

    //Sort by date function
    @FXML
    private void sortByDate(){
        //Setting an ascending sort type for the date column
        dateColumn.setSortType(TableColumn.SortType.ASCENDING);
        //Applying sort type to the expense table
        expenseTable.getSortOrder().clear();
        expenseTable.getSortOrder().add(dateColumn);
    }


    //Updating category comboBox based on the values of the expenseList
    private void updateCategoryFilter(){
        Set<String> categories = expenseService.getCategories();

        List<String> items = new ArrayList<>();
        items.add("All");
        items.addAll(categories);

        categoryFilterBox.getItems().setAll(items);
    }

    //Deleting the expenses
    @FXML
    private void deleteExpense(){
        //Getting the items and if the list isn't empty
        var selected = expenseTable.getSelectionModel().getSelectedItems();

        if(!selected.isEmpty()){
            expenseService.deleteExpenses(selected);
            updateCategoryFilter();
            applyFilters();
        }
    }

    //Resetting all the filters
    @FXML
    private void resetFilters(){
        categoryFilterBox.setValue("All");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

        applyFilters();
    }
}
