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
            LOGGER.log(Level.INFO, "== SEEDING ==");
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
            LOGGER.log(Level.INFO, "== SEEDING FINISHED ==");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        LOGGER.log(Level.INFO, "== EXECUTESCRIPT START ==");
        LOGGER.log(Level.INFO, "FileName: " + fileName);

        String createDbSql = readFile(fileName);

        LOGGER.log(Level.INFO, "createDbSql: ", createDbSql);

        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql)
        ) {
            stmt.executeUpdate();
        }

        LOGGER.log(Level.INFO, "== EXECUTESCRIPT END ==");
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        String[] pwd_split = password.split("");

        LOGGER.log(Level.INFO, "== GETCONNECTION START ==");

        LOGGER.log(Level.INFO, "URL: ", url);
        LOGGER.log(Level.INFO, "Username: ", username);
        LOGGER.log(Level.INFO, "Password: ",pwd_split[pwd_split.length - 2] + pwd_split[pwd_split.length - 1]);

        LOGGER.log(Level.INFO, "== GETCONNECTION END (before return) ==");

        return DriverManager.getConnection(url, username, password);
    }
}
