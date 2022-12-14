package mars.web;

import mars.logic.controller.MockMarsController;
import mars.logic.data.Repositories;
import mars.web.bridge.MarsOpenApiBridge;
import mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
    void getSubscriptions(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/subscriptions/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(
                                    response.bodyAsJsonObject()
                                            .getJsonArray("subscriptions")
                                            .getJsonObject(0)
                                            .getString("name")
                            ), "Silver"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getVehicles(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/vehicles/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(
                                response.bodyAsJsonObject()
                                .getJsonArray("vehicles")
                                .getJsonObject(0)
                                .getString("identifier")
                            ), "AV-001"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getVehicle(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/vehicles/AV-001").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("identifier")),
                            "AV-001"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getClients(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/clients/").send()
            .onFailure(testContext::failNow)
            .onSuccess(response -> testContext.verify(() -> {
                assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                assertEquals(6, response.bodyAsJsonObject().getJsonArray("clients").size());
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("clients").getJsonObject(0).getString("identifier")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("clients").getJsonObject(0).getString("firstname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("clients").getJsonObject(0).getString("lastname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("clients").getJsonObject(0).getJsonObject("subscription").getString("name")));
                testContext.completeNow();
            }));
    }


    @Test
    void getSubscribedClients(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/clients/subscribed").send()
            .onFailure(testContext::failNow)
            .onSuccess(response -> testContext.verify(() -> {
                assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                assertEquals(3, response.bodyAsJsonObject().getJsonArray("subscribedClients").size());
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("subscribedClients").getJsonObject(0).getString("identifier")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("subscribedClients").getJsonObject(0).getString("firstname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("subscribedClients").getJsonObject(0).getString("lastname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonArray("subscribedClients").getJsonObject(0).getJsonObject("subscription").getString("name")));
                testContext.completeNow();
            }));
    }

    @Test
    void getClient(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/clients/MARS-ID-001").send()
            .onFailure(testContext::failNow)
            .onSuccess(response -> testContext.verify(() -> {
                assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getString("identifier")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getString("firstname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getString("lastname")));
                assertTrue(StringUtils.isNotBlank(response.bodyAsJsonObject().getJsonObject("subscription").getString("name")));
                testContext.completeNow();
            }));
    }

    @Test
    void getDomes(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/domes/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(
                                    response.bodyAsJsonObject()
                                            .getJsonArray("domes")
                                            .getJsonObject(0)
                                            .getString("identifier")
                            ), "DOME-001"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getDome(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/domes/DOME-001").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("identifier")),
                            "DOME-001"
                    );
                    testContext.completeNow();
                }));
    }
}