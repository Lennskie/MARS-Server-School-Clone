package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";

    private Vertx vertx;
    private WebClient webClient;

    @BeforeEach
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(new MockMarsController()), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterEach
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    @Test
    void getQuote(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/dangerzones/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("latitude")),
                            "2"
                    );
                    testContext.completeNow();
                }));
    }
}