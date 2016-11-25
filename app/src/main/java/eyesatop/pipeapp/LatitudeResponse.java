package eyesatop.pipeapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class LatitudeResponse {
    public enum LatitudeMeasurementType {
        BAROMETER, ULTRASONIC;
    }

    private static final String MEASUREMENTS = "measurements";

    private final Map<LatitudeMeasurementType, Integer> measurements;

    @JsonCreator
    public LatitudeResponse(
            @JsonProperty(MEASUREMENTS)
            Map<LatitudeMeasurementType, Integer> measurements) {
        this.measurements = measurements;
    }

    @JsonProperty(MEASUREMENTS)
    public Map<LatitudeMeasurementType, Integer> getMeasurements() {
        return measurements;
    }

    @JsonIgnore
    public int getLatitude() {
        if (measurements.size() == 0) return -1;

        int latitude = 0;
        for (Integer currentLatitude : measurements.values()) {
            latitude += latitude;
        }
        return latitude / measurements.size();
    }

    @JsonIgnore
    public int getLatitude(LatitudeMeasurementType type) {
        return measurements.containsKey(type) ? measurements.get(type) : -1;
    }
}
