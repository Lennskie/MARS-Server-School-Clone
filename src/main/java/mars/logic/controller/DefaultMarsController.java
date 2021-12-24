package mars.logic.controller;

import mars.logic.data.Repositories;
import mars.logic.domain.*;
import mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.Future;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * It should NOT be aware that it is used in the context of a webserver:
 * <p>
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Do not be afraid to create your own Java classes if needed.
 * <p>
 * Note: Json and JsonObject can (and should) be used in the web-package however.
 * <p>
 * (please update these comments in the final version)
 */
public class DefaultMarsController implements MarsController {
    private static final String MSG_QUOTE_ID_UNKNOWN = "No quote with id: %d";
    // DP: Observer Pattern Keep a list of all listeners
    private List<MarsControllerListener> listeners = new LinkedList<>();

    @Override
    public List<Subscription> getSubscriptions() {
        return Repositories.getSubscriptionsRepo().getSubscriptions();
    }

    @Override
    public List<Dangerzone> getDangerzones() {
        return Repositories.getDangerRepo().getDangerzones();
    }

    @Override
    public void addListener(MarsControllerListener listener) {
        // DP: Observer Pattern Add listeners to get notified
        this.listeners.add(listener);
    }

    public List<Vehicle> getVehicles() {
        return Repositories.getVehiclesRepo().getVehicles();
    }

    @Override
    public Vehicle getVehicle(String identifier) {
        Vehicle vehicle = Repositories.getVehiclesRepo().getVehicle(identifier);
        if (null == vehicle)
            throw new MarsResourceNotFoundException(identifier);
        return vehicle;
    }

    @Override
    public List<Dome> getDomes() {
        return Repositories.getDomesRepo().getDomes();
    }

    @Override
    public Dome getDome(String identifier) {
        Dome dome = Repositories.getDomesRepo().getDome(identifier);
        if (null == dome)
            throw new MarsResourceNotFoundException(identifier);

        return dome;
    }

    @Override
    public List<Client> getClients() {
        return Repositories.getClientsRepo().getClients();
    }

    @Override
    public List<Client> getSubscribedClients() {
        return Repositories.getClientsRepo().getSubscribedClients();
    }

    @Override
    public Client getClient(String identifier) {
        Client client = Repositories.getClientsRepo().getClient(identifier);
        if (null == client)
            throw new MarsResourceNotFoundException(identifier);

        return client;
    }

    public Vehicle updateVehicleLocation(String identifier, Location location) {
        Vehicle vehicle = Repositories.getVehiclesRepo().updateVehicleLocation(identifier, location);
        if (null == vehicle)
            throw new MarsResourceNotFoundException(identifier);
        fireVehicleMoved(vehicle);
        return vehicle;
    }

    public Vehicle updateVehicleStatus(String identifier, Boolean status){
        Vehicle vehicle = Repositories.getVehiclesRepo().updateVehicleStatus(identifier,status);
        if (null == vehicle)
            throw new MarsResourceNotFoundException(identifier);
        return vehicle;
    }

    public Client updateClientLocation(String identifier, Location location) {
        Client client = Repositories.getClientsRepo().updateClientLocation(identifier, location);
        if (null == client)
            throw new MarsResourceNotFoundException(identifier);
        fireClientMoved(client);
        return client;
    }

    @Override
    public List<Dispatch> getDispatches() {
        return Repositories.getDispatchesRepository().getDispatches();
    }

    @Override
    public Dispatch getDispatch(String identifier) {
        return Repositories.getDispatchesRepository().getDispatch(identifier);
    }

    @Override
    public void deleteDispatch(String identifier) {
        if (null == Repositories.getDispatchesRepository().getDispatch(identifier)) {
            throw new MarsResourceNotFoundException("No valid dispatch Identifier provided");
        }

        fireDispatchDeleted(Repositories.getDispatchesRepository().getDispatch(identifier));
        Repositories.getDispatchesRepository().deleteDispatch(identifier);
    }

    @Override
    public Dispatch addDispatch(String identifier, String sourceType, String destinationType, String sourceIdentifier, String destinationIdentifier) {
        if (StringUtils.isBlank(identifier)) {
            throw new IllegalArgumentException("Identifier is not allowed to be blank");
        }

        if (null == sourceType || null == destinationType || null == sourceIdentifier || null == destinationIdentifier) {
            throw new IllegalArgumentException("sourceType, destinationType, sourceIdentifier, destinationIdentifier are all required fields");
        }

        Dispatch dispatch = Repositories.getDispatchesRepository().addDispatch(identifier, sourceType, destinationType, sourceIdentifier, destinationIdentifier);
        fireDispatchAdded(dispatch);
        return dispatch;
    }

    private void fireDispatchAdded(Dispatch dispatch) {
        listeners.forEach(listener -> listener.onDispatchAdded(dispatch));
    }

    private void fireDispatchDeleted(Dispatch dispatch) {
        listeners.forEach(listener -> listener.onDispatchDeleted(dispatch));

    }

    private void fireClientMoved(Client client) {
        listeners.forEach(listener -> listener.onClientMoved(client));
    }

    private void fireVehicleMoved(Vehicle vehicle) {
        listeners.forEach(listener -> listener.onVehicleMoved(vehicle));
    }
}