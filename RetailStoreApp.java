import java.sql.*;
import java.util.Scanner;

public class RetailStoreApp {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=== RETAIL STORE INVENTORY AND SALES SYSTEM ===");
            System.out.println("1.Add Product  2.View Products  3.Record Sale");
            System.out.println("4.Daily Sales Report  5.Low Stock Report  0.Exit");
            choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1: addProduct(); break;
                    case 2: viewProducts(); break;
                    case 3: recordSale(); break;
                    case 4: dailySalesReport(); break;
                    case 5: lowStockReport(); break;
                    case 0: System.out.println("Thank you!"); break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    private static int readInt(String msg) {
        while (true) {
            System.out.print(msg);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); }
        }
    }

    private static double readPositiveDouble(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                double n = Double.parseDouble(sc.nextLine().trim());
                if (n > 0) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Enter a value greater than zero.");
        }
    }

    private static void addProduct() throws SQLException {
        System.out.print("Product name: ");
        String name = sc.nextLine().trim();
        System.out.print("Category: ");
        String cat = sc.nextLine().trim();
        if (name.isEmpty() || cat.isEmpty()) {
            System.out.println("Name and category cannot be empty."); return;
        }
        double price = readPositiveDouble("Price: ");
        int stock = readInt("Opening stock: ");
        int reorder = readInt("Reorder level: ");
        if (stock < 0 || reorder < 0) {
            System.out.println("Stock and reorder level cannot be negative."); return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int categoryId;
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category(category_name) VALUES (?) ON CONFLICT(category_name) DO UPDATE SET category_name=EXCLUDED.category_name RETURNING category_id")) {
                    ps.setString(1, cat);
                    try (ResultSet rs = ps.executeQuery()) { rs.next(); categoryId = rs.getInt(1); }
                }
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO product(product_name,category_id,price,stock_quantity,reorder_level) VALUES (?,?,?,?,?)")) {
                    ps.setString(1, name); ps.setInt(2, categoryId); ps.setDouble(3, price);
                    ps.setInt(4, stock); ps.setInt(5, reorder); ps.executeUpdate();
                }
                con.commit();
                System.out.println("Product added successfully.");
            } catch (SQLException e) { con.rollback(); throw e; }
        }
    }

    private static void viewProducts() throws SQLException {
        String sql = "SELECT p.product_id,p.product_name,c.category_name,p.price,p.stock_quantity FROM product p JOIN category c ON p.category_id=c.category_id ORDER BY p.product_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("ID | Product | Category | Price | Stock");
            while (rs.next()) {
                Product p = new Product(rs.getInt(1), rs.getString(2), rs.getString(3),
                                        rs.getDouble(4), rs.getInt(5));
                System.out.println(p);
            }
        }
    }

    private static void recordSale() throws SQLException {
        int id = readInt("Product ID: ");
        int qty = readInt("Quantity to sell: ");
        if (qty <= 0) { System.out.println("Quantity must be positive."); return; }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                double price; int stock;
                try (PreparedStatement ps = con.prepareStatement(
                    "SELECT price,stock_quantity FROM product WHERE product_id=? FOR UPDATE")) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { System.out.println("Product not found."); con.rollback(); return; }
                        price = rs.getDouble(1); stock = rs.getInt(2);
                    }
                }
                if (qty > stock) {
                    System.out.println("Insufficient stock. Available: " + stock);
                    con.rollback(); return;
                }
                int saleId;
                try (Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery("INSERT INTO sale DEFAULT VALUES RETURNING sale_id")) {
                    rs.next(); saleId = rs.getInt(1);
                }
                try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sale_item(sale_id,product_id,quantity,unit_price) VALUES (?,?,?,?)")) {
                    ps.setInt(1, saleId); ps.setInt(2, id); ps.setInt(3, qty);
                    ps.setDouble(4, price); ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE product SET stock_quantity=stock_quantity-? WHERE product_id=?")) {
                    ps.setInt(1, qty); ps.setInt(2, id); ps.executeUpdate();
                }
                con.commit();
                System.out.printf("Sale successful. Sale ID=%d, Total=Rs. %.2f%n", saleId, price * qty);
            } catch (SQLException e) { con.rollback(); throw e; }
        }
    }

    private static void dailySalesReport() throws SQLException {
        String sql = "SELECT DATE(s.sale_date),SUM(si.quantity*si.unit_price) FROM sale s JOIN sale_item si ON s.sale_id=si.sale_id GROUP BY DATE(s.sale_date) ORDER BY DATE(s.sale_date) DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Date | Total Sales");
            while (rs.next()) System.out.printf("%s | Rs. %.2f%n", rs.getDate(1), rs.getDouble(2));
        }
    }

    private static void lowStockReport() throws SQLException {
        String sql = "SELECT product_id,product_name,stock_quantity,reorder_level FROM product WHERE stock_quantity<=reorder_level ORDER BY stock_quantity";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Low Stock Products");
            while (rs.next())
                System.out.printf("%d | %s | Available: %d | Reorder at: %d%n",
                    rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
        }
    }
}
