package lab3.factory;

import lab3.model.Faculty;
import lab3.model.Student;

public class StudentFactory implements PersonFactory<Student, Faculty> {
    @Override
    public Student create(String id, String name, Faculty faculty) {
        return new Student(id, name, faculty);
    }
}