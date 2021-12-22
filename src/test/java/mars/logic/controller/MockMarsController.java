package mars.logic.controller;

import mars.logic.domain.*;
import io.vertx.core.Future;
import io.vertx.core.Promise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle("AV-001"));
        vehicles.add(new Vehicle("AV-002"));
        vehicles.add(new Vehicle("AV-003"));

        return vehicles;
    }

    @Override
    public Vehicle getVehicle(String identifier) {
        return new Vehicle("AV-001");
    }

    @Override
    public List<Client> getClients() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Subscription subSilver = new Subscription("Silver", "Short description.", 500.00, formatter.format(date), null, false);
        Subscription subGold = new Subscription("Gold", "Short description.", 500.00, formatter.format(date), null, false);
        Subscription subPlat = new Subscription("Platinum", "Short description.", 500.00, formatter.format(date), null, false);
        Subscription subExpired = new Subscription("Silver", "Short description.", 500.00, formatter.format(date), formatter.format(date), false);
        Subscription subReimbursed = new Subscription("Silver", "Short description.", 500.00, formatter.format(date), formatter.format(date), true);

        List<Client> clients = new ArrayList<>();
        clients.add(new Client("MARS-ID-001", "Ana", "'Silveneyer'", subSilver, null, null));
        clients.add(new Client("MARS-ID-002", "Bob", "'Goldeneyer'", subGold, null, null));
        clients.add(new Client("MARS-ID-003", "Carolina", "'Platineyer'", subPlat, null, null));
        clients.add(new Client("MARS-ID-004", "Dirk", "'Endedneyer'", subExpired, null, null));
        clients.add(new Client("MARS-ID-005", "Elena", "'Reumbersedneyer'", subReimbursed, null, null));
        clients.add(new Client("MARS-ID-006", "Florence", "'Nosubneyer'", null, null, null));

        return clients;
    }

    @Override
    public List<Client> getSubscribedClients() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Subscription subSilver = new Subscription("Silver", "Short description.", 500.00, formatter.format(date), null, false);
        Subscription subGold = new Subscription("Gold", "Short description.", 500.00, formatter.format(date), null, false);
        Subscription subPlat = new Subscription("Platinum", "Short description.", 500.00, formatter.format(date), null, false);

        List<Client> clients = new ArrayList<>();
        clients.add(new Client("MARS-ID-001", "Ana", "'Silveneyer'", subSilver, null, null));
        clients.add(new Client("MARS-ID-002", "Bob", "'Goldeneyer'", subGold, null, null));
        clients.add(new Client("MARS-ID-003", "Carolina", "'Platineyer'", subPlat, null, null));

        return clients;
    }

    @Override
    public Client getClient(String identifier) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Subscription subSilver = new Subscription("Silver", "Short description.", 500.00, formatter.format(date), null, false);

        return new Client("MARS-ID-001", "Ana", "'Silveneyer'", subSilver, null, null);
    }

    @Override
    public List<Dispatch> getDispatches() {
        return null;
    }

    @Override
    public Dispatch getDispatch(String identifier) {
        return null;
    }

    @Override
    public void deleteDispatch(String identifier) {

    }

    @Override
    public void addDispatch(String identifier, DispatchSource source, DispatchTarget target) {

    }

    @Override
    public List<Dome> getDomes() {
        List<Dome> domes = new ArrayList<>();
        domes.add(new Dome("DOME-001"));
        domes.add(new Dome("DOME-002"));
        domes.add(new Dome("DOME-003"));
        domes.add(new Dome("DOME-004"));
        domes.add(new Dome("DOME-005"));
        domes.add(new Dome("DOME-006"));
        domes.add(new Dome("DOME-007"));
        domes.add(new Dome("DOME-008"));
        domes.add(new Dome("DOME-009"));
        domes.add(new Dome("DOME-010"));

        return domes;
    }

    @Override
    public Dome getDome(String identifier) {
        return new Dome("DOME-001");
    }
}
