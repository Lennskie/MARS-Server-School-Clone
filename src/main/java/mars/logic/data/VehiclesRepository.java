package mars.logic.data;

import mars.logic.domain.Location;
import mars.logic.domain.Vehicle;

import java.util.List;
public interface VehiclesRepository {
    void generateData();
    List<Vehicle> getVehicles();
    Vehicle getVehicle(String identifier);
    Vehicle updateVehicleLocation(String identifier, Location location);
    Vehicle updateVehicleStatus(String identifier, Integer status);
}
