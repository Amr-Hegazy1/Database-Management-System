# Database Management System
## Project Overview

This project implements a simplified database engine with support for B+ tree indexing as part of the CSEN604: Database II course at the German University in Cairo. The engine is developed in Java and supports basic database operations including table creation, data insertion, updating, deletion, and querying.

## Features

- Table creation with support for multiple data types (Integer, String, Double)
- Data persistence using serialized page files
- B+ tree indexing for efficient querying
- Basic CRUD operations (Create, Read, Update, Delete)
- Query execution with support for multiple conditions and logical operators
- SQL Parsing for all implemented operations

## Key Components

1. **Page Management**: Data storage in serialized page files with a configurable maximum number of rows per page.
2. **Metadata Handling**: Storage and retrieval of table schemas and index information.
3. **Indexing**: B+ tree index creation and maintenance for improved query performance.
4. **Query Processing**: Support for basic SQL-like queries with multiple conditions.

## Main Classes

- `DBApp`: The main class containing core database operations.
- `Page`: Represents a single page of data in the database.
- `Table`: Manages metadata and pages for a single table.
- `SQLTerm`: Represents a single condition in a query.

## Usage

Here's a basic example of how to use the database engine:

```java
DBApp dbApp = new DBApp();

// Create a table
Hashtable<String, String> htblColNameType = new Hashtable<>();
htblColNameType.put("id", "java.lang.Integer");
htblColNameType.put("name", "java.lang.String");
htblColNameType.put("gpa", "java.lang.Double");
dbApp.createTable("Student", "id", htblColNameType);

// Create an index
dbApp.createIndex("Student", "gpa", "gpaIndex");

// Insert data
Hashtable<String, Object> htblColNameValue = new Hashtable<>();
htblColNameValue.put("id", 1);
htblColNameValue.put("name", "John Doe");
htblColNameValue.put("gpa", 3.5);
dbApp.insertIntoTable("Student", htblColNameValue);

// Query data
SQLTerm[] arrSQLTerms = new SQLTerm[1];
arrSQLTerms[0] = new SQLTerm("Student", "gpa", ">", 3.0);
String[] strarrOperators = new String[0];
Iterator resultSet = dbApp.selectFromTable(arrSQLTerms, strarrOperators);
```

## Team Members:
<a href="https://github.com/Amr-Hegazy1/Database-Management-System/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Amr-Hegazy1/Database-Management-System" />
</a>
