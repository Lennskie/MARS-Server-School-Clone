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

/**
 * In the MarsRtcBridge class you will find one example function which sends a message on the message bus to the client.
 * The RTC bridge is one of the class taught topics.
 * If you do not choose the RTC topic you don't have to do anything with this class.
 * Otherwise, you will need to expand this bridge with the websockets topics shown in the other modules.
 *
 * Compared to the other classes only the bare minimum is given.
 * The client-side starter project does not contain any teacher code about the RTC topic.
 * The rtc bridge is already initialized and configured in the WebServer.java.
 * No need to change the WebServer.java
 *
 * As a proof of concept (poc) one message to the client is sent every 30 seconds.
 *
 * The job of the "bridge" is to bridge between websockets events and Java (the controller).
 * Just like in the openapi bridge, keep business logic isolated in the package logic.
 * <p>
 */
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
