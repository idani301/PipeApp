package eyesatop.pipeapp;

import InfoTypes.AirLinkInfo;
import InfoTypes.BatteryInfo;
import InfoTypes.CameraInfo;
import InfoTypes.FlightControllerInfo;
import InfoTypes.GimbalInfo;
import InfoTypes.RemoteControllerInfo;
import dji.sdk.products.DJIAircraft;

/**
 * This class represents all the Drone info
 */
public class DroneInfo {

    private BatteryInfo batteryInfo                   = new BatteryInfo();
    private RemoteControllerInfo remoteControllerInfo = new RemoteControllerInfo();
    private FlightControllerInfo flightControllerInfo = new FlightControllerInfo();
    private AirLinkInfo airLinkInfo                   = new AirLinkInfo();
    private CameraInfo cameraInfo                     = new CameraInfo();
    private GimbalInfo gimbalInfo                     = new GimbalInfo();

    public DroneInfo(){}

    public void updateFromDjiAircraft(DJIAircraft djiAircraft){

        if(djiAircraft == null){
            return;
        }

        batteryInfo.updateFromDjiBattery(djiAircraft.getBattery());
        remoteControllerInfo.updateFromDjiRemoteController(djiAircraft.getRemoteController());
        flightControllerInfo.updateFromDjiFlightController(djiAircraft.getFlightController());
        airLinkInfo.updateFromDjiAirLink(djiAircraft.getAirLink());
        cameraInfo.updateFromDjiCamera(djiAircraft.getCamera());
        gimbalInfo.updateFromDjiGimbal(djiAircraft.getGimbal());
    }

    public String toString(){
        return "Drone Info\n" +
                batteryInfo.toString() +
                remoteControllerInfo.toString() +
                flightControllerInfo.toString() +
                airLinkInfo.toString() +
                cameraInfo.toString() +
                gimbalInfo.toString() + "\n";
    }
}
