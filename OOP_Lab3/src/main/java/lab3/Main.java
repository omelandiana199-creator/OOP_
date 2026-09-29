package lab3;

import lab3.repository.DataRepository;
import lab3.service.UniversityService;
import lab3.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {

        DataRepository repository = new DataRepository("university.json");
        UniversityService service = new UniversityService(repository);
        ConsoleUI ui = new ConsoleUI(service);

        ui.start();
    }
}