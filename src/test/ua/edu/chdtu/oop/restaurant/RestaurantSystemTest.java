package ua.edu.chdtu.oop.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantSystemTest {
    private Order order;
    private Pizza pizza;
    private DrinkItem drink;

    @BeforeEach
    void setUp() {
        order = new Order();
        pizza = new Pizza("Маргарита", 100.0, true, 30);
        drink = new DrinkItem("Лате", 50.0, false);
    }

    @Test
    void testTaxCalculation() {
        // Очікуємо: 100 + 10% = 110.0
        assertEquals(110.0, pizza.calculatePriceWithTax(), 0.01);
        // Очікуємо: 50 + 5% = 52.5
        assertEquals(52.5, drink.calculatePriceWithTax(), 0.01);
    }

    @Test
    void testPreparationStatusFlow() {
        assertEquals("PENDING", pizza.getStatus());

        pizza.startPreparation();
        assertEquals("COOKING_IN_KITCHEN", pizza.getStatus());
        assertFalse(pizza.isReady());

        pizza.setReady();
        assertTrue(pizza.isReady());
    }

    @Test
    void testOrderReceiptGeneration() {
        order.addItem(pizza);
        order.addItem(drink);

        String receipt = order.generateReceipt();

        assertTrue(receipt.contains("Піца: Маргарита"));
        assertTrue(receipt.contains("Напій: Лате"));
        assertTrue(receipt.contains("ЗАГАЛОМ ДО СПЛАТИ: 162,50")); // 110.0 + 52.5
    }

    @Test
    void testValidationNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new DrinkItem("Безкоштовна вода", -10.0, true);
        });
        assertEquals("Ціна не може бути від'ємною", exception.getMessage());
    }
}