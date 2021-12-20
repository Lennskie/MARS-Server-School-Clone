package mars.web.bridge;

import mars.logic.controller.DefaultMarsController;
import mars.logic.controller.MarsController;
import mars.logic.domain.*;
import mars.logic.domain.util.RandomLocationGenerator;
import io.swagger.v3.core.util.Json;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.util.*;

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
public class MarsRtcBridge {

    private static final MarsController marsController = new DefaultMarsController();

    private final String NEW_CLIENT_EVENT_BUS = "new.client";
    private final String NEW_VEHICLE_EVENT_BUS = "new.vehicle";

    private final String CLIENT_STATUS_EVENT_BUS = "status.client";
    private final String VEHICLE_STATUS_EVENT_BUS = "status.vehicle";

    private final String CLIENT_LOCATION_EVENT_BUS = "location.client";
    private final String VEHICLE_LOCATION_EVENT_BUS = "location.vehicle";

    private SockJSHandler sockJSHandler;
    private EventBus eb;


    // Mockcalls is not intended to stay - it is currently just a "filler" to test and get the rest going
    private void MockCalls() {
        // "1" Is arbitrary here, it's mocking
        Subscription dummyClientSubscription = marsController.getSubscriptions().get(1);
        // This client should come from repository as well, this is WIP.
        Client mockClient = new Client("Dummy", "User", RandomLocationGenerator.getRandomLocation(), new VitalStatus(), dummyClientSubscription, "1");
        // Same for the vehicle, repository is WIP
        Vehicle mockVehicle = new Vehicle("V1", RandomLocationGenerator.getRandomLocation());


        Timer newClientTimer = new Timer();
        Timer newVehicleTimer = new Timer();

        TimerTask newClientTimerTask = new TimerTask() {
            @Override
            public void run() {
                publishNewClient(mockClient);
                mockClient.setLocation(RandomLocationGenerator.getRandomLocation());
            }
        };

        TimerTask newVehicleTimerTask = new TimerTask() {
            @Override
            public void run() {
                publishNewVehicle(mockVehicle);
                mockVehicle.setLocation(RandomLocationGenerator.getRandomLocation());
            }
        };

        newClientTimer.schedule(newClientTimerTask, 0, 3000);
        newVehicleTimer.schedule(newVehicleTimerTask, 0, 3000);
    }

    public void publishNewClient(Client newClient) {
        eb.publish(NEW_CLIENT_EVENT_BUS, JsonObject.mapFrom(newClient));
    }

    public void publishNewVehicle(Vehicle newVehicle) {
        eb.publish(NEW_VEHICLE_EVENT_BUS, JsonObject.mapFrom(newVehicle));
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
}
