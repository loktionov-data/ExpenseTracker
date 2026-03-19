package com.expensetracker.model;

import java.time.LocalDate;

public class Expense {
    private String category;
    private double amount;
    private LocalDate date;

    public Expense(String category, double amount, LocalDate date){
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    public String getCategory(){
        return category;
    }
    public double getAmount(){
        return amount;
    }
    public LocalDate getDate(){
        return date;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        else if (!(o instanceof Expense)){
            return false;
        }

        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 &&
                getCategory().equals(expense.getCategory()) &&
                getDate().equals(expense.getDate());
    }

    @Override
    public int hashCode(){
        return java.util.Objects.hash(getCategory(), getAmount(), getDate());
    }
}
