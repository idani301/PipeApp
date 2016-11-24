package InfoTypes;

import dji.common.flightcontroller.DJIAttitude;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.flightcontroller.DJILocationCoordinate3D;

/**
 * Created by einav on 23/11/2016.
 */

public class TelemetryInfo {

    private boolean dataV = false;
    private String reasonForInvalid;

    private DJIAttitude attitude;
    private DJILocationCoordinate3D aircraftLocation = new DJILocationCoordinate3D(new DJILocationCoordinate2D(-1,-1),-1);
    private boolean isUltrasonicBeingUsed;
    private boolean isUltrasonicError;
    private float ultrasonicHeight;
    private float velocityX;
    private float velocityY;
    private float velocityZ;

    public TelemetryInfo(){
        setDataVFalse("Unset yet");
    }

    private void setDataVFalse(String reason){
        dataV = false;
        reasonForInvalid = reason;
    }

    public void updateFromDjiState(DJIFlightControllerCurrentState currentState){

        if(currentState == null){
            setDataVFalse("Trying to update from DJIFlightControllerCurrentState null");
        }

        dataV = true;
        attitude = currentState.getAttitude();
        aircraftLocation = currentState.getAircraftLocation();
        isUltrasonicBeingUsed = currentState.isUltrasonicBeingUsed();
        isUltrasonicError = currentState.isUltrasonicError();
        ultrasonicHeight = currentState.getUltrasonicHeight();
        velocityX = currentState.getVelocityX();
        velocityY = currentState.getVelocityY();
        velocityZ = currentState.getVelocityZ();

    }

    public String toString(){

        if(dataV == false){
            return "Telementry info is not valid. Reason: " + reasonForInvalid + "\n";
        }

        String ultrasonicInfo = "\n\t\t\tUltrasonic Used  : " + isUltrasonicBeingUsed;
        if(isUltrasonicBeingUsed){
            ultrasonicInfo += "\n\t\t\tUltrasonic Height: " + ultrasonicHeight +
                              "\n\t\t\tUltrasonic Error : " + isUltrasonicError;
        }

        return "Telemetry Info: " +
                "\n\t\t\tattitude.pitch   : " + attitude.pitch +
                "\n\t\t\tattitude.roll    : " + attitude.roll +
                "\n\t\t\tattitude.yaw     : " + attitude.yaw +
                ultrasonicInfo +
                "\n\t\t\tBarometer Height : " + aircraftLocation.getAltitude() +
                "\n\t\t\tLatitude         :" + aircraftLocation.getLatitude() +
                "\n\t\t\tLongitude        :" + aircraftLocation.getLongitude() +
                "\n\t\t\tVelocity_X       : " + velocityX +
                "\n\t\t\tVelocity_Y       : " + velocityY +
                "\n\t\t\tVelocity_Z       : " + velocityZ + "\n";
    }
}
