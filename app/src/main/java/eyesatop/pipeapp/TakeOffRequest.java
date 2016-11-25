package eyesatop.pipeapp;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TakeOffRequest {

    private static final String LATITUDE = "latitude";

    private final int latitude;

    @JsonIgnore
    public TakeOffRequest() {
        this(-1);
    }

    @JsonCreator
    public TakeOffRequest(
            @JsonProperty(LATITUDE)
            int latitude) {

        this.latitude = latitude;
    }

    @JsonProperty(LATITUDE)
    public int getLatitude() {
        return latitude;
    }
}
