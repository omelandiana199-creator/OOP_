package lab3.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConcurrencyDemoService {
    private int counterUnsync = 0;
    private int counterSync = 0;
    private final Object lock = new Object();

    public void demoRaceConditionAndSync() {
        System.out.println("\n--- ДЕМОНСТРАЦІЯ RACE CONDITION ТА СИНХРОНІЗАЦІЇ ---");
        counterUnsync = 0;
        counterSync = 0;

        Runnable task = () -> {
            for (int i = 0; i < 10000; i++) {
                counterUnsync++;
                synchronized (lock) {
                    counterSync++;
                }
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

        t1.start(); t2.start(); t3.start();

        try {
            t1.join(); t2.join(); t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Очікуване значення лічильника: 30000");
        System.out.println("Без синхронізації (Race Condition): " + counterUnsync + " (значення втрачені)");
        System.out.println("З блоком synchronized: " + counterSync + " (дані цілі)");
    }

    public void demoProducerConsumer() {
        System.out.println("\n--- ДЕМОНСТРАЦІЯ ВИРОБНИК-СПОЖИВАЧ ---");
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("[Виробник] Створює завдання " + i);
                    queue.put("Завдання-" + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String task = queue.take();
                    System.out.println("[Споживач] Обробляє " + task);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start(); consumer.start();
        try {
            producer.join(); consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}