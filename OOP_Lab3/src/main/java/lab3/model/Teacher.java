package lab3.model;

public class Teacher extends Person {
    private String department;

    public Teacher(String id, String name, String department) {
        super(id, name);
        this.department = department;
    }

    public String getDepartment() { return department; }

    @Override
    public String getRole() { return "Викладач (" + department + ")"; }

    @Override
    public String getDailyTask() { return "Проводить заняття та перевіряє лабораторні."; }

    @Override
    public String toString() {
        return String.format("Викладач {ID='%s', ПІБ='%s', Кафедра='%s'}", id, name, department);
    }
}