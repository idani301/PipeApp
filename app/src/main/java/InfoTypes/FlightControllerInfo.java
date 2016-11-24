package InfoTypes;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIFlightControllerFlightMode;
import dji.common.flightcontroller.DJIFlightControllerNoFlyStatus;
import dji.common.flightcontroller.DJIFlightFailsafeOperation;
import dji.common.flightcontroller.DJIFlightOrientationMode;
import dji.common.flightcontroller.DJILocationCoordinate2D;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJICompass;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;

/**
 * Created by einav on 22/11/2016.
 */

public class FlightControllerInfo extends GeneralInfoType{

    private TelemetryInfo telemetryInfo = new TelemetryInfo();
    private HomeInfo homeInfo = new HomeInfo();
    private GpsInfo gpsInfo = new GpsInfo();

    private String firmwareVersion;
    private String version;

    private DJIFlightOrientationMode iocMode;
    private DJIFlightControllerFlightMode flightMode;
    private boolean isReachLimitedHeight;
    private boolean isReachLimitedRadius;
    private boolean isFailsafe;
    private boolean areMotorsOn;
    private boolean isIMUPreheating;
    private boolean isVisionSensorBeingUsed;
    private boolean isFlying;
    private int aircraftHeadDirection;
    private String flightModeString;
    private DJIFlightControllerNoFlyStatus noFlyStatus;
    private DJILocationCoordinate2D noFlyZoneCenter = new DJILocationCoordinate2D();
    private double noFlyZoneRadius;
    private int flightTime;

    private DJICompass compass = new DJICompass();
    private DJIFlightFailsafeOperation failsafeOperation;
//    private DJIFlightLimitation flightLimitation;
    private boolean isLedEnable;
    private String isLedEnableValid;

    private boolean virtualStickAdvancedModeEnabled;
    private boolean isVirtualStickControlModeAvailable;

    public FlightControllerInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiFlightController(final DJIFlightController djiFlightController) {

        if (djiFlightController == null) {
            setDataVFalse("updateFromDjiFlightController called with null. Maybe Drone not connected at all");
            return;
        }

        dataV = true;

        try {

            djiFlightController.setUpdateSystemStateCallback(new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {
                @Override
                public void onResult(DJIFlightControllerCurrentState currentState) {

                    telemetryInfo.updateFromDjiState(currentState);
                    homeInfo.updateFromDjiState(currentState);
                    gpsInfo.updateFromDjiState(currentState);

                    iocMode = currentState.getOrientaionMode();
                    flightMode = currentState.getFlightMode();
                    isReachLimitedHeight = currentState.isReachLimitedHeight();
                    isReachLimitedRadius = currentState.isReachLimitedRadius();
                    isFailsafe = currentState.isFailsafe();
                    areMotorsOn = currentState.areMotorsOn();
                    isIMUPreheating = currentState.isIMUPreheating();
                    isVisionSensorBeingUsed = currentState.isVisionSensorBeingUsed();
                    isFlying = currentState.isFlying();
                    aircraftHeadDirection = currentState.getAircraftHeadDirection();
                    flightModeString = currentState.getFlightModeString();
                    noFlyStatus = currentState.getNoFlyStatus();
                    noFlyZoneCenter = currentState.getNoFlyZoneCenter();
                    noFlyZoneRadius = currentState.getNoFlyZoneRadius();
                    flightTime = currentState.getFlightTime();
                }
            });

            compass = djiFlightController.getCompass();
            version = djiFlightController.getFlightControllerVersion();

            djiFlightController.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    firmwareVersion = djiError.getDescription();
                }
            });

            djiFlightController.getFlightFailsafeOperation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIFlightFailsafeOperation>() {
                @Override
                public void onSuccess(DJIFlightFailsafeOperation djiFlightFailsafeOperation) {
                    failsafeOperation = djiFlightFailsafeOperation;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    failsafeOperation = DJIFlightFailsafeOperation.Unknown;
                }
            });

//        flightLimitation = djiFlightController.getFlightLimitation();

//        djiFlightController.getHorizontalCoordinateSystem();

            djiFlightController.getGoHomeAltitude(new DJICommonCallbacks.DJICompletionCallbackWith<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    homeInfo.setHomeAltitudeCallback(aFloat);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    homeInfo.setHomeAltitudeCallback(-1);
                }
            });

            djiFlightController.getGoHomeBatteryThreshold(new DJICommonCallbacks.DJICompletionCallbackWith<Integer>() {
                @Override
                public void onSuccess(Integer integer) {
                    homeInfo.setBatteryThreshold(integer);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    homeInfo.setBatteryThreshold(-1);
                }
            });

            djiFlightController.getHomeLocation(new DJICommonCallbacks.DJICompletionCallbackWith<DJILocationCoordinate2D>() {
                @Override
                public void onSuccess(DJILocationCoordinate2D djiLocationCoordinate2D) {
                    homeInfo.setHomeLocationCallback(djiLocationCoordinate2D);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    homeInfo.setHomeLocationCallback(new DJILocationCoordinate2D(-1, -1));
                }
            });

            djiFlightController.getLEDsEnabled(new DJICommonCallbacks.DJICompletionCallbackWith<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    isLedEnable = aBoolean;
                    isLedEnableValid = "Yes";
                }

                @Override
                public void onFailure(DJIError djiError) {
                    isLedEnableValid = "No: " + djiError.getDescription();
                }
            });

//        djiFlightController.getMultiSideIMUCalibrationStatus(new DJICommonCallbacks.DJICompletionCallbackWith<DJIIMUState>() {
//            @Override
//            public void onSuccess(DJIIMUState djiimuState) {
//
//            }
//
//            @Override
//            public void onFailure(DJIError djiError) {
//
//            }
//        });

//        djiFlightController.getRollPitchControlMode();
//        djiFlightController.getRollPitchCoordinateSystem();
//        djiFlightController.getVerticalControlMode();
            virtualStickAdvancedModeEnabled = djiFlightController.getVirtualStickAdvancedModeEnabled();
//        djiFlightController.getYawControlMode();
            isVirtualStickControlModeAvailable = djiFlightController.isVirtualStickControlModeAvailable();

        }
        catch(Exception e){
            setDataVFalse("Error inside update flight controller from dji. Error: " + e.getMessage());
        }
    }

    public String toString(){
        if(dataV == false){
            return "Flight Controller Info not valid, Reason: " + invalidReason + "\n";
        }

        return "\tFlight Controller Info" +
                "\n\t\t" + telemetryInfo.toString() +
                "\n\t\t" + homeInfo.toString() +
                "\n\t\t" + gpsInfo.toString() +
                "\n\t\tFirmware Version              : " + firmwareVersion +
                "\n\t\tVersion                       : " + version +
                "\n\t\tOrientation Mode              : " + iocMode +
                "\n\t\tFlight Mode                   : " + flightMode +
                "\n\t\tFlight Mode String            : " + flightModeString +
                "\n\t\tReach High Limit              : " + isReachLimitedHeight +
                "\n\t\tReach Radius Limit            : " + isReachLimitedRadius +
                "\n\t\tIs Fail Safe                  : " + isFailsafe +
                "\n\t\tFail Safe Operation           : " + failsafeOperation +
                "\n\t\tMotors On                     : " + areMotorsOn +
                "\n\t\tIs Flying                     : " + isFlying +
                "\n\t\tIs Pre Heating                : " + isIMUPreheating +
                "\n\t\tVision Sensor Being Used      : " + isVisionSensorBeingUsed +
                "\n\t\tHead Direction                : " + aircraftHeadDirection +
                "\n\t\tNo Fly Status                 : " + noFlyStatus +
                "\n\t\tNo Fly Center.Lat             : " + noFlyZoneCenter.getLatitude() +
                "\n\t\tNo Fly Center.Lon             : " + noFlyZoneCenter.getLongitude() +
                "\n\t\tNo Fly Radius                 : " + noFlyZoneRadius +
                "\n\t\tFlight Time                   : " + flightTime +
                "\n\t\tCompass.Calibration status    : " + compass.getCalibrationStatus() +
                "\n\t\tCompass.Heading               : " + compass.getHeading() +
                "\n\t\tCompass.Has Error             : " + compass.hasError() +
                "\n\t\tCompass.Is Calibrating        : " + compass.isCalibrating() +
//                "\n\t\t : " + flightLimitation +
                "\n\t\tLed Enable                    : " + isLedEnable +
                "\n\t\tLed Enable Valid              : " + isLedEnableValid +
                "\n\t\tVirtual Stick Advance Mode EN : " + virtualStickAdvancedModeEnabled +
                "\n\t\tVirtual Stick Control Mode EN : " + isVirtualStickControlModeAvailable + "\n";
    }
}
