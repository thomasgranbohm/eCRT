package tk.yommail.ecrt.Misc;

public class Item {

    private int id;
    private String title;
    private double price;
    private int amount;

    public Item(int id, String title, double price, int amount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
