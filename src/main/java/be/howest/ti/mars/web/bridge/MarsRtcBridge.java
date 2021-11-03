package be.howest.ti.mars.web.bridge;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.Map;
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
public class MarsRtcBridge {
    private static final String EB_EVENT_TO_MARTIANS = "events.to.martians";
    private SockJSHandler sockJSHandler;
    private EventBus eb;

    /**
     * Example function to put a message on the event bus every 10 seconds.
     * The timer logic is only there to simulate a repetitive stream of data as an example.
     * Please remove this timer logic or move it to an appropriate place.
     * Please call the controller to get some business logic data. Afterwords publish the result to the client.
     */
    public void sendEventToClients() {
        final Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            public void run() {
                eb.publish(EB_EVENT_TO_MARTIANS, new JsonObject(Map.of("MyJsonProp", "some value")));
            }
        };

        timer.schedule(task, 0, 30000);
    }

    private void createSockJSHandler() {
        final PermittedOptions permittedOptions = new PermittedOptions().setAddressRegex("events\\..+");
        final SockJSBridgeOptions options = new SockJSBridgeOptions()
                .addInboundPermitted(permittedOptions)
                .addOutboundPermitted(permittedOptions);
        sockJSHandler.bridge(options);
    }

    public SockJSHandler getSockJSHandler(Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
        eb = vertx.eventBus();
        createSockJSHandler();

        // This is for demo purposes only.
        // Do not send messages in this getSockJSHandler function.
        sendEventToClients();

        return sockJSHandler;
    }
}
