package InfoTypes;

import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIGPSSignalStatus;

/**
 * Created by einav on 23/11/2016.
 */

public class GpsInfo extends GeneralInfoType {

    private DJIGPSSignalStatus signalStatus;
    private double satelliteCount;

    public void updateFromDjiState(DJIFlightControllerCurrentState currentState){
        if(currentState == null){
            setDataVFalse("current_state = null");
            return;
        }

        dataV = true;

        try{
            signalStatus   = currentState.getGpsSignalStatus();
            satelliteCount = currentState.getSatelliteCount();
        }
        catch(Exception e){
            setDataVFalse("Error inside updateFromDjiState : " + e.getMessage());
        }
    }

    public String toString(){
        if(dataV == false){
            return "Gps Info is not valid. Reason: " + invalidReason + "\n";
        }

        return "Gps Info " +
                "\n\t\t\tSignal Status   : " + signalStatus.value() +
                "\n\t\t\tSatellite Count : " + satelliteCount + "\n";
    }
}
