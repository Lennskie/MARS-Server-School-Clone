package mars.logic.controller;

import mars.logic.data.Repositories;
import mars.logic.domain.Dangerzone;
import mars.logic.domain.Location;
import mars.logic.domain.Quote;
import mars.logic.domain.Subscription;
import mars.logic.exceptions.MarsResourceNotFoundException;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:~/mars-db";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", URL,
                "username", "",
                "password", "",
                "webconsole.port", 9001));
        Repositories.configure(dbProperties, WebClient.create(Vertx.vertx()));
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getSubscriptions() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        List<Subscription> subscriptions = sut.getSubscriptions();

        //Assert
        assertEquals(3, subscriptions.size());
    }

    @Test
    void getQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.getQuote(0);

        //Assert
        assertTrue(quote != null && StringUtils.isNoneBlank(quote.getValue()));
    }

    @Test
    void getDangerzones() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Dangerzone dangerzone = sut.getDangerzones(new Dangerzone(new Location(2.33, 2.33),4));

        //Assert
        assertNotNull(dangerzone);
    }

    @Test
    void deleteQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        sut.deleteQuote(0);

        //Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.getQuote(0));
    }

    @Test
    void updateQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();
        Quote quote = sut.createQuote("some value");

        // Act
        Quote updatedQuoted = sut.updateQuote(quote.getId(), "new value");

        //Assert
        assertEquals("new value", updatedQuoted.getValue());
    }

    @Test
    void createQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.createQuote("new value");

        //Assert
        assertEquals("new value", quote.getValue());
    }

    @Test
    void getQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.getQuote(-1));
    }

    @Test
    void createQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.createQuote(""));
    }

    @Test
    void updateQuoteWithWrongIdThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(-1, "some quote"));
    }

    @Test
    void updateQuoteWithUnknownIdThrowsMarsResourceNotFoundException() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.updateQuote(1000, "some quote"));
    }

    @Test
    void updateQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(1, ""));
    }

    @Test
    void deleteQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(MarsResourceNotFoundException.class, () -> sut.deleteQuote(-1));
    }

}