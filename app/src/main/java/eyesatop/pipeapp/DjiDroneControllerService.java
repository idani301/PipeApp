package eyesatop.pipeapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import dji.sdk.products.DJIAircraft;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by ben on 11/24/16.
 */
public class DjiDroneControllerService extends DroneControllerService {


    @Override
    public DroneController createController() {

//        return new DjiDroneController((DJIAircraft)DJISDKManager.getInstance().getDJIProduct());

        return new DroneController() {

            AtomicInteger latitude = new AtomicInteger(0);

            @Override
            public void takeOff(TakeOffRequest request) throws DroneException {
                Log.e("TEST", "TAKING OFF " + request.getLatitude());
            }

            @Override
            public LatitudeResponse getLatitude() throws DroneException {
                return new LatitudeResponse(
                        new HashMap<LatitudeResponse.LatitudeMeasurementType, Integer>() {{
                            put(LatitudeResponse.LatitudeMeasurementType.BAROMETER, latitude.get());
                            put(LatitudeResponse.LatitudeMeasurementType.ULTRASONIC, latitude.get()-1);
                        }}
                );
            }

            @Override
            public void setLatitude(SetLatitudeRequest latitude) throws DroneException {
                Log.e("TEST", "SET LATITUDE " + latitude);
                this.latitude.set(latitude.getLatitude());
            }

            @Override
            public void setFlightVelocities(SetFlightVelocitiesRequest request) throws DroneException {

            }
        };
    }
}
