package ua.edu.chdtu.oop.math;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void testSumArray() {
        assertEquals(15, MathUtils.sumArray(new int[]{1, 2, 3, 4, 5}));
        assertEquals(0, MathUtils.sumArray(new int[]{}));
    }

    @Test
    void testCalculateAverage() {
        List<Double> list = Arrays.asList(10.0, 20.0, 30.0);
        assertEquals(20.0, MathUtils.calculateAverage(list), 0.001);
    }

    @Test
    void testFindMinMax() {
        int[] arr = {4, 1, 9, -2, 5};
        int[] result = MathUtils.findMinMax(arr);
        assertArrayEquals(new int[]{-2, 9}, result);
    }

    @Test
    void testPrimeFiltering() {
        List<Integer> input = Arrays.asList(2, 4, 5, 10, 13);
        List<Integer> expected = Arrays.asList(2, 5, 13);
        assertEquals(expected, MathUtils.filterPrimes(input));
    }

    @Test
    void testFactorial() {
        // Тут ми використовуємо нові короткі назви: factIterative та factRecursive
        assertEquals(120, MathUtils.factIterative(5));
        assertEquals(120, MathUtils.factRecursive(5));
        assertEquals(1, MathUtils.factIterative(0));
    }

    @Test
    void testReverseArray() {
        int[] arr = {1, 2, 3, 4, 5};
        MathUtils.reverseArray(arr);
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, arr);
    }

    @Test
    void testGenerateFibonacci() {
        // Використовуємо інтерфейс List замість жорсткої прив'язки до ArrayList
        List<Long> result = MathUtils.generateFibonacci(5);
        assertEquals(Arrays.asList(0L, 1L, 1L, 2L, 3L), result);
    }

    @Test
    void testRemoveDuplicates() {
        int[] arr = {1, 2, 2, 3, 4, 4, 5};
        int[] result = MathUtils.removeDuplicates(arr);
        assertEquals(5, result.length);
        Arrays.sort(result);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void testBubbleSort() {
        // Нова назва методу сортування
        int[] arr = {5, 1, 4, 2, 8};
        int swaps = MathUtils.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 4, 5, 8}, arr);
        assertTrue(swaps > 0);
    }

    @Test
    void testCountEvenOdd() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 8);
        int[] result = MathUtils.countEvenOdd(list);
        assertArrayEquals(new int[]{4, 3}, result);
    }
}