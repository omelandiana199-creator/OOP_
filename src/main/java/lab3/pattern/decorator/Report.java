package lab3.pattern.decorator;
import lab3.dto.PersonInfo;
import java.util.List;

public interface Report {
    String generate(List<PersonInfo> data);
}