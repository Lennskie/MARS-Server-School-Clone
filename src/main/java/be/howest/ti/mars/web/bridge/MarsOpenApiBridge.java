package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

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

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void getQuote(RoutingContext ctx) {
        Quote quote = controller.getQuote(Request.from(ctx).getQuoteId());

        Response.sendQuote(ctx, quote);
    }

    public void createQuote(RoutingContext ctx) {
        String quote = Request.from(ctx).getQuote();

        Response.sendQuoteCreated(ctx, controller.createQuote(quote));
    }

    public void updateQuote(RoutingContext ctx) {
        Request request = Request.from(ctx);
        String quoteValue = request.getQuote();
        int quoteId = request.getQuoteId();

        Response.sendQuoteUpdated(ctx, controller.updateQuote(quoteId, quoteValue));
    }

    public void deleteQuote(RoutingContext ctx) {
        int quoteId = Request.from(ctx).getQuoteId();

        controller.deleteQuote(quoteId);

        Response.sendQuoteDeleted(ctx);
    }

    /*
    Example of how to consume an external api.
     */
    public void randomQuote(RoutingContext ctx) {
        controller.getRandomQuote()
                .onSuccess(quote -> Response.sendQuote(ctx, quote))
                .onFailure(cause -> {
                    LOGGER.log(Level.WARNING, "Could not fetch random quote.", cause);
                    throw new MarsResourceNotFoundException("Could not fetch random quote.");
                });
    }

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getQuote");
        routerBuilder.operation("getQuote").handler(this::getQuote);

        LOGGER.log(Level.INFO, "Installing handler for: createQuote");
        routerBuilder.operation("createQuote").handler(this::createQuote);

        LOGGER.log(Level.INFO, "Installing handler for: updateQuote");
        routerBuilder.operation("updateQuote").handler(this::updateQuote);

        LOGGER.log(Level.INFO, "Installing handler for: deleteQuote");
        routerBuilder.operation("deleteQuote").handler(this::deleteQuote);

        LOGGER.log(Level.INFO, "Installing handler for: randomQuote");
        routerBuilder.operation("randomQuote").handler(this::randomQuote);

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
