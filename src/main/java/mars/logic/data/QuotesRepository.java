package mars.logic.data;

import mars.logic.domain.Quote;

public interface QuotesRepository {
    void generateData();
    Quote getQuote(int id);
    Quote insertQuote(String quoteValue);
    Quote updateQuote(int quoteId, String quote);
    void deleteQuote(int quoteId);
}
