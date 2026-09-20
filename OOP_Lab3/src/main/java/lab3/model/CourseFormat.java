package lab3.model;

public enum CourseFormat {
    ONLINE("Дистанційне навчання", false),
    OFFLINE("Очне навчання", true),
    HYBRID("Змішане навчання", true);

    private final String description;
    private final boolean needsClassroom;

    CourseFormat(String description, boolean needsClassroom) {
        this.description = description;
        this.needsClassroom = needsClassroom;
    }

    public String getDescription() { return description; }

    public boolean requiresClassroom() { return needsClassroom; }
}