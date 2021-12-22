package mars.logic.data;

import mars.logic.domain.*;
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
    private static final String SQL_SELECT_USERS = "select identifier, firstname, lastname, name, description, US.price, start_date, end_date, reimbursed from users left join USER_SUBSCRIPTION US on USERS.IDENTIFIER = US.USER_IDENTIFIER left join SUBSCRIPTIONS S on S.NAME = US.SUBSCRIPTION_NAME";
    private static final String SQL_SELECT_USER_BY_ID = SQL_SELECT_USERS + " where identifier = ?;";
    private static final String SQL_SELECT_SUBSCRIBED_USERS = SQL_SELECT_USERS + " where END_DATE is null and REIMBURSED = false";
    private static final String SQL_UPDATE_CLIENT = "update clients set latitude = ?, longitude = ? where identifier like ?;";


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
                            rs.getString("identifier"), rs.getString("firstname"), rs.getString("lastname"), new Subscription(rs.getString("name"),rs.getString("description"),rs.getDouble("price")), new Location(rs.getDouble("latitude"),rs.getDouble("longitude")), new VitalStatus(rs.getString("status"))
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
                    return new Client(rs.getString("identifier"), rs.getString("firstname"), rs.getString("lastname"), new Subscription(rs.getString("name"),rs.getString("description"),rs.getDouble("price")), new Location(rs.getDouble("latitude"),rs.getDouble("longitude")), new VitalStatus(rs.getString("status")));

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
            null,
            null
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

    @Override
    public Client updateClientLocation(String identifier, Location location) {
        try (Connection connection = Repositories.getH2Repo().getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(SQL_UPDATE_CLIENT);
             PreparedStatement selectStmt = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            updateStmt.setDouble(1, latitude);
            updateStmt.setDouble(2, longitude);
            updateStmt.setString(3, identifier);
            //updateStmt.executeUpdate();
            if (((updateStmt.executeUpdate()) <= 0)){
                throw new SQLException();
            }else{
                selectStmt.setString(1, identifier);
                ResultSet rs = selectStmt.executeQuery();
                return new Client(rs.getString("identifier"), rs.getString("firstname"), rs.getString("lastname"), new Subscription(rs.getString("name"),rs.getString("description"),rs.getDouble("price")), new Location(rs.getDouble("latitude"),rs.getDouble("longitude")), new VitalStatus(rs.getString("status")));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update location of Client.", ex);
            throw new RepositoryException("Could not update location of Client.");
        }
    }
}
