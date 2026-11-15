import os
from pathlib import Path
import pandas as pd
import matplotlib.pyplot as plt
import psycopg2

OUT = Path(__file__).resolve().parent.parent / "visualizations"
OUT.mkdir(exist_ok=True)

def main():
    conn = psycopg2.connect(
        host=os.getenv("DB_HOST", "localhost"),
        port=os.getenv("DB_PORT", "5432"),
        dbname=os.getenv("DB_NAME", "retail_store"),
        user=os.getenv("DB_USER", "postgres"),
        password=os.getenv("DB_PASSWORD", "your_password")
    )
    try:
        sales = pd.read_sql_query(
            "SELECT s.sale_date,p.product_name,si.quantity,si.unit_price,"
            "(si.quantity*si.unit_price) AS revenue "
            "FROM sale s JOIN sale_item si ON s.sale_id=si.sale_id "
            "JOIN product p ON p.product_id=si.product_id ORDER BY s.sale_date", conn)
        stock = pd.read_sql_query(
            "SELECT product_name,stock_quantity,reorder_level FROM product ORDER BY product_name", conn)
    finally:
        conn.close()

    if not sales.empty:
        sales["sale_date"] = pd.to_datetime(sales["sale_date"], errors="coerce")
        sales["quantity"] = pd.to_numeric(sales["quantity"], errors="coerce")
        sales["revenue"] = pd.to_numeric(sales["revenue"], errors="coerce")
        sales = sales.dropna(subset=["sale_date", "quantity", "revenue"])
        daily = sales.groupby(sales["sale_date"].dt.date)["revenue"].sum()
        daily.plot(kind="bar", title="Daily Sales Revenue", xlabel="Date", ylabel="Revenue (Rs.)")
        plt.tight_layout(); plt.savefig(OUT / "daily_sales.png"); plt.close()

        best = sales.groupby("product_name")["quantity"].sum().sort_values(ascending=False)
        best.plot(kind="bar", title="Quantity Sold by Product", xlabel="Product", ylabel="Quantity")
        plt.tight_layout(); plt.savefig(OUT / "product_sales.png"); plt.close()
        print("Total revenue: Rs.", round(sales["revenue"].sum(), 2))
    else:
        print("No sales data available yet. Record a sale using the Java application.")

    if not stock.empty:
        stock.plot(x="product_name", y="stock_quantity", kind="bar", legend=False,
                   title="Current Stock by Product", xlabel="Product", ylabel="Stock")
        plt.tight_layout(); plt.savefig(OUT / "current_stock.png"); plt.close()
        low = stock[stock["stock_quantity"] <= stock["reorder_level"]]
        print("\nProducts requiring restock:")
        print(low.to_string(index=False) if not low.empty else "None")
    print("Charts saved to:", OUT)

if __name__ == "__main__":
    main()
