package mars.logic.controller;

import mars.logic.domain.Dangerzone;
import mars.logic.domain.Quote;
import io.vertx.core.Future;

public interface MarsController {
    Quote getQuote(int quoteId);

    Quote createQuote(String quote);

    Quote updateQuote(int quoteId, String quote);

    void deleteQuote(int quoteId);

    Future<Quote> getRandomQuote();

    Dangerzone getDangerzones(Dangerzone dangerzones);
}
