package mars.logic.domain.util;

import mars.logic.domain.Location;

public class RandomLocationGenerator {

    // Fit in the chosen map screen
    // @TODO REMOVE IF UNUSED
    @SuppressWarnings("unused")
    private static final double LATITUDE_OFFSET = 29.62295;
    @SuppressWarnings("unused")
    private static final double LONGITUDE_OFFSET = 35.40;

    private RandomLocationGenerator() {
        // DP: Utility Class!
        // Shouldn't be instantiated
    }

    public static Location getRandomLocation() {
        double latitude;
        double longitude;

        latitude = Math.random() * 9d + 1d;
        longitude = Math.random() * 59d;

        latitude = (latitude * 0.002) + 29.62295;
        longitude = (longitude * 0.001) + 35.40;

        return new Location(latitude, longitude);
    }
}
