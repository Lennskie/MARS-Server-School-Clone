package mars.logic.data;

import mars.logic.exceptions.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoriesTest {

    @BeforeEach
    void setupTest() {
        Repositories.shutdown();
    }

    @Test
    void getMarsExternalRepoWithoutConfiguration() {
        assertThrows(RepositoryException.class, Repositories::getMarsExternalRepo);
    }

    @Test
    void getH2RepoWithoutConfiguration() {
        assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }

    @Test
    void getSubscriptionsRepo() { //noinspection ALL
        assertTrue(Repositories.getSubscriptionsRepo() instanceof SubscriptionsRepository);
    }

    @Test
    void getVehiclesRepo() { //noinspection ALL
        assertTrue(Repositories.getVehiclesRepo() instanceof VehiclesRepository);
    }

    @Test
    void getClientsRepo() { //noinspection ALL
        assertTrue(Repositories.getClientsRepo() instanceof ClientsRepository);
    }
}
