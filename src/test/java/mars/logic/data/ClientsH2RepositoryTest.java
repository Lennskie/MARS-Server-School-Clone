package mars.logic.data;

import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mars.logic.domain.Client;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientsH2RepositoryTest {
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
        Repositories.getClientsRepo().generateData();
    }

    @Test
    void getClient() {
        String identifier = "MARS-ID-007";

        Client client = Repositories.getClientsRepo().getClient(identifier);

        Assertions.assertTrue(client != null && !StringUtil.isNullOrEmpty(client.getIdentifier()));
    }

    @Test
    void getClients() {
        List<Client> clients = Repositories.getClientsRepo().getClients();

        Assertions.assertNotNull(clients);
        Assertions.assertEquals(3, clients.size());
    }
}
