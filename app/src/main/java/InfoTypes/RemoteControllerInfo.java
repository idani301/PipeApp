package InfoTypes;

import dji.common.error.DJIError;
import dji.common.remotecontroller.DJIRCBatteryInfo;
import dji.common.remotecontroller.DJIRCControlMode;
import dji.common.remotecontroller.DJIRCGPSData;
import dji.common.remotecontroller.DJIRCGimbalControlSpeed;
import dji.common.remotecontroller.DJIRCHardwareState;
import dji.common.remotecontroller.DJIRCInfo;
import dji.common.remotecontroller.DJIRCRemoteFocusState;
import dji.common.remotecontroller.DJIRCToAircraftPairingState;
import dji.common.remotecontroller.DJIRemoteControllerMode;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.remotecontroller.DJIRemoteController;

/**
 * Created by einav on 20/11/2016.
 */

public class RemoteControllerInfo extends GeneralInfoType {

    private int remainingBatteryPercent = -1;

    private String firmwareVersion;
    private boolean isConnected;

    private String serialNumber;

    private short wheelControlerGimbalSpeed;
    private String wheelControlerGimbalSpeedValid;
    private DJIRCGPSData gpsData = new DJIRCGPSData();

    private DJIRCHardwareState hardwareState = new DJIRCHardwareState();

    public RemoteControllerInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiRemoteController(DJIRemoteController djiRemoteController){

        if(djiRemoteController == null){
            setDataVFalse("djiRemoteController is null. maybe controller is not connected");
            return;
        }

        dataV = true;

        try {

            djiRemoteController.setBatteryStateUpdateCallback(new DJIRemoteController.RCBatteryStateUpdateCallback() {
                @Override
                public void onBatteryStateUpdate(DJIRemoteController djiRemoteController, DJIRCBatteryInfo djircBatteryInfo) {
                    remainingBatteryPercent = djircBatteryInfo.remainingEnergyInPercent;
                }
            });

            djiRemoteController.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    firmwareVersion = djiError.getDescription();
                }
            });

            djiRemoteController.getSerialNumber(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    serialNumber = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    serialNumber = djiError.getDescription();
                }
            });

            djiRemoteController.getRCWheelControlGimbalSpeed(new DJICommonCallbacks.DJICompletionCallbackWith<Short>() {
                @Override
                public void onSuccess(Short aShort) {
                    wheelControlerGimbalSpeed = aShort;
                    wheelControlerGimbalSpeedValid = "Valid";
                }

                @Override
                public void onFailure(DJIError djiError) {
                    wheelControlerGimbalSpeedValid = "Invalid: " + djiError.getDescription();
                }
            });

//            djiRemoteController.getRCToAircraftPairingState(new DJICommonCallbacks.DJICompletionCallbackWith<DJIRCToAircraftPairingState>() {
//                @Override
//                public void onSuccess(DJIRCToAircraftPairingState djircToAircraftPairingState) {
//
//                }
//
//                @Override
//                public void onFailure(DJIError djiError) {
//
//                }
//            });

//            djiRemoteController.getRCCustomButtonTag(new DJICommonCallbacks.DJICompletionCallbackWithTwoParam<Short, Short>() {
//                @Override
//                public void onSuccess(Short aShort, Short aShort2) {
//
//                }
//
//                @Override
//                public void onFailure(DJIError djiError) {
//
//                }
//            });


            djiRemoteController.setGpsDataUpdateCallback(new DJIRemoteController.RCGpsDataUpdateCallback() {
                @Override
                public void onGpsDataUpdate(DJIRemoteController djiRemoteController, DJIRCGPSData djircgpsData) {
                    gpsData = djircgpsData;
                }
            });

            djiRemoteController.setHardwareStateUpdateCallback(new DJIRemoteController.RCHardwareStateUpdateCallback() {
                @Override
                public void onHardwareStateUpdate(DJIRemoteController djiRemoteController, DJIRCHardwareState djircHardwareState) {
                    hardwareState = djircHardwareState;
                }
            });

            isConnected = djiRemoteController.isConnected();
        }
        catch(Exception e){
            setDataVFalse("Error inside updateFromDjiRemoteController: " + e.getMessage());
        }
    }

    public String toString(){
        if(dataV == false){
            return "\tRemote Controller Info is invalid. Reason: " + invalidReason + "\n";
        }
        return "\tRemote Controller Info       : " +
                "\n\t\tFirmware Version        : " + firmwareVersion +
                "\n\t\tBattery Percent         : " + remainingBatteryPercent + "%" +
                "\n\t\tConnected               : " + isConnected +
                "\n\t\tSerial Number           : " + serialNumber +
                "\n\t\tGimbal Speed            : " + wheelControlerGimbalSpeed +
                "\n\t\tGimbal Speed Info Valid : " + wheelControlerGimbalSpeedValid +
                "\n\t\tGPS Valid               : " + gpsData.isValid +
                "\n\t\tGPS Accuracy            : " + gpsData.accuracy +
                "\n\t\tGPS Latitude            : " + gpsData.latitude +
                "\n\t\tGPS Longitude           : " + gpsData.longitude +
                "\n\t\tGPS Satellite Count     : " + gpsData.satelliteCount +
                "\n\t\tGPS Time                : " + gpsData.time +
                "\n\t\tButtons.leftHorizontal  : " + hardwareState.leftHorizontal.value +
                "\n\t\tButtons.leftVertical    : " + hardwareState.leftVertical.value +
                "\n\t\tButtons.rightVertical   : " + hardwareState.rightVertical.value +
                "\n\t\tButtons.rightHorizontal : " + hardwareState.rightHorizontal.value +
                "\n\t\tButtons.leftWheel       : " + hardwareState.leftWheel.value +
                "\n\t\tButtons.rightWheel      : " + hardwareState.rightWheel.value +
                "\n\t\tButtons.flightModeSwitch: " + hardwareState.flightModeSwitch.mode +
                "\n\t\tButtons.goHomeButton    : " + hardwareState.goHomeButton.buttonDown +
                "\n\t\tButtons.recordButton    : " + hardwareState.recordButton.buttonDown +
                "\n\t\tButtons.shutterButton   : " + hardwareState.shutterButton.buttonDown +
                "\n\t\tButtons.playbackButton  : " + hardwareState.playbackButton.buttonDown +
                "\n\t\tButtons.pauseButton     : " + hardwareState.pauseButton.buttonDown +
                "\n\t\tButtons.customButton1   : " + hardwareState.customButton1.buttonDown +
                "\n\t\tButtons.customButton2   : " + hardwareState.customButton2.buttonDown +
                "\n\t\tButtons.dimensionButton : " + hardwareState.dimensionButton.buttonPressed + "\n";
    }
}
