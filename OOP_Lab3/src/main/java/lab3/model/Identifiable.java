package lab3.model;

// Обмеження узагальнення: усі в репозиторії повинні мати ID
public interface Identifiable<ID> {
    ID getId();
}