package mars.logic.controller;

import mars.logic.data.Repositories;
import mars.logic.domain.Dangerzone;
import mars.logic.domain.Quote;
import mars.logic.domain.Subscription;
import mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.Future;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * It should NOT be aware that it is used in the context of a webserver:
 * <p>
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Do not be afraid to create your own Java classes if needed.
 * <p>
 * Note: Json and JsonObject can (and should) be used in the web-package however.
 * <p>
 * (please update these comments in the final version)
 */
public class DefaultMarsController implements MarsController {
    private static final String MSG_QUOTE_ID_UNKNOWN = "No quote with id: %d";
    private List<MarsControllerListener> listeners;

    @Override
    public List<Subscription> getSubscriptions() {
        return Repositories.getH2Repo().getSubscriptions();
    }

    @Override
    public Quote getQuote(int quoteId) {
        Quote quote = Repositories.getH2Repo().getQuote(quoteId);
        if (null == quote)
            throw new MarsResourceNotFoundException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        return quote;
    }

    @Override
    public Quote createQuote(String quote) {
        if (StringUtils.isBlank(quote))
            throw new IllegalArgumentException("An empty quote is not allowed.");

        Quote returnQuote = Repositories.getH2Repo().insertQuote(quote);
        fireQuoteCreated(returnQuote);
        return returnQuote;
    }

    private void fireQuoteCreated(Quote quote) {
        listeners.forEach(marsControllerListener -> marsControllerListener.onQuoteCreated(quote));
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        if (StringUtils.isBlank(quote))
            throw new IllegalArgumentException("No quote provided!");

        if (quoteId < 0)
            throw new IllegalArgumentException("No valid quote ID provided");

        if (null == Repositories.getH2Repo().getQuote(quoteId))
            throw new MarsResourceNotFoundException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        return Repositories.getH2Repo().updateQuote(quoteId, quote);
    }

    @Override
    public void deleteQuote(int quoteId) {
        if (null == Repositories.getH2Repo().getQuote(quoteId))
            throw new MarsResourceNotFoundException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        Repositories.getH2Repo().deleteQuote(quoteId);
    }

    /*
    Example of how to consume an external api.
    See the openapi bridge class for an example of how to handle the Future<String> object.
     */
    @Override
    public Future<Quote> getRandomQuote() {
        return Repositories
                .getQuotesRepo()
                .getRandomQuote()
                .map(this::createQuote);
    }

    @Override
    public Dangerzone getDangerzones(Dangerzone dangerzones) {
        return dangerzones;
    }

    @Override
    public void addListener(MarsControllerListener listener) {
        this.listeners.add(listener);
    }
}