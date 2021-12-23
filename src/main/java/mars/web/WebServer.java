package mars.web;

import mars.logic.controller.DefaultMarsController;
import mars.logic.controller.MarsController;
import mars.logic.controller.MarsControllerListener;
import mars.logic.data.Repositories;
import mars.web.bridge.MarsOpenApiBridge;
import mars.web.bridge.MarsRtcBridge;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * WebServer is the main Verticle:
 *  - loads configuration
 *  - loads api specification
 *  - configures a websocket connection
 *  - starts an http-webserver.
 *
 *  No need to update anything in this class unless you know what you are doing !!!
 */
public class WebServer extends AbstractVerticle {
    private static final String REALTIME_COMM_URI = "/events/*";
    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());
    private Promise<Void> startPromise;
    private final MarsOpenApiBridge openApiBridge;
    private final MarsRtcBridge rtcBridge;

    public WebServer(MarsOpenApiBridge bridge, MarsRtcBridge rtcBridge) {
        this.openApiBridge = bridge;
        this.rtcBridge = rtcBridge;

        // rtcBridge.setMarsController(openApiBridge.getMarsController());
    }

    public WebServer(MarsController marsController) {
        // DP: Constructor Chaining
        this(new MarsOpenApiBridge(marsController), new MarsRtcBridge(marsController));
    }

    public WebServer() {
        // DP: Constructor Chaining
        this(new DefaultMarsController());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        this.startPromise = startPromise;
        ConfigRetriever.create(vertx).getConfig()
                .onFailure(cause -> shutDown("Failed to load configuration", cause))
                .onSuccess(configuration -> {
                    LOGGER.log(Level.INFO, "Configuration loaded: {0}", configuration);
                    final int port = configuration.getJsonObject("http").getInteger("port");
                    LOGGER.log(Level.INFO, "Server will be listening at port {0}", port);

                    RouterBuilder.create(vertx, configuration.getJsonObject("api").getString("url"))
                            .onFailure(cause -> shutDown("Failed to load API specification", cause))
                            .onSuccess(routerBuilder -> {
                                LOGGER.log(Level.INFO, "API specification loaded: {0}",
                                        routerBuilder.getOpenAPI().getOpenAPI().getJsonObject("info").getString("version"));
                                Repositories.configure(configuration.getJsonObject("db"), WebClient.create(vertx) );

                                Router mainRouter = Router.router(vertx);
                                mainRouter.route().handler(createCorsHandler());
                                mainRouter.route(REALTIME_COMM_URI).handler(rtcBridge.getSockJSHandler(vertx));

                                Router openApiRouter = openApiBridge.buildRouter(routerBuilder);
                                mainRouter.mountSubRouter("/", openApiRouter);

                                vertx.createHttpServer()
                                        .requestHandler(mainRouter)
                                        .listen(port)
                                        .onFailure(cause -> shutDown("Failed to start server", cause))
                                        .onSuccess(server -> {
                                            LOGGER.log(Level.INFO, "Server is listening on port: {0}", server.actualPort());
                                            startPromise.complete();
                                        });
                            });
                });
    }

    private void shutDown(String quote, Throwable cause) {
        LOGGER.log(Level.SEVERE, quote, cause);
        LOGGER.info("Shutting down");
        vertx.close();
        startPromise.fail(cause);
    }

    @Override
    public void stop() {
        Repositories.shutdown();
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