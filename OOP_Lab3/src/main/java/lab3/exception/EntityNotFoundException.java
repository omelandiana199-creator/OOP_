package lab3.exception;
// Unchecked виняток для помилок логіки (не знайдено в базі)
public class EntityNotFoundException extends RuntimeException {
    private final String entityId;

    public EntityNotFoundException(String message, String entityId) {
        super(message);
        this.entityId = entityId;
    }
    public String getEntityId() { return entityId; }
}