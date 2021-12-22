package mars.logic.data;

import mars.logic.domain.Dome;
import mars.logic.domain.Location;
import mars.logic.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomesH2Repository implements DomesRepository {
    private static final Logger LOGGER = Logger.getLogger(DomesH2Repository.class.getName());
    private static final String SQL_SELECT_DOMES = "select * from domes;";
    private static final String SQL_SELECT_DOME_BY_ID = "select * from domes where identifier = ?;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Dome> getDomes() {
        try (
                Connection conn = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_DOMES)
        ) {
            List<Dome> domes = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dome dome = new Dome(rs.getString("identifier"), rs.getInt("size"), new Location(rs.getFloat("latitude"), rs.getFloat("longitude")));
                    domes.add(dome);
                }
            }
            return domes;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get domes.", ex);
            throw new RepositoryException("Failed to get domes.");
        }
    }

    @Override
    public Dome getDome(String identifier) {
        try (
                Connection connection = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_DOME_BY_ID)
        ) {
            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Dome(rs.getString("identifier"), rs.getInt("size"), new Location(rs.getFloat("latitude"), rs.getFloat("longitude")));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get dome.", ex);
            throw new RepositoryException("Failed to get dome.");
        }
    }
}
