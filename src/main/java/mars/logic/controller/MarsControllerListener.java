package mars.logic.controller;

import mars.logic.domain.Client;
import mars.logic.domain.Dispatch;
import mars.logic.domain.Quote;
import mars.logic.domain.Vehicle;

public interface MarsControllerListener {
    // DP: Observer Pattern Actions to execute when the observed object (marsController) executes a specific action
    void onQuoteCreated(Quote quote);
    void onDispatchAdded(Dispatch dispatch);
    void onDispatchDeleted(Dispatch dispatch);
    void onVehicleMoved(Vehicle vehicle);
    void onClientMoved(Client client);
}
