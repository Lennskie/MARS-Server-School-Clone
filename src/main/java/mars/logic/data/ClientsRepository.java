package mars.logic.data;

import mars.logic.domain.Client;

import java.util.List;

public interface ClientsRepository {
    void generateData();
    List<Client> getClients();
    List<Client> getSubscribedClients();
    Client getClient(String identifier);
}
