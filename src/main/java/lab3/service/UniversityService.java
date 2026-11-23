package lab3.service;

import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.factory.PersonFactory;
import lab3.factory.StudentFactory;
import lab3.factory.TeacherFactory;
import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Student;
import lab3.model.Teacher;
import lab3.pattern.observer.AdminConsoleObserver;
import lab3.pattern.observer.FileLoggingObserver;
import lab3.pattern.observer.UniversityEventPublisher;
import lab3.repository.DataRepository;
import lab3.repository.GenericRepository;
import lab3.pattern.strategy.StudentSortingStrategy;
import lab3.functional.StudentFilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UniversityService {

    private final DataRepository dataRepository;
    private final GenericRepository<Student> studentRepo;
    private final GenericRepository<Course> courseRepo;
    private final PersonFactory<Student, Faculty> studentFactory;
    private final PersonFactory<Teacher, String> teacherFactory;
    private final UniversityEventPublisher eventPublisher;

    public UniversityService(DataRepository repository) {
        this.dataRepository = repository;
        this.studentRepo = new GenericRepository<>();
        this.studentRepo.replaceAll(repository.getData().getStudents());
        this.courseRepo = new GenericRepository<>();
        this.courseRepo.replaceAll(repository.getData().getCourses());
        this.studentFactory = new StudentFactory();
        this.teacherFactory = new TeacherFactory();

        this.eventPublisher = new UniversityEventPublisher();
        this.eventPublisher.subscribe(new FileLoggingObserver());
        this.eventPublisher.subscribe(new AdminConsoleObserver());
    }

    public GenericRepository<Student> getStudentRepo() { return studentRepo; }
    public DataRepository getDataRepository() { return dataRepository; }
    public UniversityEventPublisher getEventPublisher() { return eventPublisher; }
    public void saveAllData() { dataRepository.saveData(); }

    public void removeStudent(String studentId) {
        boolean isRemoved = studentRepo.removeById(studentId);
        if (!isRemoved) throw new EntityNotFoundException("Студента для видалення не знайдено", studentId);
        eventPublisher.notifyObservers("INFO", "Видалено студента з ID: " + studentId);
    }

    public void executeSortingStrategy(StudentSortingStrategy strategy) {
        List<Student> list = new ArrayList<>(studentRepo.findAll());
        if (list.isEmpty()) return;
        strategy.sort(list);
        list.forEach(s -> System.out.printf("%s (Бал: %.2f)\n", s.getName(), s.getAverageGrade()));
    }

    // ЛР 12: Використання власного інтерфейсу та Stream API
    public List<Student> filterStudentsCustom(StudentFilter filter) {
        return studentRepo.findAll().stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public long countStudentsByCustomFilter(StudentFilter filter) {
        return studentRepo.findAll().stream()
                .filter(filter::test)
                .count();
    }

    // ЛР 12: Вбудовані функціональні інтерфейси (Predicate, Function, Consumer)
    // Та використання методів Stream
    public void printStudentNamesWithBuiltInFunctions(Predicate<Student> condition) {
        Function<Student, String> nameExtractor = Student::getName;
        Consumer<String> printer = name -> System.out.println("-> " + name);

        studentRepo.findAll().stream()
                .filter(condition)
                .map(nameExtractor)
                .forEach(printer);
    }

    public Map<Faculty, List<Student>> groupStudentsByFaculty() {
        return studentRepo.findAll().stream()
                .collect(Collectors.groupingBy(Student::getFaculty));
    }

    public void gradeStudent(String studentId, String courseId, int grade) {
        Student student = findStudentById(studentId);
        if (!student.getGrades().containsKey(courseId)) {
            throw new IllegalArgumentException("Студент не записаний на цей курс!");
        }
        student.setGrade(courseId, grade);
    }

    public void addStudent(String id, String name, Faculty faculty) {
        Student student = studentFactory.create(id, name, faculty);
        studentRepo.save(student);
        eventPublisher.notifyObservers("INFO", "Додано нового студента: " + name);
    }

    public void addCourse(String courseId, String title, String teacherName, String department, CourseFormat format) {
        Teacher teacher = teacherFactory.create(UUID.randomUUID().toString(), teacherName, department);
        dataRepository.getData().getTeachers().add(teacher);
        Course course = new Course.Builder().courseId(courseId).title(title).teacher(teacher).format(format).build();
        courseRepo.save(course);
        eventPublisher.notifyObservers("INFO", "Створено новий курс: " + title);
    }

    public void enrollStudent(String studentId, String courseId) throws CourseCapacityExceededException {
        Student student = findStudentById(studentId);
        findCourseById(courseId);
        if (student.getGrades().size() >= 5) {
            eventPublisher.notifyObservers("URGENT", "Перевищення ліміту курсів: " + student.getName());
            throw new CourseCapacityExceededException("Ліміт курсів перевищено!", studentId);
        }
        student.enrollCourse(courseId);
        eventPublisher.notifyObservers("INFO", "Студент " + student.getName() + " записаний на " + courseId);
    }

    public void processStudentsInParallel(Consumer<Student> action) throws InterruptedException {
        List<Student> allStudents = studentRepo.findAll();
        if (allStudents.isEmpty()) return;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (Student student : allStudents) {
            tasks.add(() -> { action.accept(student); return null; });
        }
        executor.invokeAll(tasks);
        executor.shutdown();
    }

    public Student findStudentById(String id) {
        return studentRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Студента не знайдено", id));
    }

    public Course findCourseById(String id) {
        return courseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Курс не знайдено", id));
    }

    public Optional<Student> findTopStudentByFaculty(Faculty faculty) {
        return studentRepo.findAll().stream()
                .filter(s -> s.getFaculty() == faculty)
                .max(Comparator.comparingDouble(Student::getAverageGrade));
    }

    public void sortStudentsDemo() {
        List<Student> students = studentRepo.findAll();
        System.out.println("\n--- 1. Природне сортування (Comparable - за ПІБ) ---");
        students.stream().sorted().forEach(s -> System.out.println(s.getName()));
        System.out.println("\n--- 2. Сортування (Comparator - за середнім балом за спаданням) ---");
        students.sort(Comparator.comparingDouble(Student::getAverageGrade).reversed());
        students.forEach(s -> System.out.println(s.getName() + " - " + s.getAverageGrade()));
        System.out.println("\n--- 3. Сортування (Comparator - за факультетом) ---");
        students.sort(Comparator.comparing(s -> s.getFaculty().getDescription()));
        students.forEach(s -> System.out.println(s.getName() + " - " + s.getFaculty().getDescription()));
    }

    public void showGradesTreeMapDemo() {
        TreeMap<Double, Student> gradeMap = new TreeMap<>();
        for (Student s : studentRepo.findAll()) {
            gradeMap.put(s.getAverageGrade(), s);
        }
        System.out.println("\n--- Студенти відсортовані за балами (TreeMap) ---");
        gradeMap.forEach((grade, student) -> System.out.println(student.getName() + " : " + grade));
        if (!gradeMap.isEmpty()) {
            System.out.println("Студент з найвищим балом: " + gradeMap.lastEntry().getValue().getName());
        }
    }
}