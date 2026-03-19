package com.expensetracker;

import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.expensestorage.ExpenseStorage;
import javafx.scene.layout.HBox;

public class MainController {
    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String > categoryColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;


    @FXML
    private HBox inputBox;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label totalSum;

    @FXML
    private ComboBox<String> categoryFilterBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button resetButton;

    private ExpenseService expenseService;
    private FilteredList<Expense> filteredData;

    private void setExpenseService(ExpenseService service){
        this.expenseService = service;
        filteredData = new FilteredList<>(expenseService.getExpenses(), p->true);
        expenseTable.setItems(filteredData);
    }
    private void applyFilters(){

        String selectedCategory = categoryFilterBox.getValue();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        filteredData.setPredicate(expense -> {

            if(expense == null){
                return false;
            }

            boolean categoryMatch = (selectedCategory == null || selectedCategory.equals("All")
                    || expense.getCategory().equalsIgnoreCase(selectedCategory));

            boolean fromMatch = (fromDate == null ||
                    (expense.getDate() != null && !expense.getDate().isBefore(fromDate)));

            boolean toMatch = (toDate == null ||
                    (expense.getDate() != null && !expense.getDate().isAfter(toDate)));

            return categoryMatch && fromMatch && toMatch;
        });
    }

    @FXML
    public void initialize(){
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.prefWidthProperty().bind(expenseTable.widthProperty().divide(3));

        expenseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setExpenseService(new ExpenseService());
        updateCategoryFilter();
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


        categoryFilterBox.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        fromDatePicker.valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        toDatePicker.valueProperty().addListener((obs, oldV, newV) -> applyFilters());

        categoryFilterBox.setValue("All");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

    }

    @FXML
    private void updateTotalSum(){
        double total = expenseService.getTotalSum();
        totalSum.setText("Total: " + total);
    }
    @FXML
    private void addExpense(){
        String categoryToAdd = categoryField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();

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

        expenseService.addExpense(categoryToAdd, amountToAdd, date);
        updateTotalSum();
        updateCategoryFilter();
        categoryField.clear();
        amountField.clear();
        datePicker.setValue(null);
    }

    @FXML
    private void sortByDate(){
        expenseTable.getSortOrder().add(dateColumn);
    }

    private void updateCategoryFilter(){
        Set<String> categories = expenseService.getExpenses().stream()
                .map(Expense::getCategory)
                .collect(Collectors.toSet());

        List<String> items = new ArrayList<>();
        items.add("All");
        items.addAll(categories);

        categoryFilterBox.getItems().setAll(items);
    }

    @FXML
    private void deleteExpense(){
        var selected = expenseTable.getSelectionModel().getSelectedItems();

        if(!selected.isEmpty()){
            expenseService.deleteExpenses(selected);
            updateCategoryFilter();
        }
    }

    @FXML
    private void resetFilters(){
        categoryFilterBox.setValue("All");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

        applyFilters();
    }


}
