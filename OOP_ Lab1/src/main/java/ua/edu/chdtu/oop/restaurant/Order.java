package ua.edu.chdtu.oop.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {

    // 1. Потокобезпечна генерація ID (Thread Safety)
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final int orderId;

    // АГРЕГАЦІЯ: Замовлення агрегує страви (MenuItem)
    private final List<MenuItem> items;

    // АСОЦІАЦІЯ: Замовлення пов'язане з клієнтом (Customer)
    private final Customer customer;

    // КОМПОЗИЦІЯ: Замовлення контролює чек (Receipt)
    private Receipt receipt;

    public Order(Customer customer) {
        // 2. Валідація: замовлення не може існувати без клієнта
        this.customer = Objects.requireNonNull(customer, "Клієнт не може бути null");
        this.orderId = idCounter.getAndIncrement();
        this.items = new ArrayList<>();
    }

    public void addItem(MenuItem item) {
        // 3. Захист від порожніх елементів
        Objects.requireNonNull(item, "Неможливо додати порожню страву (null)");
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void processOrder() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Неможливо обробити порожнє замовлення");
        }
        if (receipt != null) {
            throw new IllegalStateException("Замовлення вже оброблено");
        }

        items.forEach(Prepareable::startPreparation);
        generateReceipt();
    }

    private void generateReceipt() {
        StringBuilder content = new StringBuilder();
        content.append("Клієнт: ").append(customer.getName()).append("\n------------------\n");

        double total = 0;
        for (MenuItem item : items) {
            double itemTotal = item.calculatePriceWithTax();
            content.append(String.format("%s | %.2f грн\n", item.getDetails(), itemTotal));
            total += itemTotal;
        }
        content.append("------------------\n");
        content.append(String.format("ДО СПЛАТИ: %.2f грн", total));

        this.receipt = new Receipt(this.orderId, content.toString());
    }

    public Receipt getReceipt() {
        if (receipt == null) {
            throw new IllegalStateException("Замовлення ще не оброблено, чек відсутній!");
        }
        return receipt;
    }
}