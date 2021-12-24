package mars.logic.data;

import mars.logic.domain.Dispatch;
import java.util.List;

public interface DispatchesRepository {
    void generateData();
    List<Dispatch> getDispatches();
    Dispatch getDispatch(String identifier);
    void deleteDispatch(String identifier);
    Dispatch addDispatch(String identifier, String sourceType, String destinationType, String sourceIdentifier, String destinationIdentifier);
}
