package lab3.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("SO-24", "Марія Іваненко", Faculty.ECONOMICS);
    }

    @Test
    @DisplayName("Deep Copy створює повністю незалежний об'єкт")
    void testDeepCopy_CreatesIndependentObject() {
        student.enrollCourse("EC-24");
        student.setGrade("EC-24", 85);

        Student deepCopy = student.deepCopy();
        deepCopy.setGrade("EC-24", 100);
        deepCopy.enrollCourse("MANAGEMENT");

        assertAll("Перевірка незалежності глибокої копії",
                () -> assertNotSame(student, deepCopy, "Посилання на об'єкти мають бути різними"),
                () -> assertEquals(85, student.getGrades().get("EC-24"), "Оцінка оригіналу не мала змінитись"),
                () -> assertEquals(100, deepCopy.getGrades().get("EC-24"), "Копія має мати нову оцінку"),
                () -> assertFalse(student.getGrades().containsKey("MANAGEMENT"), "Оригінал не повинен містити новий курс копії")
        );
    }

    @Test
    @DisplayName("Shallow Copy зберігає спільні посилання на колекції")
    void testShallowCopy_SharesReferences() {
        student.enrollCourse("IT-10");
        student.setGrade("IT-10", 90);

        Student shallowCopy = student.shallowCopy();
        shallowCopy.setGrade("IT-10", 50);

        assertEquals(50, student.getGrades().get("IT-10"), "Оригінал постраждав від зміни поверхневої копії");
    }

    @Test
    @DisplayName("Розрахунок середнього балу працює коректно")
    void testAverageGrade_Calculation() {
        student.enrollCourse("C1");
        student.enrollCourse("C2");
        student.setGrade("C1", 90);
        student.setGrade("C2", 100);

        assertEquals(95.0, student.getAverageGrade(), 0.01, "Середній бал має бути (90+100)/2 = 95.0");
    }
}