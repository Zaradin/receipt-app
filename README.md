# Receipt Tracker Application
[![Development](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=IntelliJ-IDEA&logoColor=white)](https://www.jetbrains.com/idea/)
[![Language](https://img.shields.io/badge/Kotlin-7F52FF.svg?style=for-the-badge&logo=Kotlin&logoColor=white)](https://kotlinlang.org/)
![GitHub Actions](https://github.com/Zaradin/receipt-app/actions/workflows/build-test-report.yml/badge.svg)

This application was built using Intellij and the Kotlin programming language.

## Description
This project is based on a university assignment.
We were tasked with building a console CRUD system in kotlin by using the GitHub workflow methodology.

This application allows users to create receipts and then add products to these receipts. 
The user can then choose different analysis features to see how much the user is spending and give different breakdowns.



### GitHub actions were also used for this project to generate:
* Code Coverage Report (jacoco)
* Jar file executable
* kDoc Documentation Site
* Test report file
## Getting Started

### JUnit tests
Unit tests were included for all features of the application excluding the main function.

### Dependencies

* kotlin-logging (2.1.23)
* slf4j-simple (1.7.36)
* JUnit 5 (5.8.1)
* dokka (1.6.10)
* jacoco

### Installing

* Clone repo from Intellij
* Build within Intellij

### Executing program

* After the program has been built successfully
* Click the run button within intellij

## Main menu for the application.
```
   > ----------------------------------
   > |         RECEIPT TRACKER        |
   > ----------------------------------
   > | RECEIPT MENU                   |
   > |   1) Add a receipt             |
   > |   2) List receipts             |
   > |   3) Update a receipt          |
   > |   4) Delete a receipt          |
   > |   5) Search Receipts           |
   > ----------------------------------
   > | PRODUCT MENU                   |
   > |   6) Add Product to Receipt    |
   > |   7) List Products in Receipt  |
   > |   8) Delete Product in Receipt |
   > |   9) Number of Products        |
   > |   10) Update Product Info      |
   > ----------------------------------
   > | SPENDING ANALYSIS MENU         |
   > |   11) Open Menu                |
   > ----------------------------------
   > |   20) Save Receipts            |
   > |   21) Load Receipts            |
   > ----------------------------------
   > |   0) Exit                      |
   > ----------------------------------
```

## Sub-Menu for the Spending Analysis Menu
```
   > ----------------------------------
   > |     SPENDING ANALYSIS MENU     |
   > ----------------------------------
   > | SPENDING SUB-MENU              |
   > |   1) Total Spending            |
   > |   2) Average Receipt Spend     |
   > |   3) Top 5 categories of spend |
   > |   4) Payment Type Breakdown    |
   > ----------------------------------
   > |   0) Exit                      |
   > ----------------------------------
```

Any advice for common problems or issues.
```
command to run if program contains helper info
```

## Authors

Contributors names and contact info

Josh Crotty
[@Zaradin](https://github.com/Zaradin)

## Version History
* 3.0
  * Sub-Menu for spending analysis
    * Spending analysis features
  * Data Persistence with JSON & XML
  * JUnit tests for all implemented features
  * Code Coverage and kDoc
  * GitHub Actions for generating jar executable, kDoc and code coverage reports
* 2.0
  * New Model for adding products to receipts
    * Functions within the receipt class for controlling products in MutableSet
  * CRUD capabilities for receipts
  * JUnit tests for all implemented features
* 1.0
    * Menu items for Adding, Listing, Updating and Deleting a Receipt. No Receipt model is added yet; the menu structure is a skeleton.
    * Logging capabilities are added via MicroUtils Kotlin-Logging.
    * ScannerInput utility is included for rebust user I/O.
