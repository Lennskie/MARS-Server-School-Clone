package mars.logic.controller;

import mars.logic.domain.Quote;

public interface MarsControllerListener {
    void onQuoteCreated(Quote quote);
}
