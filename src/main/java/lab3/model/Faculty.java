package lab3.model;

public enum Faculty {
    IT("Факультет інформаційних технологій та систем", 200),
    ECONOMICS("Економічний факультет", 150),
    HUMANITARIAN("Гуманітарний факультет", 100);

    private final String description;
    private final int maxCapacity;

    Faculty(String description, int maxCapacity) {
        this.description = description;
        this.maxCapacity = maxCapacity;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Власний метод для перевірки наявності вільних місць
    public boolean hasAvailableSpots(int currentStudentCount) {
        return currentStudentCount < maxCapacity;
    }
}