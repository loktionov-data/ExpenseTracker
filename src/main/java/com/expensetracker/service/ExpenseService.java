package com.expensetracker.service;

import com.expensestorage.ExpenseStorage;
import com.expensetracker.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseService {
    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

    public ExpenseService(){
        expenses.addAll(ExpenseStorage.loadExpenses());
    }

    public ObservableList<Expense> getExpenses(){
        return expenses;
    }

    public void addExpense(String category, double amount, LocalDate date){
        Expense expense = new Expense(category, amount, date);
        expenses.add(expense);
        ExpenseStorage.saveExpenses(expenses);
    }

    public void deleteExpenses(List<Expense> expensesToDelete) {
        expenses.removeAll(expensesToDelete);
        ExpenseStorage.saveExpenses(expenses);
    }

    public double getTotalSum(){
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    public List<String> getCategories(){
        return expenses.stream().map(Expense::getCategory).distinct().collect(Collectors.toList());
    }


}
