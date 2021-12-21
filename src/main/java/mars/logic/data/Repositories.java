package mars.logic.data;

import mars.logic.data.util.H2Repository;
import mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class Repositories {
    private static H2Repository h2Repo = null;
    private static QuotesExternalRepository quotesExternalRepo = null;
    private static final SubscriptionsRepository SUBSCRIPTIONS_REPO = new SubscriptionsH2Repository();
    private static final QuotesRepository QUOTES_REPO = new QuotesH2Repository();
    private static final VehiclesRepository VEHICLES_REPO = new VehiclesH2Repository();
    private static final DomesRepository DOMES_REPO = new DomesH2Repository();

    private Repositories() {}

    public static H2Repository getH2Repo() {
        if (h2Repo == null)
            throw new RepositoryException("H2Repository not configured.");

        return h2Repo;
    }

    public static QuotesExternalRepository getQuotesExternalRepo() {
        if (quotesExternalRepo == null)
            throw new RepositoryException("QuotesExternalRepository not configured.");

        return quotesExternalRepo;
    }

    public static SubscriptionsRepository getSubscriptionsRepo() {
        return SUBSCRIPTIONS_REPO;
    }

    public static QuotesRepository getQuotesRepo() {
        return QUOTES_REPO;
    }

    public static VehiclesRepository getVehiclesRepo() {
        return VEHICLES_REPO;
    }

    public static DomesRepository getDomesRepo() {
        return DOMES_REPO;
    }

    public static void configure(JsonObject dbProps, WebClient webClient) {
        h2Repo = new H2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        quotesExternalRepo = new QuotesExternalRepository(webClient);
    }

    public static void configure(JsonObject dbProps, WebClient webClient, String host, int port, String apiUrl, boolean useSsl) {
        h2Repo = new H2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        quotesExternalRepo = new QuotesExternalRepository(webClient, host, port, apiUrl, useSsl);
    }

    public static void shutdown() {
        if (h2Repo != null)
            h2Repo.cleanUp();

        h2Repo = null;
        quotesExternalRepo = null;
    }
}
