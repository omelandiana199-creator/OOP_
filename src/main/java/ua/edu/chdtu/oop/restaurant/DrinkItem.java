package ua.edu.chdtu.oop.restaurant;

public class DrinkItem extends MenuItem {
    private boolean isCold;

    public DrinkItem(String title, double basePrice, boolean isCold) {
        super(title, basePrice);
        this.isCold = isCold;
    }

    @Override
    public double calculatePriceWithTax() {
        return basePrice * 1.05; // 5% податок
    }

    @Override
    public void startPreparation() {
        this.status = "PREPARING_AT_BAR";
    }

    @Override
    public boolean isReady() {
        return "READY".equals(this.status);
    }

    @Override
    public String getDetails() {
        return String.format("Напій: %s %s", title, isCold ? "(Холодний)" : "(Гарячий)");
    }
}
