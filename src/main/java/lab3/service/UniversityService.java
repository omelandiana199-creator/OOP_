package lab3.service;

import lab3.dto.PersonInfoRecord;
import lab3.exception.CourseCapacityExceededException;
import lab3.exception.EntityNotFoundException;
import lab3.model.Course;
import lab3.model.CourseFormat;
import lab3.model.Faculty;
import lab3.model.Person;
import lab3.model.Student;
import lab3.model.StudentBinaryTree;
import lab3.model.StudentGroup;
import lab3.model.Teacher;
import lab3.repository.DataRepository;
import lab3.repository.GenericRepository;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class UniversityService {
    private final DataRepository dataRepository;
    private final GenericRepository<Student> studentRepo;
    private final GenericRepository<Course> courseRepo;
    private final LoggerService logger;

    public UniversityService(DataRepository repository) {
        this.dataRepository = repository;
        this.studentRepo = new GenericRepository<>(repository.getData().students);
        this.courseRepo = new GenericRepository<>(repository.getData().courses);
        this.logger = new LoggerService();
    }

    public LoggerService getLogger() { return logger; }

    public void demoSorting() {
        List<Student> list = new ArrayList<>(studentRepo.findAll());
        if (list.isEmpty()) {
            System.out.println("Список порожній."); return;
        }

        System.out.println("\n--- 1. Природне сортування (Comparable - за алфавітом) ---");
        Collections.sort(list);
        list.forEach(s -> System.out.println(s.getName()));

        System.out.println("\n--- 2. Сортування (Comparator 1 - за середнім балом спадання) ---");
        list.sort(Comparator.comparing(Student::getAverageGrade).reversed());
        list.forEach(s -> System.out.printf("%s (Бал: %.2f)\n", s.getName(), s.getAverageGrade()));

        System.out.println("\n--- 3. Сортування (Comparator 2 - за назвою факультету) ---");
        list.sort(Comparator.comparing(s -> s.getFaculty().getName()));
        list.forEach(s -> System.out.printf("%s (Факультет: %s)\n", s.getName(), s.getFaculty().getName()));
    }

    public void demoTreeMap() {
        System.out.println("\n--- Застосування TreeMap (групування за середнім балом) ---");
        TreeMap<Double, List<Student>> gradeMap = new TreeMap<>();
        for (Student s : studentRepo.findAll()) {
            double avg = s.getAverageGrade();
            gradeMap.putIfAbsent(avg, new ArrayList<>());
            gradeMap.get(avg).add(s);
        }
        for (Map.Entry<Double, List<Student>> entry : gradeMap.entrySet()) {
            System.out.printf("Середній бал %.2f: ", entry.getKey());
            entry.getValue().forEach(s -> System.out.print(s.getName() + ", "));
            System.out.println();
        }
    }

    public void demoCustomIterator() {
        System.out.println("\n--- Власний Ітератор (for-each цикл) ---");
        StudentGroup group = new StudentGroup(studentRepo.findAll());
        for (Student s : group) {
            System.out.println("- " + s.getName() + " (" + s.getId() + ")");
        }
    }

    public void demoBinaryTree() {
        System.out.println("\n--- Власне Бінарне Дерево Пошуку (Обхід In-Order) ---");
        StudentBinaryTree tree = new StudentBinaryTree();
        studentRepo.findAll().forEach(tree::insert);
        tree.inOrderTraversal(s -> System.out.println(s.getId() + " -> " + s.getName()));
    }

    public void runCollectionsBenchmark() {
        int count = 100_000;
        System.out.println("\n--- Бенчмарк: ArrayList vs LinkedList (Вставка в середину) ---");
        List<Integer> arrayList = new ArrayList<>();
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) arrayList.add(arrayList.size() / 2, i);
        long end1 = System.currentTimeMillis();

        List<Integer> linkedList = new LinkedList<>();
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) linkedList.add(linkedList.size() / 2, i);
        long end2 = System.currentTimeMillis();

        System.out.println("ArrayList (час мс): " + (end1 - start1));
        System.out.println("LinkedList (час мс): " + (end2 - start2));
        System.out.println("Висновок: ArrayList значно швидший при вставці в середину на великих об'ємах завдяки копіюванню блоків пам'яті (System.arraycopy), тоді як LinkedList витрачає час на лінійний пошук позиції (O(N)).");
    }


    public void exportDataToText(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("--- СТУДЕНТИ ---\n");
            for (Student s : studentRepo.findAll()) writer.write(s.getId() + ";" + s.getName() + ";" + s.getFaculty().getName() + "\n");
            writer.write("--- ВИКЛАДАЧІ ---\n");
            for (Teacher t : dataRepository.getData().teachers) writer.write(t.getId() + ";" + t.getName() + ";" + t.getDepartment() + "\n");
            logger.log("Дані експортовано в текстовий файл: " + filename);
        } catch (IOException e) { System.out.println("❌ Помилка запису тексту: " + e.getMessage()); }
    }

    public void exportDataToBinary(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(new ArrayList<>(studentRepo.findAll()));
            oos.writeObject(new ArrayList<>(dataRepository.getData().teachers));
            logger.log("Дані серіалізовано у бінарний файл: " + filename);
        } catch (IOException e) { System.out.println("❌ Помилка серіалізації: " + e.getMessage()); }
    }

    @SuppressWarnings("unchecked")
    public void importDataFromBinary(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            List<Student> loadedStudents = (List<Student>) ois.readObject();
            List<Teacher> loadedTeachers = (List<Teacher>) ois.readObject();
            studentRepo.findAll().clear();
            studentRepo.findAll().addAll(loadedStudents);
            dataRepository.getData().teachers.clear();
            dataRepository.getData().teachers.addAll(loadedTeachers);
            logger.log("Дані успішно відновлено з бінарного файлу.");
        } catch (IOException | ClassNotFoundException e) { System.out.println("❌ Помилка десеріалізації: " + e.getMessage()); }
    }

    public void createZipBackup(String sourceFile, String zipFile) {
        exportDataToBinary(sourceFile);
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(sourceFile)) {
            ZipEntry zipEntry = new ZipEntry(sourceFile);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) zos.write(buffer, 0, length);
            zos.closeEntry();
            logger.log("Створено резервну ZIP-копію: " + zipFile);
        } catch (IOException e) { System.out.println("❌ Помилка архівації: " + e.getMessage()); }
    }

    public void restoreFromZipBackup(String zipFile, String extractFile) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
             FileOutputStream fos = new FileOutputStream(extractFile)) {
            ZipEntry zipEntry = zis.getNextEntry();
            if (zipEntry != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) >= 0) fos.write(buffer, 0, length);
                zis.closeEntry();
                importDataFromBinary(extractFile);
                logger.log("Дані відновлено з ZIP-архіву.");
            }
        } catch (IOException e) { System.out.println("❌ Помилка розпакування: " + e.getMessage()); }
    }

    public void importStudentFromCsvLine(String csvLine) {
        String trimmed = csvLine.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("Порожній рядок.");
        String cleanLine = trimmed.replace("\"", "");
        String[] parts = cleanLine.split(";");
        if (parts.length < 5) throw new IllegalArgumentException("Невірний формат CSV. Потрібно 5 колонок.");
        String id = parts[0].trim();
        String name = parts[1].trim();
        String facultyStr = parts[2].trim().toLowerCase();
        String email = parts[3].trim();
        String phone = parts[4].trim();

        if (!id.matches("^S-\\d{3,5}$")) throw new IllegalArgumentException("Невалідний ID. Формат: S-123");
        if (!Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$").matcher(email).matches()) throw new IllegalArgumentException("Невалідний Email");
        if (!Pattern.compile("^\\+380\\d{9}$").matcher(phone).matches()) throw new IllegalArgumentException("Невалідний телефон");

        Faculty faculty = facultyStr.contains("фітіс") || facultyStr.contains("it") ? Faculty.IT :
                facultyStr.contains("феу") ? Faculty.ECONOMICS : Faculty.HUMANITARIAN;
        addStudent(id, name, faculty);
    }

    public void exportFormattedReport(String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================\n");
        sb.append(String.format("| %-10.10s | %-25.25s | %-18.18s |\n", "ID", "ПІП", "Роль"));
        sb.append("---------------------------------------------------------------\n");
        for (Student s : studentRepo.findAll()) sb.append(String.format("| %-10.10s | %-25.25s | %-18.18s |\n", s.getId(), s.getName(), "Студент"));
        for (Teacher t : dataRepository.getData().teachers) sb.append(String.format("| %-10.10s | %-25.25s | %-18.18s |\n", t.getId(), t.getName(), "Викладач"));
        sb.append("===============================================================\n");
        System.out.println(sb.toString());
        try (PrintWriter out = new PrintWriter(filename)) { out.println(sb.toString()); }
        catch (IOException e) { System.out.println("❌ Помилка запису: " + e.getMessage()); }
    }

    public void runStringBenchmark() {
        int iterations = 100_000;
        long t1 = System.currentTimeMillis();
        String str = ""; for (int i = 0; i < iterations; i++) str += "A";
        long t2 = System.currentTimeMillis();
        long t3 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder(); for (int i = 0; i < iterations; i++) sb.append("A");
        long t4 = System.currentTimeMillis();
        System.out.println("Час '+': " + (t2 - t1) + " мс | StringBuilder: " + (t4 - t3) + " мс");
    }

    public void addStudent(String id, String name, Faculty faculty) { studentRepo.add(new Student(id, name, faculty)); }
    public void addCourse(String courseId, String title, String teacherName, String department, CourseFormat format) {
        Teacher teacher = new Teacher(UUID.randomUUID().toString(), teacherName, department);
        dataRepository.getData().teachers.add(teacher);
        courseRepo.add(new Course(courseId, title, teacher, format));
    }
    public void enrollStudent(String studentId, String courseId) throws CourseCapacityExceededException {
        Student student = findStudentById(studentId);
        findCourseById(courseId);
        if (student.getGrades().size() >= 5) throw new CourseCapacityExceededException("Ліміт курсів перевищено!", studentId);
        student.enrollCourse(courseId);
    }
    public void gradeStudent(String studentId, String courseId, int grade) {
        Student student = findStudentById(studentId);
        if (!student.getGrades().containsKey(courseId)) throw new IllegalArgumentException("Студент не записаний!");
        student.setGrade(courseId, grade);
    }
    public Student findStudentById(String id) { return studentRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Не знайдено", id)); }
    public Course findCourseById(String id) { return courseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Не знайдено", id)); }
    public List<Course> getAllCourses() { return courseRepo.findAll(); }
    public List<String> extractNames(List<? extends Person> people) {
        List<String> names = new ArrayList<>();
        for (Person p : people) names.add(p.getRole() + ": " + p.getName());
        return names;
    }
    public List<PersonInfoRecord> generateUniversityReport() {
        List<Person> allPeople = new ArrayList<>();
        allPeople.addAll(dataRepository.getData().teachers);
        allPeople.addAll(studentRepo.findAll());
        List<PersonInfoRecord> report = new ArrayList<>();
        for (Person p : allPeople) {
            String roleDetails = p instanceof Student s ? "Навчається: " + s.getFaculty().getName() : "Викладає";
            report.add(new PersonInfoRecord(p.getName(), roleDetails, p.getDailyTask()));
        }
        return report;
    }
    public void saveAllData() { dataRepository.saveData(); }
}