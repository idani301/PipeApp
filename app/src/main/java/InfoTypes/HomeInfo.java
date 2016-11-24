package InfoTypes;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIGoHomeStatus;
import dji.common.flightcontroller.DJILocationCoordinate2D;

/**
 * All home point info.
 */
public class HomeInfo {

    private String invalidReason;
    private boolean dataV;

    private DJIGoHomeStatus goHomeStatus;
    private DJILocationCoordinate2D homepoint;
    private boolean isGoingHome;
    private boolean isHomePointSet;
    private float homePointAltitude;
    private int goHomeHeight;

    private DJILocationCoordinate2D homeLocationCallback = new DJILocationCoordinate2D();
    private float homeAltitudeCallback;
    private int batteryThreshold;

    public HomeInfo(){
        setDataVFalse("Unset yet");
    }

    public void setHomeLocationCallback(DJILocationCoordinate2D location){
        homeLocationCallback = location;
    }

    public void setHomeAltitudeCallback(float alt){
        homeAltitudeCallback = alt;
    }

    public void setBatteryThreshold(int threshold){
        batteryThreshold = threshold;
    }

    public void setDataVFalse(String reason){
        invalidReason = reason;
        dataV = false;
    }

    public void updateFromDjiState(DJIFlightControllerCurrentState currentState){

        if(currentState == null){
            setDataVFalse("current state is null");
            return;
        }

        goHomeStatus = currentState.getGoHomeStatus();
        homepoint = currentState.getHomeLocation();
        isGoingHome = currentState.isGoingHome();
        isHomePointSet = currentState.isHomePointSet();
        homePointAltitude = currentState.getHomePointAltitude();
        goHomeHeight = currentState.getGoHomeHeight();
        dataV = true;
    }

    public String toString(){

        if(dataV == false){
            return "Home Info invalid. Reason: " + invalidReason + "\n";
        }

        return "Home Info: " +
                "\n\t\t\tGo Home Status      : " + goHomeStatus +
                "\n\t\t\tHome Point.Latitude : " + homepoint.getLatitude() +
                "\n\t\t\tHome Point.Longitude: " + homepoint.getLongitude() +
                "\n\t\t\tHome Point.Altitude : " + homePointAltitude +
                "\n\t\t\tIs Going Home       : " + isGoingHome +
                "\n\t\t\tIs Home Point Set   : " + isHomePointSet +
                "\n\t\t\tHome callback.lat   : " + homeLocationCallback.getLatitude() +
                "\n\t\t\tHome callback.lon   : " + homeLocationCallback.getLongitude() +
                "\n\t\t\tBattery Threshold   : " + batteryThreshold +
                "\n\t\t\tGo Home Height      : " + goHomeHeight + "\n";
    }
}
