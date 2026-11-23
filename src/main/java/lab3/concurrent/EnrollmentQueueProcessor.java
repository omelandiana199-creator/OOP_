package lab3.concurrent;

import lab3.model.Student;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnrollmentQueueProcessor {
    private final BlockingQueue<Student> queue;
    private volatile boolean running = true;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    private final ExecutorService executor;

    public EnrollmentQueueProcessor(int queueCapacity) {
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Enrollment-Consumer-Thread");
            t.setDaemon(false);
            return t;
        });
    }

    public void submitEnrollment(Student student) throws InterruptedException {
        if (student == null) {
            throw new IllegalArgumentException("Студент не може бути null");
        }
        // Захист від блокування, якщо сервіс вже зупинено
        if (!running) {
            throw new IllegalStateException("Не можна додати заявку: процесор зупинено!");
        }

        queue.put(student);
        System.out.println(" Заявку студента " + student.getName() + " додано в чергу.");
    }

    public void startProcessing() {
        if (!isStarted.compareAndSet(false, true)) {
            System.err.println("Процесор вже запущено!");
            return;
        }

        executor.submit(() -> {
            while (running || !queue.isEmpty()) {
                try {
                    Student student = queue.poll(500, TimeUnit.MILLISECONDS);
                    if (student != null) {
                        processStudent(student);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {

                    System.err.println(" Помилка обробки заявки: " + e.getMessage());
                }
            }
            System.out.println(" Чергу заявок повністю оброблено. Потік завершує роботу.");
        });
    }

    private void processStudent(Student student) throws InterruptedException {
        System.out.println(" Обробка заявки студента: " + student.getName() + "...");
        Thread.sleep(300);
        System.out.println(" Заявку студента " + student.getName() + " успішно опрацьовано!");
    }

    public void stopProcessing() {
        this.running = false;
        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Примусове завершення потоків...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}