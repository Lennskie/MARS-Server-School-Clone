package mars.logic.domain.util;

import mars.logic.domain.Location;

public class RandomLocationGenerator {

    // Fit in the chosen map screen
    private static final double latitudeOffset = 29.62295;
    private static final double longitudeOffset = 35.40;

    private RandomLocationGenerator() {
        // DP: Utility Class!
        // Shouldn't be instantiated
    }

    public static Location getRandomLocation() {
        double latitude =  ((Math.random() * (9)) * 0.002) + latitudeOffset;
        double longitude = ((Math.random() * (59)) * 0.001) + longitudeOffset;

        return new Location(latitude, longitude);
    }
}
