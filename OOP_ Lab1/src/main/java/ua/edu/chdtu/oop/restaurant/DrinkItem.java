package ua.edu.chdtu.oop.restaurant;

public class DrinkItem extends MenuItem {

    private static final double TAX_RATE = 1.05;
    private static final String STATUS_PREPARING = "PREPARING_AT_BAR";

    private final boolean isCold;

    public DrinkItem(String title, double basePrice, boolean isCold) {
        super(title, basePrice);
        this.isCold = isCold;
    }

    public boolean isCold() {
        return isCold;
    }

    @Override
    public double calculatePriceWithTax() {
        return getBasePrice() * TAX_RATE;
    }

    @Override
    public void startPreparation() {
        setStatus(STATUS_PREPARING);
    }

    @Override
    public String getDetails() {
        return String.format("Напій: %s %s", getTitle(), isCold ? "(Холодний)" : "(Гарячий)");
    }
}