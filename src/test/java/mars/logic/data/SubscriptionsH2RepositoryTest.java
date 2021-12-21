package mars.logic.data;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mars.logic.domain.Subscription;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionsH2RepositoryTest {
    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getSubscriptionsRepo().generateData();
    }

    @Test
    void getAllSubscriptions() {
        List<Subscription> subscriptions = Repositories.getSubscriptionsRepo().getSubscriptions();

        Assertions.assertEquals(3, subscriptions.size());
    }
}
