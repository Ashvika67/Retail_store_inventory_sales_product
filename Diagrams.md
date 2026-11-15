# SYSTEM DESIGN DIAGRAMS

Paste these Mermaid diagrams into Mermaid Live Editor or a compatible Markdown viewer.

## Use Case Diagram
```mermaid
flowchart LR
 Staff[Store Staff] --> A[Add Product]
 Staff --> B[View Products]
 Staff --> C[Record Sale]
 Staff --> D[Check Stock]
 Owner[Store Owner] --> E[Daily Sales Report]
 Owner --> F[Low Stock Report]
 Owner --> G[Sales Charts]
 C --> H[Update Inventory]
```

## Class Diagram
```mermaid
classDiagram
 class Product {
  -int productId
  -String name
  -String category
  -double price
  -int stock
  +getters()
  +toString()
 }
 class DBConnection {
  +getConnection() Connection
 }
 class RetailStoreApp {
  +main(String[] args)
  -addProduct()
  -viewProducts()
  -recordSale()
  -dailySalesReport()
  -lowStockReport()
 }
 RetailStoreApp --> Product
 RetailStoreApp --> DBConnection
```

## System Architecture
```mermaid
flowchart TD
 U[Staff / Owner] --> J[Java Application]
 J --> B[Business Logic and Stock Validation]
 B --> D[JDBC]
 D --> DB[(PostgreSQL)]
 DB --> P[Python Retrieval]
 P --> PD[Pandas Preprocessing]
 PD --> M[Matplotlib Charts]
```

## ER Diagram
```mermaid
erDiagram
 CATEGORY ||--o{ PRODUCT : contains
 PRODUCT ||--o{ SALE_ITEM : appears_in
 SALE ||--|{ SALE_ITEM : includes
 CATEGORY { int category_id PK
 varchar category_name }
 PRODUCT { int product_id PK
 varchar product_name
 int category_id FK
 numeric price
 int stock_quantity
 int reorder_level }
 SALE { int sale_id PK
 timestamp sale_date }
 SALE_ITEM { int sale_item_id PK
 int sale_id FK
 int product_id FK
 int quantity
 numeric unit_price }
```

## Relational Model
- CATEGORY(**category_id**, category_name)
- PRODUCT(**product_id**, product_name, category_id FK, price, stock_quantity, reorder_level)
- SALE(**sale_id**, sale_date)
- SALE_ITEM(**sale_item_id**, sale_id FK, product_id FK, quantity, unit_price)

PK = Primary Key; FK = Foreign Key.
