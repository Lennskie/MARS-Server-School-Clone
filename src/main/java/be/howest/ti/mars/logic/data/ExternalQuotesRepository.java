package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.vertx.core.Future;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is an example of an external repository consuming an api.
 * In Project II it is encouraged to connect with external api's or api's provided by other teams!
 * <p>
 * This class consumes an external random quotes api.
 * See https://goquotes-api.herokuapp.com/api/v1/random?count=1
 * See SOA&SD for another example.
 */
public class ExternalQuotesRepository {
    private static final int DEFAULT_PORT = 443;
    private static final String DEFAULT_HOST = "frightening-warlock-37692.herokuapp.com";
    private static final String DEFAULT_API_URI = "/api/v1/random";
    private static final Logger LOGGER = Logger.getLogger(ExternalQuotesRepository.class.getName());

    private final WebClient webClient;
    private final String host;
    private final String apiUri;
    private final int port;
    private final boolean useSsl;

    public ExternalQuotesRepository(WebClient webClient) {
        this(webClient, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_API_URI, true);
    }

    public ExternalQuotesRepository(WebClient webClient, String host, int port, String apiUri, boolean useSsl) {
        if (webClient == null) {
            LOGGER.log(Level.SEVERE, "RandomQuotesClient is not configured");
            throw new RepositoryException("RandomQuotesClient is not configured");
        }
        this.webClient = webClient;
        this.host = host;
        this.port = port;
        this.apiUri = apiUri;
        this.useSsl = useSsl;
    }

    /*
    Example of how to consume an external api.
     */
    public Future<String> getRandomQuote() {
        return webClient.get(port, host, apiUri)
                .ssl(useSsl)
                .addQueryParam("count", "1")
                .as(BodyCodec.jsonObject())
                .send()
                .map(HttpResponse::body)
                .map(json -> json.getJsonArray("quotes").getJsonObject(0).getString("text"));
    }
}
