package mars.logic.data;

import mars.logic.domain.Client;
import mars.logic.domain.Location;
import mars.logic.domain.Vehicle;

import java.util.List;

public interface ClientsRepository {
    void generateData();
    List<Client> getClients();
    List<Client> getSubscribedClients();
    Client getClient(String identifier);
    Client updateClientLocation(String identifier, Location location);
}
