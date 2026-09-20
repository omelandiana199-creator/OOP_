package lab3;

import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UniversityLogicTest {

    private Student student;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student("S-01", "Олександр Сергійович", Faculty.IT);
        teacher = new Teacher("T-01", "Василь Іванович", "Кафедра інженерії програмного забезпечення");
        course = new Course("C-01", "Об'єктно-орієнтоване програмування", teacher, CourseFormat.ONLINE);
    }

    @Test
    void testPersonRolePolymorphism() {
        assertEquals("Студент", student.getRole(),
                "Роль студента має повертати 'Студент'");
        assertEquals("Викладач (Кафедра інженерії програмного забезпечення)", teacher.getRole(),
                "Роль викладача має коректно формуватися з назвою кафедри");
    }

    @Test
    void testCourseCreation() {
        assertEquals("C-01", course.getId());
        assertEquals("Об'єктно-орієнтоване програмування", course.getTitle());
    }

    @Test
    void testEnrollCourse() {
        student.enrollCourse("C-01");

        assertTrue(student.getGrades().containsKey("C-01"),
                "Після запису курс має з'явитися у мапі студента");
        assertEquals(0, student.getGrades().get("C-01"),
                "Початкова оцінка щойно записаного курсу має дорівнювати 0");
    }

    @Test
    void testSetGradeForEnrolledCourse() {
        student.enrollCourse("C-01");
        student.setGrade("C-01", 95);

        assertEquals(95, student.getGrades().get("C-01"),
                "Оцінка має успішно оновитися до 95");
    }

    @Test
    void testSetGradeForNotEnrolledCourse() {
        student.setGrade("C-01", 95);

        assertFalse(student.getGrades().containsKey("C-01"),
                "Оцінка не повинна додаватися, якщо студент не записаний на цей курс");
    }
}