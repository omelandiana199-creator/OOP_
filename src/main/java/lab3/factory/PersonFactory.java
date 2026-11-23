package lab3.factory;

import lab3.model.Person;

/**
 * абстрактна фабрика для створення об'єктів, пов'язаних з людьми в університеті.
 *
 * @param <T> тип персони, що створюється (повинен наслідувати Person)
 * @param <D> тип додаткових даних (наприклад, Факультет або Кафедра)
 */
public interface PersonFactory<T extends Person, D> {

    T create(String id, String name, D additionalData);
}