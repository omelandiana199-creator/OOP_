package ua.edu.chdtu.oop.restaurant;

public class Pizza extends FoodItem {

    private final int sizeCm;

    public Pizza(String title, double basePrice, boolean isVegetarian, int sizeCm) {
        super(title, basePrice, isVegetarian);

        // 2. Додаємо базову валідацію розміру
        if (sizeCm <= 0) {
            throw new IllegalArgumentException("Розмір піци має бути більшим за нуль");
        }

        this.sizeCm = sizeCm;
    }

    public int getSizeCm() {
        return sizeCm;
    }

    @Override
    public String getDetails() {
        return String.format("Піца: %s (%d см) %s",
                getTitle(),
                sizeCm,
                isVegetarian() ? "[Вег]" : "[М'ясна]");
    }
}