package lab3.model;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Course implements Identifiable<String>, java.io.Serializable {
    private String courseId;
    private String title;
    private Teacher teacher;
    private CourseFormat format;
    // Queue за прямим призначенням: черга FIFO для студентів
    private Queue<String> waitingList;

    public Course(String courseId, String title, Teacher teacher, CourseFormat format) {
        this.courseId = courseId;
        this.title = title;
        this.teacher = teacher;
        this.format = format;
        this.waitingList = new LinkedList<>();
    }

    @Override
    public String getId() { return courseId; }
    public String getTitle() { return title; }
    public Teacher getTeacher() { return teacher; }
    public CourseFormat getFormat() { return format; }
    public Queue<String> getWaitingList() { return waitingList; }

    public void addToWaitlist(String studentId) { waitingList.offer(studentId); }

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