package ua.edu.chdtu.oop.restaurant;

public abstract class MenuItem implements Payable, Prepareable {
    protected String title;
    protected double basePrice;
    protected String status;

    public MenuItem(String title, double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        }
        this.title = title;
        this.basePrice = basePrice;
        this.status = "PENDING";
    }

    public String getTitle() {
        return title;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String getStatus() {
        return status;
    }

    public abstract String getDetails();
}