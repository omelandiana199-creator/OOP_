package lab3.pattern.strategy;
import lab3.model.Student;
import java.util.List;

public interface StudentSortingStrategy {
    void sort(List<Student> students);
}