package ua.edu.chdtu.oop.restaurant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Receipt {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int receiptId;
    private final LocalDateTime generatedAt;
    private final String orderDetails;

    Receipt(int receiptId, String orderDetails) {
        if (receiptId <= 0) {
            throw new IllegalArgumentException("ID чека має бути додатнім");
        }
        this.receiptId = receiptId;
        this.generatedAt = LocalDateTime.now();
        this.orderDetails = Objects.requireNonNull(orderDetails, "Деталі замовлення не можуть бути null");
    }

    public int getReceiptId() {
        return receiptId;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public String print() {
        return String.format("\n=== ФІСКАЛЬНИЙ ЧЕК #%d ===\nЧас: %s\n%s\n=======================",
                receiptId, generatedAt.format(DATE_FORMATTER), orderDetails);
    }
}