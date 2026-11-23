package lab3.pattern.strategy;
import lab3.model.Student;
import java.util.List;

public class SortByNameStrategy implements StudentSortingStrategy {
    @Override
    public void sort(List<Student> students) {
        students.sort((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()));
    }
}