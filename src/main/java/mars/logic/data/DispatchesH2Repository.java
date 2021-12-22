package mars.logic.data;

import mars.logic.domain.Dispatch;
import mars.logic.domain.DispatchSource;
import mars.logic.domain.DispatchTarget;

import java.util.List;

public class DispatchesH2Repository implements DispatchesRepository {

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Dispatch> getDispatches() {
        return null;
    }

    @Override
    public Dispatch getDispatch(String identifier) {
        return null;
    }

    @Override
    public void deleteDispatch(String identifier) {

    }

    @Override
    public void addDispatch(String identifier, DispatchSource source, DispatchTarget target) {

    }
}
