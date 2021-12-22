package mars.logic.data;

import mars.logic.domain.Client;
import mars.logic.domain.Location;
import mars.logic.domain.Subscription;
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
    private static final String SQL_SELECT_USERS = "select identifier, firstname, lastname,latitude,longitude,status, name, description, US.price, start_date, end_date, reimbursed from users left join USER_SUBSCRIPTION US on USERS.IDENTIFIER = US.USER_IDENTIFIER left join SUBSCRIPTIONS S on S.NAME = US.SUBSCRIPTION_NAME";
    private static final String SQL_SELECT_USER_BY_ID = SQL_SELECT_USERS + " where identifier = ?;";
    private static final String SQL_SELECT_SUBSCRIBED_USERS = SQL_SELECT_USERS + " where END_DATE is null and REIMBURSED = false";

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
                    clients.add(mapClient(rs));
                }
            }
            return clients;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get clients.", ex);
            throw new RepositoryException("Could not get clients.");
        }
    }

    @Override
    public List<Client> getSubscribedClients() {
        try (
            Connection conn = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_SUBSCRIBED_USERS)
        ) {
            List<Client> clients = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapClient(rs));
                }
            }
            return clients;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get subscribed clients.", ex);
            throw new RepositoryException("Could not get subscribed clients.");
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
                    return mapClient(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get client.", ex);
            throw new RepositoryException("Could not get client.");
        }
    }

    private Client mapClient(ResultSet rs) throws SQLException {
        return new Client(
            rs.getString("identifier"),
            rs.getString("firstname"),
            rs.getString("lastname"),
            mapSubscription(rs),
            new Location(rs.getFloat("latitude"), rs.getFloat("longitude")),
            rs.getString("status")
        );
    }

    private Subscription mapSubscription(ResultSet rs) throws SQLException {
        Subscription subscription = new Subscription(
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("start_date"),
            rs.getString("end_date"),
            rs.getBoolean("reimbursed")
        );

        return subscription.getName() != null ? subscription: null;
    }
}
