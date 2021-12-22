package mars.logic.data;

import mars.logic.domain.Dispatch;
import mars.logic.domain.DispatchSource;
import mars.logic.domain.DispatchTarget;

import java.util.List;

public interface DispatchesRepository {
    void generateData();
    List<Dispatch> getDispatches();
    Dispatch getDispatch(String identifier);
    void deleteDispatch(String identifier);
    void addDispatch(String identifier, DispatchSource source, DispatchTarget target);
}
