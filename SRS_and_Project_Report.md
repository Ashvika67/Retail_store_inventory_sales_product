# PROJECT REPORT: Retail Store Inventory and Sales Analysis System

## 1. Introduction
Retail shops maintain product details, stock quantities and sales records. Manual record keeping can cause calculation mistakes, stock mismatches and delays in identifying popular products. This project computerizes product management, sales recording, inventory updates and sales analysis.

## 2. Problem Statement
A retail store manages product information, stock availability, sales transactions and customer purchases. Manual maintenance may cause stock discrepancies, difficulty tracking sales and delays in identifying fast-selling and low-stock items. The proposed system uses Java, DBMS and Python.

## 3. Objectives
- Maintain product ID, name, category, price and available stock.
- Record sales with product, quantity and date.
- Automatically reduce stock after a successful sale.
- Validate stock and prevent excess sales.
- Generate daily sales and low-stock reports.
- Analyze data and create visualizations using Python.

## 4. Scope
The system is intended for a small or medium retail store. It supports product management, sales recording, stock validation, reports and data visualization. Online payment, barcode scanning and multi-branch synchronization are outside the current scope.

## 5. Stakeholders
| Stakeholder | Responsibility |
|---|---|
| Store owner | Monitors sales and inventory |
| Store staff | Adds products and records sales |
| Inventory manager | Checks stock and restocking needs |
| Customer | Purchases available products |
| Administrator | Maintains application and database |

## 6. Existing System and Limitations
The manual method uses notebooks or spreadsheets. It may cause duplicate entries, arithmetic errors, delayed stock updates and difficulty preparing reports.

## 7. Proposed System
A Java menu-driven application connects to PostgreSQL. Product and sales data are stored in relational tables. During a sale, stock is checked and the sale record and stock reduction are committed together in one database transaction. Python retrieves records, preprocesses them with Pandas and generates charts with Matplotlib.

## 8. Functional Requirements
1. Add and display products.
2. Record sales with product, quantity and date.
3. Check stock before processing a sale.
4. Reject sales when stock is insufficient.
5. Update stock automatically after successful sales.
6. Display daily sales totals.
7. Display products at or below reorder level.
8. Retrieve sales and inventory data using Python.
9. Generate sales and inventory charts.

## 9. Non-Functional Requirements
- Usability: simple menu and clear messages.
- Reliability: failed sales must not partially update records.
- Performance: reports should run efficiently for a small store.
- Security: credentials must not be uploaded to GitHub.
- Maintainability: application, database and analysis are separated.
- Integrity: primary keys, foreign keys and CHECK constraints are used.

## 10. Hardware and Software
Computer with 4 GB RAM or more; Windows/Linux; JDK 17+; PostgreSQL 14+; Python 3.10+; VS Code/Eclipse; pgAdmin optional.

## 11. Modules
Product Management, Sales Management, Inventory Management, Reporting and Python Data Analysis.

## 12. Inputs and Outputs
Inputs: product name, category, price, stock, product ID and quantity sold.
Outputs: product list, sale confirmation, insufficient-stock message, daily sales report, low-stock report and charts.

## 13. Business Rules
- Price must be greater than zero.
- Stock cannot be negative.
- Sale quantity must be positive.
- Sale is allowed only when sufficient stock exists.
- Sale insertion and stock reduction must succeed or fail together.
- Low stock means quantity <= reorder level.

## 14. Conclusion
The system reduces manual work, improves stock accuracy and helps the store understand sales performance. Java implements business logic, PostgreSQL stores records and Python visualizes useful patterns.
