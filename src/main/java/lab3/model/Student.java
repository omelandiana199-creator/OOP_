package lab3.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Student extends Person implements Comparable<Student>, Cloneable {
    private Faculty faculty;
    private Map<String, Integer> grades;

    public Student(String id, String name, Faculty faculty) {
        super(id, name);
        this.faculty = faculty;
        this.grades = new HashMap<>();
    }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public Map<String, Integer> getGrades() { return grades; }

    public void enrollCourse(String courseId) {
        grades.putIfAbsent(courseId, 0);
    }

    public void setGrade(String courseId, int grade) {
        if (grades.containsKey(courseId)) {
            grades.put(courseId, grade);
        }
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        return grades.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    // Реалізація абстрактних методів
    @Override
    public String getRole() {
        return "Студент";
    }

    @Override
    public String getDailyTask() {
        return "Відвідувати лекції та складати лабораторні роботи";
    }

    @Override
    public int compareTo(Student o) {
        return this.getName().compareToIgnoreCase(o.getName());
    }

    // ЛР 4: equals, hashCode, toString
    // Обґрунтування: Два студенти вважаються ідентичними виключно за унікальним ID.
    // Ім'я чи факультет можуть змінюватися (зміна прізвища або переведення),
    // тому ці поля не беруть участі у порівнянні.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Студент [ID: %s, ПІБ: %s, Факультет: %s, Сер.бал: %.2f]",
                getId(), getName(), faculty.getDescription(), getAverageGrade());
    }

    // ЛР 4: Копіювання
    // Поверхневе копіювання: колекція grades передається за посиланням.
    public Student shallowCopy() {
        try {
            return (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Помилка копіювання", e);
        }
    }

    // Глибоке копіювання: створюється повністю незалежний об'єкт колекції grades.
    public Student deepCopy() {
        try {
            Student cloned = (Student) super.clone();
            cloned.grades = new HashMap<>(this.grades);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Помилка копіювання", e);
        }
    }
}