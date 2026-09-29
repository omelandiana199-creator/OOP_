package ua.edu.chdtu.oop.restaurant;

//Контракт для будь-яких позицій замовлення, які потребують часу на приготування

public interface Prepareable {
    //Дає команду розпочати процес приготування.
    void startPreparation();

    //Перевіряє, чи завершено процес приготування

    boolean isReady();
    String getStatus();
}