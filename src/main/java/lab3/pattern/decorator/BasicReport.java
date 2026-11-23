package lab3.pattern.decorator;
import lab3.dto.PersonInfo;
import java.util.List;

public class BasicReport implements Report {
    @Override
    public String generate(List<PersonInfo> data) {
        StringBuilder sb = new StringBuilder();
        for (PersonInfo info : data) {
            sb.append(info.name()).append(" | ").append(info.role()).append("\n");
        }
        return sb.toString();
    }
}