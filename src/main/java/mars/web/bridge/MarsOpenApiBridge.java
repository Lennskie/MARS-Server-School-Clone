package mars.web.bridge;

import io.vertx.core.json.JsonObject;
import mars.logic.controller.DefaultMarsController;
import mars.logic.controller.MarsController;
import mars.logic.domain.*;
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

public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;
    public static final String SPEC_SUBSCRIPTIONS = "subscriptions";
    public static final String SPEC_DOMES = "domes";
    public static final String SPEC_VEHICLES = "vehicles";
    public static final String SPEC_CLIENTS = "clients";
    public static final String SPEC_SUBSCRIBED_CLIENTS = "subscribedClients";
    public static final String SPEC_DANGERZONES = "dangerzones";
    public static final String SPEC_DISPATCHES = "dispatches";
    public static final String SPEC_DISPATCH = "dispatch";

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void getDangerzones(RoutingContext ctx) {
        List<Dangerzone> dangerzones = controller.getDangerzones();
        Response.sendDangerzones(ctx, new JsonObject().put(SPEC_DANGERZONES, dangerzones));
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

    public void getDomes(RoutingContext ctx) {
        List<Dome> domes = controller.getDomes();
        Response.sendDomes(ctx, new JsonObject().put(SPEC_DOMES, domes));
    }

    public void getDome(RoutingContext ctx) {
        Dome dome = controller.getDome(Request.from(ctx).getDomeId());
        Response.sendDome(ctx, JsonObject.mapFrom(dome));
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

    public void getDispatches(RoutingContext ctx) {
        List<Dispatch> dispatches = controller.getDispatches();
        Response.sendDispatches(ctx, new JsonObject().put(SPEC_DISPATCHES, dispatches));
    }

    public void getDispatch(RoutingContext ctx) {
        Dispatch dispatch = controller.getDispatch(Request.from(ctx).getIdentifier());
        Response.sendDispatches(ctx, new JsonObject().put(SPEC_DISPATCH, dispatch));
    }

    public void deleteDispatch(RoutingContext ctx) {
        controller.deleteDispatch(Request.from(ctx).getIdentifier());
        Response.sendOK(ctx);
    }

    public void addDispatch(RoutingContext ctx) {
        Dispatch dispatch = controller.addDispatch(
                Request.from(ctx).getBodyIdentifier(),
                Request.from(ctx).getType("source"),
                Request.from(ctx).getType("destination"),
                Request.from(ctx).getIdentifier("source"),
                Request.from(ctx).getIdentifier("destination")
        );
        Response.sendDispatches(ctx, new JsonObject().put(SPEC_DISPATCH, dispatch));
    }

    public void updateVehicleLocation(RoutingContext ctx){
        Vehicle vehicle = controller.updateVehicleLocation(Request.from(ctx).getVehicleId(), Request.from(ctx).getVehicleLocation());
        Response.sendClient(ctx, JsonObject.mapFrom(vehicle));
    }

    public void updateVehicleStatus(RoutingContext ctx){
        Vehicle vehicle = controller.updateVehicleStatus(Request.from(ctx).getVehicleId(), Request.from(ctx).getVehicleStatus());
        Response.sendClient(ctx, JsonObject.mapFrom(vehicle));
    }
    public void updateClientLocation(RoutingContext ctx){
        Client client = controller.updateClientLocation(Request.from(ctx).getClientId(), Request.from(ctx).getClientLocation());
        Response.sendClient(ctx, JsonObject.mapFrom(client));
    }

    public void getOverview(RoutingContext ctx) {
        JsonObject overview = new JsonObject();

        List<Vehicle> vehicles = controller.getVehicles();
        List<Client> clients = controller.getClients();
        List<Dome> domes = controller.getDomes();
        List<Dispatch> dispatches = controller.getDispatches();


        overview.put(SPEC_VEHICLES, vehicles);
        overview.put(SPEC_CLIENTS, clients);
        overview.put(SPEC_DOMES, domes);

        overview.put(SPEC_DISPATCHES, dispatches);

        Response.sendOverview(ctx, overview);
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

        LOGGER.log(Level.INFO, "Installing handler for: getDomes");
        routerBuilder.operation("getDomes").handler(this::getDomes);

        LOGGER.log(Level.INFO, "Installing handler for: getDome");
        routerBuilder.operation("getDome").handler(this::getDome);

        LOGGER.log(Level.INFO, "Installing handler for: getClients");
        routerBuilder.operation("getClients").handler(this::getClients);

        LOGGER.log(Level.INFO, "Installing handler for: getSubscribedClients");
        routerBuilder.operation("getSubscribedClients").handler(this::getSubscribedClients);

        LOGGER.log(Level.INFO, "Installing handler for: getClient");
        routerBuilder.operation("getClient").handler(this::getClient);

        LOGGER.log(Level.INFO, "Installing handler for: getOverview");
        routerBuilder.operation("getOverview").handler(this::getOverview);

        LOGGER.log(Level.INFO, "Installing handler for: updateVehicleLocation");
        routerBuilder.operation("updateVehicleLocation").handler(this::updateVehicleLocation);

        LOGGER.log(Level.INFO, "Installing handler for: updateVehicleStatus");
        routerBuilder.operation("updateVehicleStatus").handler(this::updateVehicleStatus);

        LOGGER.log(Level.INFO, "Installing handler for: updateClientLocation");
        routerBuilder.operation("updateClientLocation").handler(this::updateClientLocation);

        LOGGER.log(Level.INFO, "Installing handler for: getDispatch");
        routerBuilder.operation("getDispatch").handler(this::getDispatch);

        LOGGER.log(Level.INFO, "Installing handler for: getDispatches");
        routerBuilder.operation("getDispatches").handler(this::getDispatches);

        LOGGER.log(Level.INFO, "Installing handler for: deleteDispatch");
        routerBuilder.operation("deleteDispatch").handler(this::deleteDispatch);

        LOGGER.log(Level.INFO, "Installing handler for: addDispatch");
        routerBuilder.operation("addDispatch").handler(this::addDispatch);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }


    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String errorMessage = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        if (cause instanceof MarsResourceNotFoundException) {
            code = 404;
        } else if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, errorMessage);
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
