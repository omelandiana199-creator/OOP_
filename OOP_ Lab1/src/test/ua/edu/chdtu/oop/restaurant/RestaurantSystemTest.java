package ua.edu.chdtu.oop.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantSystemTest {

    private Order order;
    private Pizza pizza;
    private DrinkItem drink;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("Діана", "+380956876358");
        order = new Order(customer);

        pizza = new Pizza("Маргарита", 100.0, true, 30);
        drink = new DrinkItem("Лате", 50.0, false);
    }

    @Test
    void processOrder_shouldGenerateReceiptAndChangeItemStatuses_whenOrderIsValid() {
        order.addItem(pizza);
        order.addItem(drink);

        assertEquals(MenuItem.STATUS_PENDING, pizza.getStatus());

        order.processOrder();

        assertEquals("COOKING_IN_KITCHEN", pizza.getStatus());
        assertEquals("PREPARING_AT_BAR", drink.getStatus());

        Receipt receipt = order.getReceipt();
        assertNotNull(receipt, "Чек має бути згенерований");

        String receiptContent = receipt.print();
        assertTrue(receiptContent.contains("Діана"));
        assertTrue(receiptContent.contains("Маргарита"));
        assertTrue(receiptContent.contains("Лате"));
    }

    @Test
    void getReceipt_shouldThrowException_whenOrderIsNotProcessed() {
        Exception exception = assertThrows(IllegalStateException.class, () -> order.getReceipt());
        assertEquals("Замовлення ще не оброблено, чек відсутній!", exception.getMessage());
    }

    @Test
    void processOrder_shouldThrowException_whenOrderIsEmpty() {
        Exception exception = assertThrows(IllegalStateException.class, () -> order.processOrder());
        assertEquals("Неможливо обробити порожнє замовлення", exception.getMessage());
    }

    @Test
    void processOrder_shouldThrowException_whenProcessedTwice() {
        order.addItem(pizza);
        order.processOrder();

        Exception exception = assertThrows(IllegalStateException.class, () -> order.processOrder());
        assertEquals("Замовлення вже оброблено", exception.getMessage());
    }

    @Test
    void addItem_shouldThrowException_whenItemIsNull() {
        // Перевірка захисту від null
        assertThrows(NullPointerException.class, () -> order.addItem(null));
    }
}