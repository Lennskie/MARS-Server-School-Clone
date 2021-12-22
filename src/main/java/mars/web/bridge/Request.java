package mars.web.bridge;

import mars.logic.domain.Dangerzone;
import mars.logic.domain.Location;
import mars.logic.domain.Vehicle;
import mars.web.exceptions.MalformedRequestException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 */
public class Request {
    private static final Logger LOGGER = Logger.getLogger(Request.class.getName());
    public static final String SPEC_VEHICLE_ID = "vehicleId";
    public static final String SPEC_DOME_ID = "domeId";
    public static final String SPEC_CLIENT_ID = "clientId";
    public static final String SPEC_QUOTE = "quote";
    public static final String SPEC_VEHICLE_LATITUDE = "latitude";
    public static final String SPEC_VEHICLE_LONGITUDE = "longitude";
    public static final String SPEC_CLIENT_LATITUDE = "latitude";
    public static final String SPEC_CLIENT_LONGITUDE = "longitude";

    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public String getQuote() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_QUOTE);
            return params.body().get().toString();
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, "Unable to decipher the data in the body", ex);
            throw new MalformedRequestException("Unable to decipher the data in the request body. See logs for details.");
        }
    }

    public String getVehicleId() {
        return params.pathParameter(SPEC_VEHICLE_ID).getString();
    }

    public String getDomeId() {
        return params.pathParameter(SPEC_DOME_ID).getString();
    }

    public String getClientId() {
        return params.pathParameter(SPEC_CLIENT_ID).getString();
    }

    public Dangerzone getDangerzones() {
        Location testLocation = new Location(3.555, 7.888);
        return new Dangerzone(testLocation, 50);
    }

    public Location getVehicleLocation() {
        double latitude = params.pathParameter(SPEC_VEHICLE_LATITUDE).getDouble();
        double longitude = params.pathParameter(SPEC_VEHICLE_LONGITUDE).getDouble();
        return new Location(latitude, longitude);
    }

    public Location getClientLocation() {
        double latitude = params.pathParameter(SPEC_CLIENT_LATITUDE).getDouble();
        double longitude = params.pathParameter(SPEC_CLIENT_LONGITUDE).getDouble();
        return new Location(latitude, longitude);
    }
}
