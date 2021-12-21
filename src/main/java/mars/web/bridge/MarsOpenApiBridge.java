package mars.web.bridge;

import io.vertx.core.json.JsonObject;
import mars.logic.controller.DefaultMarsController;
import mars.logic.controller.MarsController;
import mars.logic.domain.Client;
import mars.logic.domain.Dangerzone;
import mars.logic.domain.Subscription;
import mars.logic.domain.Vehicle;
import mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will find one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class,
 * pass it to the controller and use its result to generate a response in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;
    public static final String SPEC_SUBSCRIPTIONS = "subscriptions";
    public static final String SPEC_VEHICLES = "vehicles";
    public static final String SPEC_CLIENTS = "clients";
    public static final String SPEC_SUBSCRIBED_CLIENTS = "subscribedClients";

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void getDangerzones(RoutingContext ctx) {
        Dangerzone dangerzones = controller.getDangerzones(Request.from(ctx).getDangerzones());
        Response.sendDangerzones(ctx, dangerzones);
    }

    public void getSubscriptions(RoutingContext ctx) {
        List<Subscription> subscriptions = controller.getSubscriptions();
        Response.sendSubscriptions(ctx, new JsonObject().put(SPEC_SUBSCRIPTIONS, subscriptions));
    }

    public void getVehicles(RoutingContext ctx) {
        List<Vehicle> vehicles = controller.getVehicles();
        Response.sendVehicles(ctx, new JsonObject().put(SPEC_VEHICLES, vehicles));
    }

    public void getVehicle(RoutingContext ctx) {
        Vehicle vehicle = controller.getVehicle(Request.from(ctx).getVehicleId());
        Response.sendVehicle(ctx, JsonObject.mapFrom(vehicle));
    }

    public void getClients(RoutingContext ctx) {
        List<Client> clients = controller.getClients();
        Response.sendClients(ctx, new JsonObject().put(SPEC_CLIENTS, clients));
    }

    public void getSubscribedClients(RoutingContext ctx) {
        List<Client> subscribedClients = controller.getSubscribedClients();
        Response.sendClients(ctx, new JsonObject().put(SPEC_SUBSCRIBED_CLIENTS, subscribedClients));
    }

    public void getClient(RoutingContext ctx) {
        Client client = controller.getClient(Request.from(ctx).getClientId());
        Response.sendClient(ctx, JsonObject.mapFrom(client));
    }

    /*
    Example of how to consume an external api.
     */
    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getDangerzones");
        routerBuilder.operation("getDangerzones").handler(this::getDangerzones);

        LOGGER.log(Level.INFO, "Installing handler for: getSubscriptions");
        routerBuilder.operation("getSubscriptions").handler(this::getSubscriptions);

        LOGGER.log(Level.INFO, "Installing handler for: getVehicles");
        routerBuilder.operation("getVehicles").handler(this::getVehicles);

        LOGGER.log(Level.INFO, "Installing handler for: getVehicle");
        routerBuilder.operation("getVehicle").handler(this::getVehicle);

        LOGGER.log(Level.INFO, "Installing handler for: getClients");
        routerBuilder.operation("getClients").handler(this::getClients);

        LOGGER.log(Level.INFO, "Installing handler for: getSubscribedClients");
        routerBuilder.operation("getSubscribedClients").handler(this::getSubscribedClients);

        LOGGER.log(Level.INFO, "Installing handler for: getClient");
        routerBuilder.operation("getClient").handler(this::getClient);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        if (cause instanceof MarsResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
