package mars.logic.data.util;

import mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class H2Repository {
    private static final Logger LOGGER = Logger.getLogger(H2Repository.class.getName());
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public H2Repository(String url, String username, String password, int console) {
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
            throw new RepositoryException("Could not configure H2repository");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();
    }

    public void generateData() {
        try {
            LOGGER.log(Level.CONFIG, "== SEEDING ==");
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
            LOGGER.log(Level.CONFIG, "== SEEDING FINISHED ==");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed: {0}", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        LOGGER.log(Level.CONFIG, "== EXECUTESCRIPT START ==");
        LOGGER.log(Level.CONFIG, "fileName: {0}" + fileName);
        String createDbSql = readFile(fileName);
        LOGGER.log(Level.CONFIG, "createDbSql: {0}", createDbSql);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            LOGGER.log(Level.CONFIG, "connection: {0}", conn);
            LOGGER.log(Level.CONFIG, "stmt: {0}", stmt);
            stmt.executeUpdate();
        }
        LOGGER.log(Level.CONFIG, "== EXECUTESCRIPT END ==");
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        LOGGER.log(Level.CONFIG, "== GETCONNECTION START ==");
        LOGGER.log(Level.CONFIG, "URL: {0} ", url);
        LOGGER.log(Level.CONFIG, "Username: {0}", username);
        LOGGER.log(Level.CONFIG, "Password: {0}", password.substring(0,2));
        LOGGER.log(Level.CONFIG, "== GETCONNECTION END (before return) ==");

        return DriverManager.getConnection(url, username, password);
    }
}
