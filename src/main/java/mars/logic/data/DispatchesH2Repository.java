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

public class DispatchesH2Repository implements DispatchesRepository {

    private static final Logger LOGGER = Logger.getLogger(DispatchesH2Repository.class.getName());
    private static final String SQL_INSERT_DISPATCH = "INSERT INTO dispatches VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_DISPATCHES = "SELECT identifier, source_type, destination_type, source_identifier, destination_identifier FROM dispatches";
    private static final String SQL_WHERE_IDENTIFIER = " WHERE identifier = ?";
    private static final String SQL_SELECT_DISPATCH = SQL_SELECT_DISPATCHES + SQL_WHERE_IDENTIFIER;
    private static final String SQL_DELETE_DISPATCH = "DELETE FROM dispatches" + SQL_WHERE_IDENTIFIER;
    public static final String ERROR_MSG_ADD_DISPATCH = "Unable to add dispatch";
    public static final String VEHICLE = "Vehicle";
    public static final String CLIENT = "Client";
    public static final String DOME = "Dome";

    private final ClientsRepository clientRepository = Repositories.getClientsRepo();
    private final DomesRepository domeRepository = Repositories.getDomesRepo();
    private final VehiclesRepository vehicleDepository = Repositories.getVehiclesRepo();


    @Override
    public void generateData() {
        Repositories.getH2Repo().generateData();
    }

    @Override
    public List<Dispatch> getDispatches() {
        try (
                Connection connection = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_DISPATCHES)
        ) {
            ResultSet rs = stmt.executeQuery();
            List<Dispatch> dispatches = new ArrayList<>();
            while (rs.next()) {
                dispatches.add(mapDispatch(rs));
            }

            return dispatches;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get dispatches from database", ex);
            throw new RepositoryException("Unable to get dispatches from database");

        } catch (RepositoryException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get valid dispatch from database", ex);
            throw new RepositoryException("Unable to get valid dispatch from database");

        }
    }

    @Override
    public Dispatch getDispatch(String identifier) {
        try (
                Connection connection = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_DISPATCH)
        ) {

            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapDispatch(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get dispatch.", ex);
            throw new RepositoryException("Could not get dispatch.");
        }
    }


    private Dispatch mapDispatch(ResultSet rs) throws SQLException, RepositoryException {

        return new Dispatch(
                rs.getString("identifier"),
                mapDispatchSource(rs.getString("source_type"), rs.getString("source_identifier")),
                mapDispatchDestination(rs.getString("destination_type"), rs.getString("destination_identifier"))
        );
    }

    private DispatchSource mapDispatchSource(String sourceType, String sourceIdentifier) throws RepositoryException {
        switch (sourceType) {
            case VEHICLE:
                return vehicleDepository.getVehicle(sourceIdentifier);
            case CLIENT:
                return clientRepository.getClient(sourceIdentifier);
            default:
                throw new RepositoryException("Unable to get valid DispatchSource from database");
        }
    }

    private DispatchDestination mapDispatchDestination(String destinationType, String destinationIdentifier) throws RepositoryException {
        switch (destinationType) {
            case CLIENT:
                return clientRepository.getClient(destinationIdentifier);
            case DOME:
                return domeRepository.getDome(destinationIdentifier);
            default:
                throw new RepositoryException("Unable to get valid DispatchTarget from database");
        }
    }

    @Override
    public void deleteDispatch(String identifier) {
        try (
                Connection connection = Repositories.getH2Repo().getConnection();
                PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_DISPATCH)
        ) {
            stmt.setString(1, identifier);
            if (stmt.executeUpdate() <= 0) {
                throw new SQLException();
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to delete dispatch", ex);
            throw new RepositoryException("Unable to delete dispatch");

        }
    }

    @Override
    public Dispatch addDispatch(String identifier, String sourceType, String destinationType, String sourceIdentifier, String destinationIdentifier) {
        try (
                Connection connection = Repositories.getH2Repo().getConnection();
                PreparedStatement addStmt = connection.prepareStatement(SQL_INSERT_DISPATCH);
                PreparedStatement selectStmt = connection.prepareStatement(SQL_SELECT_DISPATCH)
        ) {
            addStmt.setString(1, identifier);

            switch (sourceType) {
                case VEHICLE:
                    addStmt.setString(2, VEHICLE);
                    break;
                case CLIENT:
                    addStmt.setString(2, CLIENT);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid source type given for dispatch");
            }

            switch (destinationType) {
                case DOME:
                    addStmt.setString(3, DOME);
                    break;
                case CLIENT:
                    addStmt.setString(3, CLIENT);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid destination type given for dispatch");
            }

            addStmt.setString(4, sourceIdentifier);
            addStmt.setString(5, destinationIdentifier);

            if (addStmt.executeUpdate() <= 0) {
                throw new RepositoryException(ERROR_MSG_ADD_DISPATCH);
            } else {
                selectStmt.setString(1, identifier);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    return mapDispatch(rs);
                } else {
                    return null;
                }
            }

        } catch (SQLException | RepositoryException ex) {
            LOGGER.log(Level.SEVERE, ERROR_MSG_ADD_DISPATCH, ex);
            throw new RepositoryException(ERROR_MSG_ADD_DISPATCH);

        }
    }
}
