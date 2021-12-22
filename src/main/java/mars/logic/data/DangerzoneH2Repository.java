package mars.logic.data;

import mars.logic.domain.Dangerzone;
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

public class DangerzoneH2Repository implements DangerzonesRepository {
    private static final Logger LOGGER = Logger.getLogger(DangerzoneH2Repository.class.getName());
    private static final String SQL_SELECT_DANGERZONES = "select * from dangerzones;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Dangerzone> getDangerzones() {
        try (
                Connection conn = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_DANGERZONES)
        ) {
            List<Dangerzone> dangerzones = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Dangerzone dangerzone = new Dangerzone(rs.getString("identifier"),new Location(rs.getFloat("latitude"),rs.getFloat("longitude")), rs.getFloat("radius"));
                    dangerzones.add(dangerzone);
                }
            }
            return dangerzones;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get domes.", ex);
            throw new RepositoryException("Failed to get domes.");
        }
    }

}
