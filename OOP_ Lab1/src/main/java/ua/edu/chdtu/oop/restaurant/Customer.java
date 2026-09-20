package ua.edu.chdtu.oop.restaurant;

import java.util.Objects;

public class Customer {
    private final String name;
    private final String phone;

    public Customer(String name, String phone) {
        // 1. Захист від порожніх даних (валідація)
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я клієнта не може бути порожнім");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Телефон клієнта не може бути порожнім");
        }

        // Зберігаємо дані, прибираючи випадкові пробіли по краях (trim)
        this.name = name.trim();
        this.phone = phone.trim();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', phone='" + phone + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) && Objects.equals(phone, customer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }
}