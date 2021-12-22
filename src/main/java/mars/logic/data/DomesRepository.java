package mars.logic.data;

import mars.logic.domain.Dome;

import java.util.List;

public interface DomesRepository {
    void generateData();
    List<Dome> getDomes();
    Dome getDome(String identifier);
}
