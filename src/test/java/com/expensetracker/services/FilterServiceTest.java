package com.expensetracker.services;

import com.expensetracker.model.Expense;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class FilterServiceTest {

    @Test
    public void categoryPredicateTest(){
        FilterService fs = new FilterService();
        Expense e1 = new Expense("Food", 33, LocalDate.of(2002, 12, 1));
        Predicate<Expense> expensePredicate = fs.createPredicate("Entertainment", null, null);
        assertFalse(expensePredicate.test(e1));
    }

    @Test
    public void fromDatePredicateTest(){
        FilterService fs = new FilterService();
        LocalDate dateToTest = LocalDate.of(2002, 10, 2);
        Expense e1 = new Expense(null, 0, dateToTest);
        Predicate<Expense> ex = fs.createPredicate(null, dateToTest, null);
        assertTrue(ex.test(e1));
    }

    @Test
    public void toDatePredicateTest(){ //Must fail
        FilterService fs = new FilterService();
        LocalDate dateToTest = LocalDate.of(2020, 10, 2);
        Expense e1 = new Expense("Entertainment", 22, dateToTest);
        Predicate<Expense> ex = fs.createPredicate(null, LocalDate.of(2021, 12, 3), dateToTest);
        assertTrue(ex.test(e1));
    }

    @Test
    public void toDatePredicateTest1(){
        FilterService fs = new FilterService();
        LocalDate dateToTest = LocalDate.of(2020, 10, 2);
        Expense e1 = new Expense("Food", 22, dateToTest);
        Predicate<Expense> ex = fs.createPredicate(null,  LocalDate.of(2019, 12, 3), dateToTest);
        assertTrue(ex.test(e1));
    }

    @Test
    public void mixedParametersPredicateTest(){
        FilterService fs = new FilterService();
        String categoryToTest = "Food";
        LocalDate datetoTest = LocalDate.of(2002, 1, 1);
        Expense e1 = new Expense(categoryToTest, 22, datetoTest);
        Predicate<Expense> ex = fs.createPredicate(categoryToTest, LocalDate.of(2001,2,1 ), datetoTest);
        assertTrue(ex.test(e1));
    }

}