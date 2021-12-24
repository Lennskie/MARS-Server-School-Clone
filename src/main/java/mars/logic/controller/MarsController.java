package mars.logic.controller;

import mars.logic.domain.*;
import io.vertx.core.Future;

import java.util.List;

public interface MarsController {

    List<Subscription> getSubscriptions();

    List<Dangerzone> getDangerzones();

    void addListener(MarsControllerListener listener);

    List<Vehicle> getVehicles();

    Vehicle getVehicle(String identifier);

    List<Dome> getDomes();

    Dome getDome(String identifier);

    List<Client> getClients();
    List<Client> getSubscribedClients();
    Client getClient(String identifier);

    Vehicle updateVehicleLocation(String identifier, Location location);
    Vehicle updateVehicleStatus(String identifier, Boolean status);

    Client updateClientLocation(String identifier, Location location);



    List<Dispatch> getDispatches();
    Dispatch getDispatch(String identifier);
    void deleteDispatch(String identifier);
    Dispatch addDispatch(String identifier, String source_type, String destination_type, String source_identifier, String destination_identifier);
}
