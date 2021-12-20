package mars.logic.data;

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

public class SubscriptionsH2Repository implements SubscriptionsRepository {
    private static final Logger LOGGER = Logger.getLogger(SubscriptionsH2Repository.class.getName());
    private static final String SQL_SELECT_SUBSCRIPTIONS = "select * from subscriptions;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Subscription> getSubscriptions() {
        try (
            Connection conn = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_SUBSCRIPTIONS)
        ) {
            List<Subscription> subscriptions = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subscription subscription = new Subscription(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                    );
                    subscriptions.add(subscription);
                }
            }
            return subscriptions;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get subscriptions.", ex);
            throw new RepositoryException("Could not get subscriptions.");
        }
    }
}
