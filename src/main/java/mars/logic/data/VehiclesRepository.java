package mars.logic.data;

import mars.logic.domain.Vehicle;

import java.util.List;

public interface VehiclesRepository {
    void generateData();
    List<Vehicle> getVehicles();
    Vehicle getVehicles(int identifier);
}
