package ua.edu.chdtu.oop.math;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void sumArray_shouldReturnCorrectSum_whenArrayIsValid() {
        assertEquals(15, MathUtils.sumArray(new int[]{1, 2, 3, 4, 5}));
        assertEquals(0, MathUtils.sumArray(new int[]{})); // Порожній масив = 0
    }

    // 2. Тестування винятків (Negative Testing)
    @Test
    void sumArray_shouldThrowNullPointerException_whenArrayIsNull() {
        assertThrows(NullPointerException.class, () -> MathUtils.sumArray(null));
    }

    @Test
    void findMinMax_shouldReturnCorrectBounds_whenArrayIsValid() {
        assertArrayEquals(new int[]{-2, 9}, MathUtils.findMinMax(new int[]{4, 1, 9, -2, 5}));
    }

    @Test
    void findMinMax_shouldThrowIllegalArgumentException_whenArrayIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> MathUtils.findMinMax(new int[]{})
        );
        assertEquals("Array must not be empty", exception.getMessage());
    }

    @Test
    void calculateAverage_shouldHandleNullElementsInList() {
        List<Double> list = Arrays.asList(10.0, null, 30.0);
        assertEquals(20.0, MathUtils.calculateAverage(list), 0.001);
    }

    @Test
    void calculateAverage_shouldReturnZero_whenListIsEmpty() {
        assertEquals(0.0, MathUtils.calculateAverage(Collections.emptyList()), 0.001);
    }

    @Test
    void factorial_shouldCalculateCorrectly_forValidLimits() {
        assertAll(
                () -> assertEquals(1, MathUtils.factIterative(0)),
                () -> assertEquals(120, MathUtils.factIterative(5)),
                () -> assertEquals(2432902008176640000L, MathUtils.factIterative(20)) // Максимум для long
        );
    }

    @Test
    void factorial_shouldThrowException_whenOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.factIterative(-1));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.factIterative(21)); // Переповнення
    }

    @Test
    void filterPrimes_shouldReturnOnlyPrimes() {
        List<Integer> input = Arrays.asList(2, 4, 5, 10, 13, null); // Додали null для перевірки захисту
        List<Integer> expected = Arrays.asList(2, 5, 13);
        assertEquals(expected, MathUtils.filterPrimes(input));
    }

    @Test
    void generateFibonacci_shouldThrowException_whenNegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.generateFibonacci(-5));
    }
}