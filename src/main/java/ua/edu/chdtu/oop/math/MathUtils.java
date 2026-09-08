package ua.edu.chdtu.oop.math;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class MathUtils {
    private MathUtils() {}

    // 1. Сума масиву
    public static int sumArray(int[] arr) {
        return Arrays.stream(arr).sum();
    }

    // 2. Середнє арифметичне
    public static double calculateAverage(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    // 3. Мінімум та максимум
    public static int[] findMinMax(int[] arr) {
        IntSummaryStatistics stat = Arrays.stream(arr).summaryStatistics();
        return new int[]{stat.getMin(), stat.getMax()};
    }

    // 4. Прості числа
    public static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }
    public static List<Integer> filterPrimes(List<Integer> numbers) {
        return numbers.stream().filter(MathUtils::isPrime).collect(Collectors.toList());
    }

    // 5. Факторіал
    public static long factIterative(int n) {
        return LongStream.rangeClosed(1, n).reduce(1, (a, b) -> a * b);
    }
    public static long factRecursive(int n) {
        return n <= 1 ? 1 : n * factRecursive(n - 1);
    }

    // 6. Реверс масиву
    public static void reverseArray(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i]; arr[i] = arr[arr.length - 1 - i]; arr[arr.length - 1 - i] = temp;
        }
    }

    // 7. Числа Фібоначчі
    public static List<Long> generateFibonacci(int n) {
        return Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0] + f[1]})
                .limit(n).map(f -> f[0]).collect(Collectors.toList());
    }

    // 8. Видалення дублікатів
    public static int[] removeDuplicates(int[] arr) {
        return Arrays.stream(arr).distinct().toArray();
    }

    // 9. Сортування бульбашкою з підрахунком
    public static int bubbleSort(int[] arr) {
        int swaps = 0;
        for (int i = 0; i < arr.length - 1; i++)
            for (int j = 0; j < arr.length - 1 - i; j++)
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j]; arr[j] = arr[j+1]; arr[j+1] = temp; swaps++;
                }
        return swaps;
    }

    // 10. Парні та непарні
    public static int[] countEvenOdd(List<Integer> list) {
        int even = (int) list.stream().filter(n -> n % 2 == 0).count();
        return new int[]{even, list.size() - even};
    }
}