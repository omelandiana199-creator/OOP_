package lab3.model;

import java.util.Objects;

public class Teacher extends Person {
    private String department;

    public Teacher(String id, String name, String department) {
        super(id, name);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String getRole() {
        return "Викладач";
    }

    @Override
    public String getDailyTask() {
        return "Проводити пари: " + department;
    }

    // Обґрунтування: Ідентифікація викладача базується виключно на його унікальному ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(getId(), teacher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Викладач [ID: %s, ПІБ: %s, Кафедра: %s]", getId(), getName(), department);
    }
}