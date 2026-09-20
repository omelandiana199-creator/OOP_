package ua.edu.chdtu.oop.math;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class MathUtils {

    private MathUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // 1. Сума масиву
    public static int sumArray(int[] arr) {
        Objects.requireNonNull(arr, "Array cannot be null");
        return Arrays.stream(arr).sum();
    }

    // 2. Середнє арифметичне
    public static double calculateAverage(List<Double> list) {
        Objects.requireNonNull(list, "List cannot be null");
        return list.stream()
                .filter(Objects::nonNull) // Захист від null-елементів всередині списку
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // 3. Мінімум та максимум
    public static int[] findMinMax(int[] arr) {
        Objects.requireNonNull(arr, "Array can`t be null");
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
        IntSummaryStatistics stat = Arrays.stream(arr).summaryStatistics();
        return new int[]{stat.getMin(), stat.getMax()};
    }

    // 4. Прості числа
    public static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }

    public static List<Integer> filterPrimes(List<Integer> numbers) {
        Objects.requireNonNull(numbers, "List can`t be null");
        return numbers.stream()
                .filter(Objects::nonNull)
                .filter(MathUtils::isPrime)
                .collect(Collectors.toList());
    }

    // 5. Факторіал
    public static long factIterative(int n) {
        validateFactorialInput(n);
        return LongStream.rangeClosed(1, n).reduce(1, (a, b) -> a * b);
    }

    public static long factRecursive(int n) {
        validateFactorialInput(n);
        return n <= 1 ? 1 : n * factRecursive(n - 1);
    }

    private static void validateFactorialInput(int n) {
        if (n < 0 || n > 20) {
            throw new IllegalArgumentException("Input must be between 0 and 20 to fit in a long");
        }
    }

    // 6. Реверс масиву
    public static void reverseArray(int[] arr) {
        Objects.requireNonNull(arr, "Array cannot be null");
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
    }

    // 7. Числа Фібоначчі
    public static List<Long> generateFibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of elements can`t be negative");
        }
        return Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
                .limit(n)
                .map(f -> f[0])
                .collect(Collectors.toList());
    }

    // 8. Видалення дублікатів
    public static int[] removeDuplicates(int[] arr) {
        Objects.requireNonNull(arr, "Array cannot be null");
        return Arrays.stream(arr).distinct().toArray();
    }

    // 9. Сортування бульбашкою
    public static int bubbleSort(int[] arr) {
        Objects.requireNonNull(arr, "Array cannot be null");
        int swaps = 0;
        boolean swapped;
        for (int i = 0; i < arr.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            // Якщо обмінів не було, масив вже відсортований
            if (!swapped) break;
        }
        return swaps;
    }

    // 10. Парні та непарні
    public static int[] countEvenOdd(List<Integer> list) {
        Objects.requireNonNull(list, "List cannot be null");
        int even = (int) list.stream()
                .filter(Objects::nonNull)
                .filter(n -> n % 2 == 0)
                .count();
        int validElementsCount = (int) list.stream().filter(Objects::nonNull).count();
        return new int[]{even, validElementsCount - even};
    }
}