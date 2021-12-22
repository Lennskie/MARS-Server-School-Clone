package mars.logic.data;

import mars.logic.domain.Dispatch;
import mars.logic.domain.DispatchSource;
import mars.logic.domain.DispatchDestination;

import java.util.List;

public interface DispatchesRepository {
    void generateData();
    List<Dispatch> getDispatches();
    Dispatch getDispatch(String identifier);
    void deleteDispatch(String identifier);
    Dispatch addDispatch(String identifier, DispatchSource source, DispatchDestination target);
}
