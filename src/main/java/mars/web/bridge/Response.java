package mars.web.bridge;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Response {

    private Response() { }

    public static void sendSubscriptions(RoutingContext ctx, JsonObject subscriptions) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(subscriptions));
    }

    public static void sendVehicles(RoutingContext ctx, JsonObject vehicles) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(vehicles));
    }

    public static void sendVehicle(RoutingContext ctx, JsonObject vehicle) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(vehicle));
    }

    public static void sendClients(RoutingContext ctx, JsonObject clients) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(clients));
    }

    public static void sendSubscribedClients(RoutingContext ctx, JsonObject subscribedClients) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(subscribedClients));
    }

    public static void sendClient(RoutingContext ctx, JsonObject client) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(client));
    }

    public static void sendDomes(RoutingContext ctx, JsonObject domes) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(domes));
    }

    public static void sendDome(RoutingContext ctx, JsonObject dome) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(dome));
    }

    public static void sendOverview(RoutingContext ctx, JsonObject overview) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(overview));
    }


    @SuppressWarnings("all")
    private static void sendEmptyResponse(RoutingContext ctx, int statusCode) {
        ctx.response()
                .setStatusCode(statusCode)
                .end();
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String errorMessage) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", errorMessage));
    }

    public static void sendDangerzones(RoutingContext ctx, JsonObject dangerzones) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(dangerzones));
    }

    public static void sendDispatches(RoutingContext ctx, JsonObject dispatches) {
        sendJsonResponse(ctx, 200, dispatches);
    }

    public static void sendOK(RoutingContext ctx) {
        sendEmptyResponse(ctx, 200);
    }
}
