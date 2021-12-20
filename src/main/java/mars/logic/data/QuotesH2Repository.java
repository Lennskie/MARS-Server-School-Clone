package mars.logic.data;

import mars.logic.domain.Quote;
import mars.logic.exceptions.RepositoryException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuotesH2Repository implements QuotesRepository {
    private static final Logger LOGGER = Logger.getLogger(QuotesH2Repository.class.getName());
    private static final String SQL_QUOTA_BY_ID = "select id, quote from quotes where id = ?;";
    private static final String SQL_INSERT_QUOTE = "insert into quotes (`quote`) values (?);";
    private static final String SQL_UPDATE_QUOTE = "update quotes set quote = ? where id = ?;";
    private static final String SQL_DELETE_QUOTE = "delete from quotes where id = ?;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public Quote getQuote(int id) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_QUOTA_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Quote(rs.getInt("id"), rs.getString("quote"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get quote.", ex);
            throw new RepositoryException("Could not get quote.");
        }
    }

    @Override
    public Quote insertQuote(String quoteValue) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_QUOTE, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, quoteValue);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            Quote quote = new Quote(quoteValue);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quote.setId(generatedKeys.getInt(1));
                    return quote;
                }
                else {
                    throw new SQLException("Creating quote failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create quote.", ex);
            throw new RepositoryException("Could not create quote.");
        }
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_QUOTE)
        ) {
            stmt.setString(1, quote);
            stmt.setInt(2, quoteId);
            stmt.executeUpdate();
            return new Quote(quoteId, quote);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update quote.", ex);
            throw new RepositoryException("Could not update quote.");
        }
    }

    @Override
    public void deleteQuote(int quoteId) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_QUOTE)
        ) {
            stmt.setInt(1, quoteId);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete quote.", ex);
            throw new RepositoryException("Could not delete quote.");
        }
    }
}
