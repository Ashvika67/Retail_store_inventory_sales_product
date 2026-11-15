# Retail Store Inventory and Sales Analysis System

Java + PostgreSQL + Python project for product management, sales recording, stock updates and data visualization.

## Technologies
Java 17+, PostgreSQL 14+, JDBC, Python 3.10+, Pandas, Matplotlib, psycopg2.

## Folder structure
- `docs/` – SRS, diagrams and testing
- `database/retail_store.sql` – tables, sample records and SQL reports
- `java/src/` – Java application
- `python/` – database retrieval and charts

## Database setup
Create a database named `retail_store`, then execute `database/retail_store.sql` using pgAdmin or psql.

## Java setup
Add the PostgreSQL JDBC driver JAR to the classpath. Update URL, username and password in `DBConnection.java`. Compile the Java files and run `RetailStoreApp`. In Windows use `;` in the classpath; in Linux/macOS use `:`.

## Python setup
Run `pip install -r python/requirements.txt`. Set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD` environment variables, then run `python python/sales_analysis.py`. Charts are saved in `visualizations/`.

## GitHub
Create a repository named `retail-store-inventory-sales-system` and upload this folder. Add your name, register number, class and guide details to the report. Never upload real passwords.
