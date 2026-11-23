package lab3.exception;

public class EntityNotFoundException extends UniversityRuntimeException {
    private final String entityId;

    public EntityNotFoundException(String message, String entityId) {
        super(message);
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }
}