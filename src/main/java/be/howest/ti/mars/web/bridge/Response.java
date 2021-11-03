package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Quote;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendQuote(RoutingContext ctx, Quote quote) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(quote));
    }

    public static void sendQuoteCreated(RoutingContext ctx, Quote quote) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(quote));
    }

    public static void sendQuoteDeleted(RoutingContext ctx) {
        sendEmptyResponse(ctx, 204);
    }

    public static void sendQuoteUpdated(RoutingContext ctx, Quote quote) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(quote));
    }

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

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }
}
