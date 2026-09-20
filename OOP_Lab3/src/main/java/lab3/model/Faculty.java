package lab3.model;

public enum Faculty {
    IT("Інформаційних технологій і систем"),
    ECONOMICS("Економіка та управління"),
    HUMANITARIAN("Гуманітарних технологій");

    private final String name;
    Faculty(String name) { this.name = name; }
    public String getName() { return name; }
}