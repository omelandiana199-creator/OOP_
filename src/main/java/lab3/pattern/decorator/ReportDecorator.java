package lab3.pattern.decorator;
import lab3.dto.PersonInfo;
import java.util.List;

public abstract class ReportDecorator implements Report {
    protected final Report wrappee;

    public ReportDecorator(Report wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String generate(List<PersonInfo> data) {
        return wrappee.generate(data);
    }
}