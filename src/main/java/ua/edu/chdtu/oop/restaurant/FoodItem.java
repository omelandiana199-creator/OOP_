package ua.edu.chdtu.oop.restaurant;

public class FoodItem extends MenuItem {
    protected boolean isVegetarian;

    public FoodItem(String title, double basePrice, boolean isVegetarian) {
        super(title, basePrice);
        this.isVegetarian = isVegetarian;
    }

    @Override
    public double calculatePriceWithTax() {
        return basePrice * 1.10; // 10% податок
    }

    @Override
    public void startPreparation() {
        this.status = "COOKING_IN_KITCHEN";
    }

    @Override
    public boolean isReady() {
        return "READY".equals(this.status);
    }

    public void setReady() {
        this.status = "READY";
    }

    @Override
    public String getDetails() {
        return String.format("Страва: %s %s", title, isVegetarian ? "(Вег)" : "");
    }
}

