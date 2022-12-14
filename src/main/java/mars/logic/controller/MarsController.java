package mars.logic.controller;

import mars.logic.domain.*;
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
    Dispatch addDispatch(String identifier, String sourceType, String destinationType, String sourceIdentifier, String destinationIdentifier);
}
