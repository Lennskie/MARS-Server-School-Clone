package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.net.ssl.SSLSocketFactory;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(VertxExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExternalQuotesRepositoryTest {
    private MockWebServer webServer;

    @BeforeAll
    void setupTestSuite() {
        webServer = new MockWebServer();
        webServer.useHttps((SSLSocketFactory) SSLSocketFactory.getDefault(), true);
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url","jdbc:h2:~/mars-db",
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories
                .configure(dbProperties, WebClient.create(Vertx.vertx()),"localhost", webServer.getPort(), "", false);
    }

    @Test
    void getRandomQuote(final VertxTestContext testContext) throws InterruptedException {
        // Arrange
        final JsonObject response =
                new JsonObject().put("quotes", new JsonArray().add(new JsonObject().put("text", "some quote")));
        webServer.enqueue(new MockResponse()
                .setBody(response.encode())
                .setResponseCode(HttpResponseStatus.OK.code())
                .setHeader("content-type", "application/json"));

        // Act
        Future<String> quote = Repositories.getQuotesRepo().getRandomQuote();

        // Assert
        final RecordedRequest recordedRequest = webServer.takeRequest();
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        quote
                .onSuccess(r -> testContext.verify(() -> {
                    assertEquals("some quote", r);
                    testContext.completeNow();
                }))
                .onFailure(testContext::failNow);
    }

    @Test
    void getRepoWithoutConfiguration() {
        //Arrange + Act + Assert
        assertThrows(RepositoryException.class, () -> new ExternalQuotesRepository(null));
    }
}
