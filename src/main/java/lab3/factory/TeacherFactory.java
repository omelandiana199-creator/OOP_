package lab3.factory;

import lab3.model.Teacher;

public class TeacherFactory implements PersonFactory<Teacher, String> {
    @Override
    public Teacher create(String id, String name, String department) {
        return new Teacher(id, name, department);
    }
}