package mars.logic.data;

import mars.logic.exceptions.RepositoryException;
import io.vertx.ext.web.client.WebClient;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MarsExternalRepository {
    private static final int DEFAULT_PORT = 443;
    private static final String DEFAULT_HOST = "frightening-warlock-37692.herokuapp.com";
    private static final String DEFAULT_API_URI = "/api/v1/random";
    private static final Logger LOGGER = Logger.getLogger(MarsExternalRepository.class.getName());

    @SuppressWarnings("ALL")
    private final WebClient webClient;
    @SuppressWarnings("ALL")
    private final String host;
    @SuppressWarnings("ALL")
    private final String apiUri;
    @SuppressWarnings("ALL")
    private final int port;
    @SuppressWarnings("ALL")
    private final boolean useSsl;

    public MarsExternalRepository(WebClient webClient) {
        this(webClient, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_API_URI, true);
    }

    public MarsExternalRepository(WebClient webClient, String host, int port, String apiUri, boolean useSsl) {
        if (webClient == null) {
            LOGGER.log(Level.SEVERE, "RandomMarsClient is not configured");
            throw new RepositoryException("RandomMarsClient is not configured");
        }
        this.webClient = webClient;
        this.host = host;
        this.port = port;
        this.apiUri = apiUri;
        this.useSsl = useSsl;
    }
}
