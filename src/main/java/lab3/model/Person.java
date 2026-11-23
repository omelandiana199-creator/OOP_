package lab3.model;

public abstract class Person implements Identifiable<String> {
    private final String id;
    private String name;

    public Person(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Абстрактні методи ( ЛР 4)
    public abstract String getRole();
    public abstract String getDailyTask();
}