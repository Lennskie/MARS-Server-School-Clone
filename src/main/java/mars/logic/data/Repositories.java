package mars.logic.data;

import mars.logic.data.util.H2Repository;
import mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class Repositories {
    private static H2Repository h2Repo = null;
    private static MarsExternalRepository marsExternalRepo = null;
    private static final SubscriptionsRepository SUBSCRIPTIONS_REPO = new SubscriptionsH2Repository();
    private static final VehiclesRepository VEHICLES_REPO = new VehiclesH2Repository();
    private static final DomesRepository DOMES_REPO = new DomesH2Repository();
    private static final ClientsRepository CLIENTS_REPO = new ClientsH2Repository();
    private static final DispatchesRepository DISPATCHES_REPOSITORY = new DispatchesH2Repository();
    private static final DangerzonesRepository DANGER_REPO = new DangerzoneH2Repository();

    private Repositories() {}

    public static H2Repository getH2Repo() {
        if (h2Repo == null)
            throw new RepositoryException("H2Repository not configured.");

        return h2Repo;
    }

    public static MarsExternalRepository getMarsExternalRepo() {
        if (marsExternalRepo == null)
            throw new RepositoryException("MarsExternalRepository not configured.");

        return marsExternalRepo;
    }

    public static SubscriptionsRepository getSubscriptionsRepo() {
        return SUBSCRIPTIONS_REPO;
    }

    public static VehiclesRepository getVehiclesRepo() { return VEHICLES_REPO; }

    public static DomesRepository getDomesRepo() {
        return DOMES_REPO;
    }

    public static ClientsRepository getClientsRepo() {
        return CLIENTS_REPO;
    }

    public static DispatchesRepository getDispatchesRepository() {
        return DISPATCHES_REPOSITORY;
    }


    public static DangerzonesRepository getDangerRepo() {
        return DANGER_REPO;
    }

    public static void configure(JsonObject dbProps, WebClient webClient) {
        h2Repo = new H2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        marsExternalRepo = new MarsExternalRepository(webClient);
    }

    public static void configure(JsonObject dbProps, WebClient webClient, String host, int port, String apiUrl, boolean useSsl) {
        h2Repo = new H2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        marsExternalRepo = new MarsExternalRepository(webClient, host, port, apiUrl, useSsl);
    }

    public static void shutdown() {
        if (h2Repo != null)
            h2Repo.cleanUp();

        h2Repo = null;
        marsExternalRepo = null;
    }
}
