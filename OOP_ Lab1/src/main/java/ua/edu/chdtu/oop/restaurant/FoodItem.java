package ua.edu.chdtu.oop.restaurant;

public class FoodItem extends MenuItem {

    private static final double TAX_RATE = 1.10;
    private static final String STATUS_COOKING = "COOKING_IN_KITCHEN";

    private final boolean isVegetarian;

    public FoodItem(String title, double basePrice, boolean isVegetarian) {
        super(title, basePrice);
        this.isVegetarian = isVegetarian;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    @Override
    public double calculatePriceWithTax() {
        return getBasePrice() * TAX_RATE;
    }

    @Override
    public void startPreparation() {
        // Використовуємо setStatus() замість this.status = ...
        setStatus(STATUS_COOKING);
    }

    @Override
    public String getDetails() {
        String vegMarker = isVegetarian ? " (Вег)" : "";
        return String.format("Страва: %s%s", getTitle(), vegMarker);
    }
}