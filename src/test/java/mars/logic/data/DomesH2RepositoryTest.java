package mars.logic.data;

import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mars.logic.domain.Dome;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DomesH2RepositoryTest {
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
        Repositories.getDomesRepo().generateData();
    }

    @Test
    void getDome() {
        String identifier = "DOME-001";

        Dome dome = Repositories.getDomesRepo().getDome(identifier);

        Assertions.assertTrue(dome != null && !StringUtil.isNullOrEmpty(dome.getIdentifier()));
    }

    @Test
    void getDomes() {
        List<Dome> domes = Repositories.getDomesRepo().getDomes();

        Assertions.assertNotNull(domes);
        Assertions.assertEquals(10, domes.size());
    }
}
