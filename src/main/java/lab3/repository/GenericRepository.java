package lab3.repository;

import lab3.model.Identifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Узагальнений клас із обмеженням
public class GenericRepository<T extends Identifiable<String>> {
    private final List<T> dataList = new ArrayList<>();

    public void save(T entity) {
        dataList.add(entity);
    }

    public List<T> findAll() {
        return new ArrayList<>(dataList); // Повертаємо копію для безпеки
    }

    public void replaceAll(List<T> newData) {
        dataList.clear();
        dataList.addAll(newData);
    }

    public boolean removeById(String id) {
        if (id == null) return false;
        return dataList.removeIf(entity -> Objects.equals(entity.getId(), id));
    }

    // ЛР 5: Використання Optional
    public Optional<T> findById(String id) {
        return dataList.stream()
                .filter(entity -> Objects.equals(entity.getId(), id))
                .findFirst();
    }
}