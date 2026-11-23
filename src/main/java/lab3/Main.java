package lab3;

import lab3.repository.DataRepository;
import lab3.service.FileExportService;
import lab3.service.ReportService;
import lab3.service.UniversityService;
import lab3.service.LoggerService;
import lab3.ui.ConsoleUI;

public class Main {

    public static void main(String[] args) {

        try {

            DataRepository repository = new DataRepository("university.json");

            UniversityService coreService = new UniversityService(repository);

            FileExportService fileService = new FileExportService(coreService);
            ReportService reportService = new ReportService(coreService);

            ConsoleUI ui = new ConsoleUI(coreService, fileService, reportService);

            ui.start();

        } catch (Exception e) {
            System.err.println("❌ Критичний збій при запуску системи. Деталі в логах.");
            LoggerService.getInstance().error("Критичний збій при запуску", e);
        }
    }
}