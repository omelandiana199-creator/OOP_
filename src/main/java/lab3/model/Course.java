package lab3.model;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class Course implements Identifiable<String> {
    private final String courseId;
    private final String title;
    private final Teacher teacher;
    private final CourseFormat format;

    // Обгрунтування (ЛР 8): Використовуємо Queue (LinkedList), оскільки список очікування
    // працює за принципом FIFO (First In, First Out) - хто перший став у чергу, той перший отримає місце.
    private final Queue<String> waitingList;

    private Course(Builder builder) {
        this.courseId = builder.courseId;
        this.title = builder.title;
        this.teacher = builder.teacher;
        this.format = builder.format;
        this.waitingList = new LinkedList<>(); // Queue
    }

    @Override
    public String getId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public CourseFormat getFormat() {
        return format;
    }

    public Queue<String> getWaitingList() {
        return waitingList;
    }

    public void addToWaitlist(String studentId) {
        waitingList.offer(studentId); // Додавання в кінець черги
    }

    // ЛР 4: equals, hashCode, toString
    // Обґрунтування: Курс є унікальним виключно за його ідентифікатором (courseId).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return String.format("Курс [ID: %s, Назва: %s, Формат: %s]", courseId, title, format);
    }

    // Внутрішній клас Builder
    public static class Builder {
        private String courseId;
        private String title;
        private Teacher teacher;
        private CourseFormat format;

        public Builder courseId(String courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder format(CourseFormat format) {
            this.format = format;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
}