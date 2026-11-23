package lab3.pattern.strategy;
import lab3.model.Student;
import java.util.List;

public class SortByGradeStrategy implements StudentSortingStrategy {
    @Override
    public void sort(List<Student> students) {
        students.sort((s1, s2) -> Double.compare(s2.getAverageGrade(), s1.getAverageGrade())); // За спаданням
    }
}