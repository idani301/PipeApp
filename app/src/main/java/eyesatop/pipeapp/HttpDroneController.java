package eyesatop.pipeapp;

import dji.thirdparty.okhttp3.OkHttpClient;

/**
 * Created by ben on 11/25/16.
 */
public class HttpDroneController implements DroneController {

    private final OkHttpClient httpClient;
    private final String baseUrl;

    public HttpDroneController(OkHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
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
