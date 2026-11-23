package lab3.exception;
/**
 * Базовий Checked-виняток для бізнес-логіки університету.
 */
public abstract class UniversityException extends Exception {
    public UniversityException(String message) {
        super(message);
    }
}
