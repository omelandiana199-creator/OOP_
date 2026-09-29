package lab3.model;

import java.util.HashMap;
import java.util.Map;

public class Student extends Person implements Comparable<Student> {
    private Faculty faculty;
    // Map за прямим призначенням: швидкий пошук (O(1)) оцінки за ID курсу
    private Map<String, Integer> grades;

    public Student(String id, String name, Faculty faculty) {
        super(id, name);
        this.faculty = faculty;
        this.grades = new HashMap<>();
    }

    public Faculty getFaculty() { return faculty; }
    public Map<String, Integer> getGrades() { return grades; }

    public void enrollCourse(String courseId) { grades.putIfAbsent(courseId, 0); }

    public void setGrade(String courseId, int grade) {
        if (grades.containsKey(courseId)) grades.put(courseId, grade);
    }

    public double getAverageGrade() {
        if (grades.isEmpty()) return 0.0;
        return grades.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public String getRole() { return "Студент"; }

    @Override
    public String getDailyTask() { return "Відвідувати лекції та виконувати практичні завдання."; }

    // Природне впорядкування (Comparable) - за ім'ям
    @Override
    public int compareTo(Student other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}