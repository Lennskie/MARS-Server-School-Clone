package mars.logic.controller;

import mars.logic.domain.Quote;

public interface MarsControllerListener {
    // DP: Observer Pattern Actions to execute when the observed object (marsController) executes a specific action
    void onQuoteCreated(Quote quote);
}
