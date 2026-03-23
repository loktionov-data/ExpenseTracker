package com.expensetracker.services;

import com.expensetracker.model.Expense;

import java.time.LocalDate;
import java.util.function.Predicate;

// Service responsible for building filter predicates for expenses
public class FilterService {
    public Predicate<Expense> createPredicate(
                            String category,
                            LocalDate fromDate,
                            LocalDate toDate) {
        return expense -> {
            if (expense == null) {
                return false;
            }

            //Checking if expense matches the category and the category is not null
            boolean categoryMatch = (category == null || category.equals("All")
                    || expense.getCategory().equalsIgnoreCase(category));

            //Checking if expense matches and the dates are not null and have correct form
            boolean fromMatch = (fromDate == null ||
                    (expense.getDate() != null && !expense.getDate().isBefore(fromDate)));

            boolean toMatch = (toDate == null ||
                    (expense.getDate() != null && !expense.getDate().isAfter(toDate)));

            return categoryMatch && fromMatch && toMatch;
            };
        }
    }

