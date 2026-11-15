# TESTING DOCUMENT

Testing methods: unit testing, integration testing (Java-JDBC-PostgreSQL), system testing, database constraint testing and Python analysis testing.

| Test ID | Scenario | Input / Action | Expected Result | Actual Result |
|---|---|---|---|---|
| TC01 | Add valid product | Name, category, price 50, stock 20 | Product saved | Fill after execution |
| TC02 | Empty product name | Blank name | Validation message; no insert | Fill after execution |
| TC03 | Invalid price | Zero or negative | Reject input | Fill after execution |
| TC04 | View products | Select option 2 | Product list displayed | Fill after execution |
| TC05 | Valid sale | Stock 20, sell 3 | Sale saved; stock becomes 17 | Fill after execution |
| TC06 | Insufficient stock | Stock 2, sell 5 | Sale rejected; stock remains 2 | Fill after execution |
| TC07 | Invalid quantity | Quantity 0 | Sale rejected | Fill after execution |
| TC08 | Unknown product | Invalid product ID | Product not found | Fill after execution |
| TC09 | Low stock report | Stock <= reorder level | Product appears | Fill after execution |
| TC10 | Daily report | Existing sales | Date-wise total shown | Fill after execution |
| TC11 | Python analysis | Run analysis script | Charts generated | Fill after execution |
| TC12 | Database unavailable | Stop PostgreSQL | Database error shown | Fill after execution |

Do not mark tests as passed until you have actually executed them.
