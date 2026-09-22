package lab3.model;
import java.util.Objects;

public class Course implements Identifiable<String> {
    private String courseId;
    private String title;
    private Teacher teacher;
    private CourseFormat format;

    public Course(String courseId, String title, Teacher teacher, CourseFormat format) {
        this.courseId = courseId;
        this.title = title;
        this.teacher = teacher;
        this.format = format;
    }

    @Override
    public String getId() { return courseId; }
    public String getTitle() { return title; }
    public Teacher getTeacher() { return teacher; }
    public CourseFormat getFormat() { return format; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }
    @Override
    public int hashCode() { return Objects.hash(courseId); }

    @Override
    public String toString() {
        return String.format("[%s] %s (Викладач: %s) - Формат: %s",
                courseId, title, teacher.getName(), format.getDescription());
    }
}