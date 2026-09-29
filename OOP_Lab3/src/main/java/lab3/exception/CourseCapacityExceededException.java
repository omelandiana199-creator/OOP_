package lab3.exception;

public class CourseCapacityExceededException extends UniversityException {
    private final String studentId;

    public CourseCapacityExceededException(String message, String studentId) {
        super(message);
        this.studentId = studentId;
    }
    public String getStudentId() { return studentId; }
}