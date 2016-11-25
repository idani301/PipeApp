package eyesatop.pipeapp;

/**
 * Created by ben on 11/24/16.
 */
public interface DroneController {
    void takeOff(TakeOffRequest request) throws DroneException;

    LatitudeResponse getLatitude() throws DroneException;

    void setLatitude(SetLatitudeRequest request) throws DroneException;

    void setFlightVelocities(SetFlightVelocitiesRequest request) throws DroneException;
}
