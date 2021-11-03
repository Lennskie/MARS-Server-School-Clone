package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start-project there was no need for an Interface MarsRepository.
Please always use interfaces when needed.
To make this class useful, please complete it with the topics seen in the module OOA & SD
- Make sure the conf/config.json (local & production) properties are correct.
- The h2 web console is available at http://localhost:9000
- The h2 database file is located at ~/mars-db
- Don't create tables manually but create sql files in the folder resources.
  - With each deploy create the db structure from scratch (it's just a poc).
  - Two starter sql script are already given.
- Hint:
  - Mocking this repository for testing purposes is not needed.
    Create database creating and population scripts in plain SQL (resources folder).
    Use the @Before or @BeforeEach (depending on the type of test) to quickly create a populated database.
 */
public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_QUOTA_BY_ID = "select id, quote from quotes where id = ?;";
    private static final String SQL_INSERT_QUOTE = "insert into quotes (`quote`) values (?);";
    private static final String SQL_UPDATE_QUOTE = "update quotes set quote = ? where id = ?;";
    private static final String SQL_DELETE_QUOTE = "delete from quotes where id = ?;";
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database webconsole started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public Quote getQuote(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_QUOTA_BY_ID)
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

    public Quote insertQuote(String quoteValue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_QUOTE, Statement.RETURN_GENERATED_KEYS)) {

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

    public Quote updateQuote(int quoteId, String quote) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_QUOTE)) {

            stmt.setString(1, quote);
            stmt.setInt(2, quoteId);
            stmt.executeUpdate();
            return new Quote(quoteId, quote);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update quote.", ex);
            throw new RepositoryException("Could not update quote.");
        }
    }

    public void deleteQuote(int quoteId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_QUOTE)) {

            stmt.setInt(1, quoteId);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete quote.", ex);
            throw new RepositoryException("Could not delete quote.");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();
    }

    public void generateData() {
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
