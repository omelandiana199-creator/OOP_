package lab3.model;
import java.util.Objects;


public abstract class Person implements Identifiable<String> {
    protected String id;
    protected String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() { return id; } // Реалізація інтерфейсу
    public String getName() { return name; }
    public abstract String getRole();
    public abstract String getDailyTask();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }
    @Override
    public int hashCode() { return Objects.hash(id); }
}