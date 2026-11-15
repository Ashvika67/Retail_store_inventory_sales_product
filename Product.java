public class Product {
    private final int productId;
    private final String name, category;
    private final double price;
    private final int stock;

    public Product(int productId, String name, String category, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public String toString() {
        return productId + " | " + name + " | " + category + " | Rs." + price + " | Stock: " + stock;
    }
}
