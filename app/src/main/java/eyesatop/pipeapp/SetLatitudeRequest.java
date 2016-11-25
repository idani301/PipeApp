package eyesatop.pipeapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ben on 11/25/16.
 */
public class SetLatitudeRequest {

    private static final String LATITUDE = "latitude";

    private final int latitude;

    @JsonCreator
    public SetLatitudeRequest(
            @JsonProperty(LATITUDE)
            int latitude) {
        this.latitude = latitude;

    }

    @JsonProperty(LATITUDE)
    public int getLatitude() {
        return latitude;
    }
}
