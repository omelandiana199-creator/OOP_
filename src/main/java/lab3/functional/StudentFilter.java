package lab3.functional;

import lab3.model.Student;

@FunctionalInterface
public interface StudentFilter {
    boolean test(Student student);
}