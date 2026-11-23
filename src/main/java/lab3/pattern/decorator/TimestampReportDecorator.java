package lab3.pattern.decorator;
import lab3.dto.PersonInfo;
import java.util.List;
import java.time.LocalDateTime;

public class TimestampReportDecorator extends ReportDecorator {
    public TimestampReportDecorator(Report wrappee) {
        super(wrappee);
    }

    @Override
    public String generate(List<PersonInfo> data) {
        return super.generate(data) + "Час генерації: " + LocalDateTime.now() + "\n";
    }
}