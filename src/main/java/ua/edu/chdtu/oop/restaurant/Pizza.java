package ua.edu.chdtu.oop.restaurant;

public class Pizza extends FoodItem {
    private int sizeCm;

    public Pizza(String title, double basePrice, boolean isVegetarian, int sizeCm) {
        super(title, basePrice, isVegetarian);
        this.sizeCm = sizeCm;
    }

    @Override
    public String getDetails() {
        return String.format("Піца: %s (%d см) %s", title, sizeCm, isVegetarian ? "[Вег]" : "[М'ясна]");
    }
}