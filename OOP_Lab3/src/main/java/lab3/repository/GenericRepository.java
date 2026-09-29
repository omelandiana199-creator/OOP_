package lab3.repository;

import lab3.model.Identifiable;
import java.util.List;
import java.util.Optional;

// Generic клас з обмеженням
public class GenericRepository<T extends Identifiable<String>> {
    private final List<T> dataList;

    public GenericRepository(List<T> dataList) {
        this.dataList = dataList;
    }

    public void add(T entity) {
        dataList.add(entity);
    }

    public List<T> findAll() {
        return dataList;
    }

    public Optional<T> findById(String id) {
        return dataList.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }
}