package ua.edu.chdtu.oop.restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас замовлення, який агрегує позиції та формує текстовий чек без виведення в консоль.
 */
public class Order {
    private final int orderId;
    private final List<MenuItem> items;
    private static int idCounter = 1;

    public Order() {
        this.orderId = idCounter++;
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items); // Повертаємо копію для захисту даних
    }

    public void processOrder() {
        items.forEach(Prepareable::startPreparation);
    }

    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append(String.format("--- Чек замовлення #%d ---\n", orderId));

        double total = 0;
        for (MenuItem item : items) {
            double itemTotal = item.calculatePriceWithTax();
            receipt.append(String.format("%s | Ціна з ПДВ: %.2f грн\n", item.getDetails(), itemTotal));
            total += itemTotal;
        }

        receipt.append(String.format("ЗАГАЛОМ ДО СПЛАТИ: %.2f грн\n", total));
        return receipt.toString();
    }
}