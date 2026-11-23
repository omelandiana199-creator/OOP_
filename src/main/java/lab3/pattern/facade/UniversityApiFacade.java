package lab3.pattern.facade;

import lab3.model.Faculty;
import lab3.pattern.decorator.*;
import lab3.service.FileExportService;
import lab3.service.ReportService;
import lab3.service.UniversityService;

public class UniversityApiFacade {
    private final UniversityService universityService;
    private final FileExportService fileService;
    private final ReportService reportService;

    public UniversityApiFacade(UniversityService us, FileExportService fs, ReportService rs) {
        this.universityService = us;
        this.fileService = fs;
        this.reportService = rs;
    }

    public void registerNewStudent(String id, String name, String facultyType) {
        Faculty faculty = facultyType.equalsIgnoreCase("IT") ? Faculty.IT : Faculty.HUMANITARIAN;
        universityService.addStudent(id, name, faculty);
    }

    public void executeSystemBackup() {
        fileService.createZipBackup("api_data.dat", "api_backup.zip");
    }

    public String getFullyDecoratedReport() {
        Report report = new BasicReport();
        report = new HeaderReportDecorator(report);
        report = new FooterReportDecorator(report);
        report = new TimestampReportDecorator(report);

        return report.generate(reportService.generateUniversityReport());
    }
}