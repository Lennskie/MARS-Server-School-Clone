package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class Repositories {
    private static MarsH2Repository h2Repo = null;
    private static ExternalQuotesRepository quotesRepo = null;

    private Repositories() {
    }

    public static MarsH2Repository getH2Repo() {
        if (quotesRepo == null)
            throw new RepositoryException("MarsH2Repository not configured.");

        return h2Repo;
    }

    public static ExternalQuotesRepository getQuotesRepo() {
        if (quotesRepo == null)
            throw new RepositoryException("ExternalQuotesRepository not configured.");

        return quotesRepo;
    }

    public static void configure(JsonObject dbProps, WebClient webClient) {
        h2Repo = new MarsH2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        quotesRepo = new ExternalQuotesRepository(webClient);
    }

    public static void configure(JsonObject dbProps, WebClient webClient, String host, int port, String apiUrl, boolean useSsl) {
        h2Repo = new MarsH2Repository(dbProps.getString("url"),
                dbProps.getString("username"),
                dbProps.getString("password"),
                dbProps.getInteger("webconsole.port"));
        quotesRepo = new ExternalQuotesRepository(webClient, host, port, apiUrl, useSsl);
    }

    public static void shutdown() {
        if (h2Repo != null)
            h2Repo.cleanUp();

        h2Repo = null;
        quotesRepo = null;
    }
}
