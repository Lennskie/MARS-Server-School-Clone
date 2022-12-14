package mars.web.bridge;

import mars.logic.domain.Dangerzone;
import mars.logic.domain.Location;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;


public class Request {
    public static final String SPEC_VEHICLE_ID = "vehicleId";
    public static final String SPEC_DOME_ID = "domeId";
    public static final String SPEC_CLIENT_ID = "clientId";
    public static final String SPEC_VEHICLE_LATITUDE = "latitude";
    public static final String SPEC_VEHICLE_LONGITUDE = "longitude";
    private static final String SPEC_VEHICLE_STATUS = "status";
    public static final String SPEC_CLIENT_LATITUDE = "latitude";
    public static final String SPEC_CLIENT_LONGITUDE = "longitude";
    public static final String SPEC_IDENTIFIER = "identifier";

    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
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

    public String getIdentifier() {
        return params.pathParameter(SPEC_IDENTIFIER).getString();
    }

    public String getBodyIdentifier() {
        return params.body().getJsonObject().getString(SPEC_IDENTIFIER);
    }

    public String getIdentifier(String field) {
        return params.body().getJsonObject().getString(field + "_identifier");
    }

    @SuppressWarnings("unused")
    public Dangerzone getDangerzones() {
        Location testLocation = new Location(3.555, 7.888);
        return new Dangerzone("DZ-001",testLocation, 50);
    }

    public Location getVehicleLocation() {
        double latitude = params.pathParameter(SPEC_VEHICLE_LATITUDE).getDouble();
        double longitude = params.pathParameter(SPEC_VEHICLE_LONGITUDE).getDouble();
        return new Location(latitude, longitude);
    }

    public Boolean getVehicleStatus() {
        return params.pathParameter(SPEC_VEHICLE_STATUS).getBoolean();
    }

    public Location getClientLocation() {
        double latitude = params.pathParameter(SPEC_CLIENT_LATITUDE).getDouble();
        double longitude = params.pathParameter(SPEC_CLIENT_LONGITUDE).getDouble();
        return new Location(latitude, longitude);
    }

    @SuppressWarnings("unused")
    public String getJsonAsString() {
        return params.body().getJsonObject().toString();
    }

    public String getType(String field) {
        return params.body().getJsonObject().getString(field + "_type");
    }
}
