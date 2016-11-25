package eyesatop.pipeapp;

import dji.sdk.products.DJIAircraft;

/**
 * Created by ben on 11/24/16.
 */
public class DjiDroneController implements DroneController {
    private final DJIAircraft aircraft;

    public DjiDroneController(DJIAircraft aircraft) {
        this.aircraft = aircraft;
    }


    @Override
    public void takeOff(TakeOffRequest request) throws DroneException {

    }

    @Override
    public LatitudeResponse getLatitude() throws DroneException {
        return null;
    }

    @Override
    public void setLatitude(SetLatitudeRequest latitude) throws DroneException {

    }

    @Override
    public void setFlightVelocities(SetFlightVelocitiesRequest request) throws DroneException {

    }
}
