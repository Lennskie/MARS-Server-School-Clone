package mars.web.bridge;

import mars.logic.controller.DefaultMarsController;
import mars.logic.controller.MarsControllerListener;
import mars.logic.controller.MarsController;
import mars.logic.data.Repositories;
import mars.logic.domain.*;
import mars.logic.domain.util.RandomLocationGenerator;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MarsRtcBridge implements MarsControllerListener {

    private static final String NEW_CLIENT_EVENT_BUS = "new.client";
    private static final String NEW_VEHICLE_EVENT_BUS = "new.vehicle";
    private static final String NEW_DISPATCH_EVENT_BUS = "new.dispatch";

    private static final String DELETED_DISPATCH_EVENT_BUS = "delete.dispatch";

    // @TODO REMOVE IF UNUSED
    @SuppressWarnings("unused")
    private static final String CLIENT_STATUS_EVENT_BUS = "status.client";
    @SuppressWarnings("unused")
    private static final String VEHICLE_STATUS_EVENT_BUS = "status.vehicle";

    private static final String CLIENT_LOCATION_EVENT_BUS = "location.client";
    private static final String VEHICLE_LOCATION_EVENT_BUS = "location.vehicle";

    private MarsController marsController;
    private SockJSHandler sockJSHandler;
    private EventBus eb;

    public MarsRtcBridge() {
        this(new DefaultMarsController());
    }

    public MarsRtcBridge(MarsController marsController) {
        this.setMarsController(marsController);
    }

    private void MockCalls() {
        List<Client> clients = Repositories.getClientsRepo().getClients();
        List<Vehicle> vehicles = Repositories.getVehiclesRepo().getVehicles();

        // @TODO REMOVE IF UNUSED
        // Domes don't move
        @SuppressWarnings("unused")
        List<Dome> domes = Repositories.getDomesRepo().getDomes();


        Timer movementTimer = new Timer();
        TimerTask movementTimerTask = new TimerTask() {
            @Override
            public void run() {
                clients.forEach(client -> marsController.updateClientLocation(client.getIdentifier(), RandomLocationGenerator.getRandomLocation()));

                vehicles.forEach(vehicle -> marsController.updateVehicleLocation(vehicle.getIdentifier(), RandomLocationGenerator.getRandomLocation()));
            }
        };
        movementTimer.schedule(movementTimerTask, 0, 5000);
    }

    // @TODO REMOVE IF UNUSED
    @SuppressWarnings("unused")
    public void publishNewClient(Client newClient) {
        eb.publish(NEW_CLIENT_EVENT_BUS, JsonObject.mapFrom(newClient));
    }

    // @TODO REMOVE IF UNUSED
    @SuppressWarnings("unused")
    public void publishNewVehicle(Vehicle newVehicle) {
        eb.publish(NEW_VEHICLE_EVENT_BUS, JsonObject.mapFrom(newVehicle));
    }

    public void publishMovedClient(Client movedClient) {
        eb.publish(CLIENT_LOCATION_EVENT_BUS, JsonObject.mapFrom(movedClient));
    }

    public void publishMovedVehicle(Vehicle movedVehicle) {
        eb.publish(VEHICLE_LOCATION_EVENT_BUS, JsonObject.mapFrom(movedVehicle));
    }

    public void publishNewDispatch(Dispatch newDispatch) {
        eb.publish(NEW_DISPATCH_EVENT_BUS, JsonObject.mapFrom(newDispatch));
    }

    public void publishDeletedDispatch(Dispatch deletedDispatch) {
        eb.publish(DELETED_DISPATCH_EVENT_BUS, JsonObject.mapFrom(deletedDispatch));
    }

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex(".+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
    }

    public SockJSHandler getSockJSHandler(Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
        eb = vertx.eventBus();
        createSockJSHandler();

        MockCalls();

        return sockJSHandler;
    }

    public void setMarsController(MarsController marsController) {
        // DP: Observer pattern (Register this bridge by the controller as listener)
        marsController.addListener(this);
        this.marsController = marsController;
    }

    @Override
    public void onDispatchAdded(Dispatch dispatch) {
        publishNewDispatch(dispatch);
    }

    @Override
    public void onDispatchDeleted(Dispatch dispatch) {
        publishDeletedDispatch(dispatch);
    }

    @Override
    public void onVehicleMoved(Vehicle vehicle) {
        publishMovedVehicle(vehicle);
    }

    @Override
    public void onClientMoved(Client client) {
        publishMovedClient(client);
    }
}
