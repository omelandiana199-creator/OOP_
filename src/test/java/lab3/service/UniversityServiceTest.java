package lab3.service;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UniversityServiceTest {

    private UniversityService universityService;

    @BeforeEach
    void setUp() {

        DataRepository repository = new DataRepository("test_data.dat");
        universityService = new UniversityService(repository);
    }

    @Test
    @DisplayName("Успішне додавання та пошук студента за ID")
    void testAddAndFindStudent_Success() {
        String studentId = "S-01";
        String name = "Олександр Петренко";

        universityService.addStudent(studentId, name, Faculty.IT);
        Student foundStudent = universityService.findStudentById(studentId);

        assertAll("Перевірка властивостей знайденого студента",
                () -> assertNotNull(foundStudent, "Студент не повинен бути null"),
                () -> assertEquals(studentId, foundStudent.getId()),
                () -> assertEquals(name, foundStudent.getName()),
                () -> assertEquals(Faculty.IT, foundStudent.getFaculty())
        );
    }

    @Test
    @DisplayName("Пошук неіснуючого студента викидає EntityNotFoundException")
    void testFindStudentById_NotFound_ThrowsException() {

        String invalidId = "S-99";

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> universityService.findStudentById(invalidId)
        );

        assertAll("Перевірка винятку",
                () -> assertTrue(exception.getMessage().contains("не знайдено")),
                () -> assertEquals(invalidId, exception.getEntityId())
        );
    }

    @Test
    @DisplayName("Успішний запис студента на курс")
    void testEnrollStudent_Success() throws CourseCapacityExceededException {
        String studentId = "S-01";
        String courseId = "C-01";
        universityService.addStudent(studentId, "Іван", Faculty.IT);
        universityService.addCourse(courseId, "Java", "Викладач", "Кафедра ПЗ", CourseFormat.ONLINE);

        universityService.enrollStudent(studentId, courseId);
        Student student = universityService.findStudentById(studentId);

        assertTrue(student.getGrades().containsKey(courseId), "Студент має бути записаний на курс");
    }

    @Test
    @DisplayName("Пошук найкращого студента факультету повертає правильний Optional")
    void testFindTopStudentByFaculty_ReturnsMaxGrade() throws CourseCapacityExceededException {
        String weakStudentId = "S-01";
        String strongStudentId = "S-02";
        String courseId = "C-1";

        universityService.addStudent(weakStudentId, "Слабкий Студент", Faculty.IT);
        universityService.addStudent(strongStudentId, "Сильний Студент", Faculty.IT);
        universityService.addCourse(courseId, "Math", "T", "D", CourseFormat.OFFLINE);

        universityService.enrollStudent(weakStudentId, courseId);
        universityService.gradeStudent(weakStudentId, courseId, 60);

        universityService.enrollStudent(strongStudentId, courseId);
        universityService.gradeStudent(strongStudentId, courseId, 95);

        Optional<Student> topStudent = universityService.findTopStudentByFaculty(Faculty.IT);

        assertTrue(topStudent.isPresent(), "Має бути знайдений хоча б один студент");
        assertEquals("Сильний Студент", topStudent.get().getName(), "Найкращим має бути Сильний Студент");
    }
}