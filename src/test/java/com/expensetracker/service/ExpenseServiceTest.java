package com.expensetracker.service;

import com.expensetracker.model.Expense;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseServiceTest {
    @Test
    public void addExpenseTest(){
        ExpenseService es = new ExpenseService();
        es.addExpense("Food", 22, LocalDate.of(2026, 5, 4));
        assertEquals("Food", es.getExpenses().getLast().getCategory());
    }

    @Test
    public void deleteExpenseTest(){
        ExpenseService es = new ExpenseService();
        Expense expense = new Expense("Food", 22, LocalDate.of(2025, 4, 5));
        es.addExpense(expense.getCategory(), expense.getAmount(), expense.getDate());
        int prev_size = es.getExpenses().size();
        es.deleteExpenses(List.of(expense));
        assertEquals(prev_size-1, es.getExpenses().size());
    }

    @Test
    public void deleteNonExistingExpenseTest(){
        ExpenseService es = new ExpenseService();
        Expense e = new Expense("Not Food", 34, LocalDate.of(2002, 2, 2));
        Expense e1 = new Expense("Food", 22, LocalDate.now());
        es.addExpense(e.getCategory(), e.getAmount(), e.getDate());
        int prev_size = es.getExpenses().size();
        es.deleteExpenses(List.of(e1));
        assertEquals(prev_size, es.getExpenses().size());
    }



}