package lab3.pattern.strategy;
import lab3.model.Student;
import java.util.List;

public class SortByFacultyStrategy implements StudentSortingStrategy {
    @Override
    public void sort(List<Student> students) {
        students.sort((s1, s2) -> s1.getFaculty().getDescription().compareTo(s2.getFaculty().getDescription()));
    }
}