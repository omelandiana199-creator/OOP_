package lab3.pattern.decorator;
import lab3.dto.PersonInfo;
import java.util.List;

public class HeaderReportDecorator extends ReportDecorator {
    public HeaderReportDecorator(Report wrappee) {
        super(wrappee);
    }

    @Override
    public String generate(List<PersonInfo> data) {
        return "=== УНІВЕРСИТЕТСЬКИЙ ЗВІТ ===\n" + super.generate(data);
    }
}