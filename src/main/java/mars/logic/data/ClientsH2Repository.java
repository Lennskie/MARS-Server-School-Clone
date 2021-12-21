package mars.logic.data;

import mars.logic.domain.Client;
import mars.logic.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientsH2Repository implements ClientsRepository {
    private static final Logger LOGGER = Logger.getLogger(ClientsH2Repository.class.getName());
    private static final String SQL_SELECT_USERS = "select * from users;";
    private static final String SQL_SELECT_USER_BY_ID = "select * from users where identifier = ?;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Client> getClients() {
        try (
            Connection conn = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USERS)
        ) {
            List<Client> clients = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(
                        rs.getString("identifier"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        null,
                        null,
                        null
                    );
                    clients.add(client);
                }
            }
            return clients;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get clients.", ex);
            throw new RepositoryException("Could not get clients.");
        }
    }

    @Override
    public Client getClient(String identifier) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_USER_BY_ID)
        ) {
            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                        rs.getString("identifier"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        null,
                        null,
                        null
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get client.", ex);
            throw new RepositoryException("Could not get client.");
        }
    }
}
