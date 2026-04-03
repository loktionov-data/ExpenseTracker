# Expense Tracker (JavaFX)

Expense Tracker is a desktop application built with JavaFX for managing personal expenses.  
The application allows users to add, delete, filter, and analyze their expenses through a simple and responsive user interface.

The project was designed with a focus on clean architecture, separation of concerns, and testability of business logic.

---

## Overview

The application provides a structured way to track spending by storing expenses with three main attributes:
- category
- amount
- date

All data is persisted locally using JSON, allowing the application to retain state between runs.

The UI is built using JavaFX and is fully synchronized with the underlying data model via observable collections.

---

## Features

### Expense Management
- Add new expenses with category, amount, and date
- Delete one or multiple selected expenses
- Automatic update of total expenses

### Filtering
- Filter expenses by category
- Filter by date range (from / to)
- Combine multiple filters simultaneously

### Sorting
- Sort expenses by date directly in the table

### Data Persistence
- All expenses are saved to a JSON file
- Data is automatically loaded on application startup

---

## Architecture

The application follows a layered architecture to separate responsibilities:

Controller → Service → Storage

### Controller Layer
Responsible for handling user input and updating the UI.  
It does not contain business logic, but delegates operations to services.

### Service Layer
Contains the core business logic of the application:

- **ExpenseService**
  - manages the list of expenses
  - handles add/delete operations
  - calculates total sum
  - communicates with storage

- **FilterService**
  - builds filtering logic using predicates
  - allows flexible combination of filters

### Storage Layer
Handles persistence:

- **ExpenseStorage**
  - loads data from JSON
  - saves updated data back to file

---

## Data Flow

The application relies on JavaFX observable collections to keep the UI in sync with data.

General flow:
User Action -> Controller -> Service -> ObservableList -> UI update

Filtering flow:
UI Filters -> FilterService -> Predicate -> FilteredList -> TableView

Sorting is handled via:
FilteredList -> SortedList -> TableView

---

## Key Technical Concepts

- **ObservableList**  
  Automatically updates the UI when data changes.

- **FilteredList**  
  Applies dynamic filtering without modifying the original data.

- **SortedList**  
  Enables sorting that integrates with TableView.

- **Predicate**  
  Used to encapsulate flexible filtering logic.

---

## Testing

The project is structured to allow unit testing of business logic independently from the UI.

### Tested Components

- **ExpenseService**
  - adding expenses
  - deleting expenses
  - total sum calculation

- **FilterService**
  - category filtering
  - date range filtering
  - combined filter scenarios

---

## Future Improvements

Planned features and improvements:

- Data visualization (charts by category and time)
- Editing existing expenses
- Search functionality
- Improved user interface
