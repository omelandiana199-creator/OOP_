package lab3.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class StudentGroup implements Iterable<Student> {
    private final List<Student> students;

    public StudentGroup(List<Student> students) {
        this.students = students;
    }

    @Override
    public Iterator<Student> iterator() {
        return new Iterator<Student>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < students.size();
            }

            @Override
            public Student next() {
                if (!hasNext()) throw new NoSuchElementException();
                return students.get(currentIndex++);
            }
        };
    }
}