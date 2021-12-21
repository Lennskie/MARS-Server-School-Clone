package mars.logic.data;

import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import mars.logic.domain.Vehicle;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehiclesH2RepositoryTest {
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
        Repositories.getVehiclesRepo().generateData();
    }

    @Test
    void getVehicle() {
        String identifier = "AV-001";

        Vehicle vehicle = Repositories.getVehiclesRepo().getVehicle(identifier);

        Assertions.assertTrue(vehicle != null && !StringUtil.isNullOrEmpty(vehicle.getIdentifier()));
    }

    @Test
    void getVehicles() {
        List<Vehicle> vehicles = Repositories.getVehiclesRepo().getVehicles();

        Assertions.assertNotNull(vehicles);
        Assertions.assertEquals(3, vehicles.size());
    }
}
