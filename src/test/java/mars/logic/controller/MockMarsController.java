package mars.logic.controller;

import mars.logic.domain.Dangerzone;
import mars.logic.domain.Location;
import mars.logic.domain.Quote;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import mars.logic.domain.Subscription;

import java.util.ArrayList;
import java.util.List;

public class MockMarsController implements MarsController {
    private static final String SOME_QUOTE = "quote";

    @Override
    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription("Silver", "Short description.", 500.00));
        subscriptions.add(new Subscription("Gold", "Short description.", 750.00));
        subscriptions.add(new Subscription("Platinum", "Short description.", 1000.00));
        return subscriptions;
    }

    @Override
    public Quote getQuote(int quoteId) {
        return new Quote(quoteId, SOME_QUOTE);
    }

    @Override
    public Quote createQuote(String quote) {
        return new Quote(1, quote);
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        return new Quote(quoteId, quote);
    }

    @Override
    public void deleteQuote(int quoteId) {
    }

    @Override
    public Future<Quote> getRandomQuote() {
        Promise<Quote> promise = Promise.promise();
        promise.complete(new Quote(1, SOME_QUOTE));
        return promise.future();
    }

    @Override
    public Dangerzone getDangerzones(Dangerzone dangerzones) {
        return new Dangerzone(new Location(2.33, 2.33),3);
    }

    @Override
    public void addListener(MarsControllerListener listener) {
        // Boilerplate
    }
}
