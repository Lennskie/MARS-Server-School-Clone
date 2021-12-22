package mars.logic.controller;

import mars.logic.domain.*;
import io.vertx.core.Future;

import java.util.List;

public interface MarsController {

    List<Subscription> getSubscriptions();

    Quote getQuote(int quoteId);
    Quote createQuote(String quote);
    Quote updateQuote(int quoteId, String quote);
    void deleteQuote(int quoteId);
    Future<Quote> getRandomQuote();

    Dangerzone getDangerzones(Dangerzone dangerzones);

    void addListener(MarsControllerListener listener);

    List<Vehicle> getVehicles();

    Vehicle getVehicle(String identifier);

    List<Dome> getDomes();

    Dome getDome(String identifier);

    List<Client> getClients();
    List<Client> getSubscribedClients();
    Client getClient(String identifier);

    Vehicle updateVehicleLocation(String identifier, Location location);
    Vehicle updateVehicleStatus(String identifier, Integer status);
}
