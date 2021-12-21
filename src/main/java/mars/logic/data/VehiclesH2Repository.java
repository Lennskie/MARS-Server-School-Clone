package mars.logic.data;

import mars.logic.domain.Vehicle;
import mars.logic.exceptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehiclesH2Repository implements VehiclesRepository {
    private static final Logger LOGGER = Logger.getLogger(VehiclesH2Repository.class.getName());
    private static final String SQL_SELECT_VEHICLES = "select * from vehicles;";
    private static final String SQL_SELECT_VEHICLE_BY_ID = "select * from vehicles where identifier = ?;";

    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Vehicle> getVehicles() {
        try (
            Connection conn = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_VEHICLES)
        ) {
            List<Vehicle> vehicles = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = new Vehicle(
                        rs.getString("identifier")
                    );
                    vehicles.add(vehicle);
                }
            }
            return vehicles;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get vehicles.", ex);
            throw new RepositoryException("Could not get vehicles.");
        }
    }

    @Override
    public Vehicle getVehicle(String identifier) {
        try (
            Connection connection = Repositories.getH2Repo().getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_VEHICLE_BY_ID)
        ) {
            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(rs.getString("identifier"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get vehicle.", ex);
            throw new RepositoryException("Could not get vehicle.");
        }
    }
}
