package ua.edu.chdtu.oop.restaurant;

public abstract class MenuItem implements Payable, Prepareable {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_READY = "READY";

    private final String title;
    private final double basePrice;

    private String status;

    public MenuItem(String title, double basePrice) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва не може бути порожньою");
        }
        if (basePrice < 0) {
            throw new IllegalArgumentException("Ціна не може бути від'ємною");
        }

        this.title = title.trim();
        this.basePrice = basePrice;
        this.status = STATUS_PENDING;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean isReady() {
        return STATUS_READY.equals(this.status);
    }

    public void setReady() {
        this.status = STATUS_READY;
    }

    public abstract String getDetails();
}