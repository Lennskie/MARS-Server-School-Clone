package mars.web.bridge;

import mars.logic.domain.Dangerzone;
import mars.logic.domain.Quote;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import mars.logic.domain.Subscription;

import java.util.List;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendSubscriptions(RoutingContext ctx, JsonObject subscriptions) {
        // Lenn: JsonObject has been added here and in MarsOpenApiBridge > getSubscriptions
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(subscriptions));
    }

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

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendDangerzones(RoutingContext ctx, Dangerzone dangerzones) {
        sendJsonResponse(ctx, 200, JsonObject.mapFrom(dangerzones));
    }
}