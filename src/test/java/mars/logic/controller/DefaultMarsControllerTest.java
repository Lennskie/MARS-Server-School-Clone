package mars.logic.controller;

import mars.logic.data.Repositories;
import mars.logic.domain.*;
import mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9001));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getSubscriptions() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        List<Subscription> subscriptions = sut.getSubscriptions();

        //Assert
        assertEquals(3, subscriptions.size());
    }

    @Test
    void getVehiclesAmount() {
        MarsController sut = new DefaultMarsController();

        List<Vehicle> vehicles = sut.getVehicles();

        assertEquals(3, vehicles.size());
    }

    @Test
    void getVehicleFirst() {
        MarsController sut = new DefaultMarsController();

        Vehicle vehicle = sut.getVehicle("AV-001");

        assertTrue(vehicle != null && StringUtils.isNoneBlank(vehicle.getIdentifier()));
    }

    @Test
    void getClients() {
        MarsController sut = new DefaultMarsController();

        List<Client> clients = sut.getClients();

        // Assert show all Clients
        assertEquals(6, clients.size());

        // Assert Client attributes
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getIdentifier()));
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getFirstname()));
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getLastname()));

        // Assert Subscription attributes
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getSubscription().getName()));
        assertTrue(StringUtils.isNoneBlank(clients.get(1).getSubscription().getName()));
        assertTrue(StringUtils.isNoneBlank(clients.get(2).getSubscription().getName()));
        assertNotNull(clients.get(3).getSubscription().getEndDate());
        assertTrue(clients.get(4).getSubscription().isReimbursed());
        assertNull(clients.get(5).getSubscription());
    }

    @Test
    void getSubscribedClients() {
        MarsController sut = new DefaultMarsController();

        List<Client> clients = sut.getSubscribedClients();

        // Assert only show Clients with an active subscription
        assertEquals(3, clients.size());

        // Assert Client attributes
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getIdentifier()));
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getFirstname()));
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getLastname()));

        // Assert Subscription attributes
        assertTrue(StringUtils.isNoneBlank(clients.get(0).getSubscription().getName()));
    }

    @Test
    void getClient() {
        MarsController sut = new DefaultMarsController();

        Client client = sut.getClient("MARS-ID-001");

        assertTrue(client != null && StringUtils.isNoneBlank(client.getIdentifier()));
        assertTrue(StringUtils.isNoneBlank(client.getIdentifier()));
        assertTrue(StringUtils.isNoneBlank(client.getFirstname()));
        assertTrue(StringUtils.isNoneBlank(client.getLastname()));
        assertTrue(StringUtils.isNoneBlank(client.getSubscription().getName()));
    }

    @Test
    void getDomesAmount() {
        MarsController sut = new DefaultMarsController();

        List<Dome> domes = sut.getDomes();

        assertEquals(10, domes.size());
    }

    @Test
    void getDomeFirst() {
        MarsController sut = new DefaultMarsController();

        Dome dome = sut.getDome("DOME-001");

        assertTrue(dome != null && StringUtils.isNoneBlank(dome.getIdentifier()));
    }

    @Test
    void getDangerzones() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Dangerzone dangerzone = sut.getDangerzones(new Dangerzone(new Location(2.33, 2.33),4));

        //Assert
        assertNotNull(dangerzone);
    }
}
