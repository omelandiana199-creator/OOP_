package lab3.service;

import lab3.dto.PersonInfoRecord;
import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.repository.DataRepository;
import lab3.repository.GenericRepository;
import lab3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UniversityService {
    private final DataRepository dataRepository;

    private final GenericRepository<Student> studentRepo;
    private final GenericRepository<Course> courseRepo;

    public UniversityService(DataRepository repository) {
        this.dataRepository = repository;
        this.studentRepo = new GenericRepository<>(repository.getData().students);
        this.courseRepo = new GenericRepository<>(repository.getData().courses);
    }

    public void addStudent(String id, String name, Faculty faculty) {
        studentRepo.add(new Student(id, name, faculty));
    }

    public void addCourse(String courseId, String title, String teacherName, String department, CourseFormat format) {
        Teacher teacher = new Teacher(UUID.randomUUID().toString(), teacherName, department);
        dataRepository.getData().teachers.add(teacher);
        courseRepo.add(new Course(courseId, title, teacher, format));
    }

    // Метод може кидати Checked Exception, тому ми зобов'язані вказати throws
    public void enrollStudent(String studentId, String courseId) throws CourseCapacityExceededException {
        Student student = findStudentById(studentId);
        findCourseById(courseId); // Перевіряємо існування курсу

        // Штучна доменна логіка для генерації власного винятку
        if (student.getGrades().size() >= 5) {
            throw new CourseCapacityExceededException(
                    "Студент не може бути записаний більше ніж на 5 курсів одночасно!", studentId);
        }
        student.enrollCourse(courseId);
    }

    public void gradeStudent(String studentId, String courseId, int grade) {
        Student student = findStudentById(studentId);
        if (!student.getGrades().containsKey(courseId)) {
            throw new IllegalArgumentException("Студент не записаний на цей курс!");
        }
        student.setGrade(courseId, grade);
    }

    // Обробка Optional через orElseThrow
    public Student findStudentById(String id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студента з ID не знайдено", id));
    }

    // Обробка Optional через orElseThrow
    public Course findCourseById(String id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс з ID не знайдено", id));
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    // ДЕМОНСТРАЦІЯ WILDCARD
    // Цей метод може приймати List<Student>, List<Teacher> або List<Person>
    public List<String> extractNames(List<? extends Person> people) {
        List<String> names = new ArrayList<>();
        for (Person p : people) {
            names.add(p.getRole() + ": " + p.getName());
        }
        return names;
    }

    public List<PersonInfoRecord> generateUniversityReport() {
        List<Person> allPeople = new ArrayList<>();
        allPeople.addAll(dataRepository.getData().teachers);
        allPeople.addAll(studentRepo.findAll());

        List<PersonInfoRecord> report = new ArrayList<>();
        for (Person p : allPeople) {
            String roleDetails = "";

            if (p instanceof Student s) {
                roleDetails = "Вчиться: " + s.getFaculty().getName();
            } else if (p instanceof Teacher t) {
                // Проходимо по всіх курсах і шукаємо ті, де ID викладача збігається з поточним
                List<String> teacherCourses = courseRepo.findAll().stream()
                        .filter(c -> c.getTeacher().getId().equals(t.getId()))
                        .map(Course::getTitle)
                        .toList();

                String coursesStr = teacherCourses.isEmpty() ? "немає призначених курсів" : String.join(", ", teacherCourses);
                roleDetails = "Викладає: " + coursesStr;
            }

            report.add(new PersonInfoRecord(p.getName(), roleDetails, p.getDailyTask()));
        }
        return report;
    }


    public void saveAllData() {
        dataRepository.saveData();
    }
}