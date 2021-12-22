package mars.logic.data;

import mars.logic.domain.Dangerzone;

import java.util.List;

public interface DangerzonesRepository {
    void generateData();
    List<Dangerzone> getDangerzones();
}
