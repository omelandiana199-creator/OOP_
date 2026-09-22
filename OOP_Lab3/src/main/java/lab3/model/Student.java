package lab3.model;

import java.util.HashMap;
import java.util.Map;

public class Student extends Person {
    private Faculty faculty;
    private Map<String, Integer> grades;

    public Student(String id, String name, Faculty faculty) {
        super(id, name);
        this.faculty = faculty;
        this.grades = new HashMap<>();
    }

    // КОНСТРУКТОР ДЛЯ ПОВЕРХНЕВОГО КОПІЮВАННЯ
    public Student(Student other) {
        super(other.id, other.name);
        this.faculty = other.faculty;
        this.grades = other.grades; // Посилання на ту саму мапу оцінок
    }

    // МЕТОД ДЛЯ ГЛИБОКОГО КОПІЮВАННЯ
    public Student deepCopy() {
        Student copy = new Student(this.id, this.name, this.faculty);
        copy.grades = new HashMap<>(this.grades); // Створюємо новий об'єкт мапи в пам'яті!
        return copy;
    }

    public Faculty getFaculty() { return faculty; }
    public Map<String, Integer> getGrades() { return grades; }

    public void enrollCourse(String courseId) {
        grades.putIfAbsent(courseId, 0);
    }

    public void setGrade(String courseId, int grade) {
        if (grades.containsKey(courseId)) {
            grades.put(courseId, grade);
        }
    }

    @Override
    public String getRole() { return "Студент"; }

    @Override
    public String getDailyTask() { return "Відвідує лекції та здає лабораторні роботи."; }

    @Override
    public String toString() {
        return String.format("Студент {ID='%s', ПІБ='%s', Факультет='%s'}", id, name, faculty.getName());
    }
}