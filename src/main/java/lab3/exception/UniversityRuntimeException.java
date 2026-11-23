package lab3.exception;

public abstract class UniversityRuntimeException extends RuntimeException {
    public UniversityRuntimeException(String message) {
        super(message);
    }
}