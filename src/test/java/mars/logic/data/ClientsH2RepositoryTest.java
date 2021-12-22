package mars.logic.data;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mars.logic.domain.Client;
import org.apache.commons.lang3.StringUtils;
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
        String identifier = "MARS-ID-001";

        Client client = Repositories.getClientsRepo().getClient(identifier);

        Assertions.assertTrue(client != null && StringUtils.isNoneBlank(client.getIdentifier()));
        Assertions.assertTrue(StringUtils.isNoneBlank(client.getSubscription().getName()));
        Assertions.assertTrue(StringUtils.isNoneBlank(client.getFirstname()));
        Assertions.assertTrue(StringUtils.isNoneBlank(client.getLastname()));
    }

    @Test
    void getClients() {
        List<Client> clients = Repositories.getClientsRepo().getClients();

        Assertions.assertNotNull(clients);
        Assertions.assertEquals(6, clients.size());
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getIdentifier()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getFirstname()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getLastname()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getSubscription().getName()));
    }

    @Test
    void getSubscribedClients() {
        List<Client> clients = Repositories.getClientsRepo().getSubscribedClients();

        Assertions.assertNotNull(clients);
        Assertions.assertEquals(1, clients.size());
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getIdentifier()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getFirstname()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getLastname()));
        Assertions.assertTrue(StringUtils.isNoneBlank(clients.get(0).getSubscription().getName()));
    }
}
