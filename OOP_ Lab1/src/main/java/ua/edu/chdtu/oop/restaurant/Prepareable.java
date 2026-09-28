package ua.edu.chdtu.oop.restaurant;

public interface Prepareable {
    //Дає команду розпочати процес приготування.
    void startPreparation();

    //Перевіряє, чи завершено процес приготування

    boolean isReady();
    String getStatus();
}