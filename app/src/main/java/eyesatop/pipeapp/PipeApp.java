package eyesatop.pipeapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import dji.common.battery.DJIBatteryState;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.base.DJIDiagnostics;
import dji.sdk.battery.DJIBattery;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import dji.sdk.products.DJIAircraft;
import dji.sdk.products.DJIHandHeld;
import dji.sdk.sdkmanager.DJIBluetoothProductConnector;
import dji.sdk.sdkmanager.DJISDKManager;
import logs.LoggerTypes;
import logs.MainLogger;

/**
 * This enum represents all the mode of works we are working in.
 * NOTHING_CONNECTED - We don't have remote controller connected to the Android system.
 * ONLY_CONTROLLER - We have only remote controller that is connected to the Android system,
 *                   but no Drone.
 * CONTROLLER_AND_DRONE - We have Remote Controller connected that detects a drone in it's range.
 */
enum ControllerDroneState {NOTHING_CONNECTED,ONLY_CONTROLLER,CONTROLLER_AND_DRONE};

/**
 * This Application is the pipe process that will run in Android system, receive commands
 * and will send data to the application that will handshake with it.
 */
public class PipeApp extends Application {

    // The current state we are at.
    ControllerDroneState currentState = ControllerDroneState.NOTHING_CONNECTED;

    // The main logger of this program.
    public static MainLogger logger;

    /**
     * This class will eventually send the data outside, currently just to a log file.
     */
    public class OutputDemoThread extends Thread {

        public OutputDemoThread(){
            super();
        }

        public void run(){

            DroneInfo currentDroneInfo = new DroneInfo();

            DroneControlThread thread = null;

            while(true){

                if(currentState == ControllerDroneState.NOTHING_CONNECTED){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                currentDroneInfo.updateFromDjiAircraft(getAircraftInstance());
                logger.write_message(LoggerTypes.OUTPUT,currentDroneInfo.toString());

                if(currentState == ControllerDroneState.CONTROLLER_AND_DRONE && thread == null){
                    thread = new DroneControlThread(getAircraftInstance().getFlightController());
                    thread.start();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Gets the curret product instance that connected.
    private static synchronized DJIBaseProduct getProductInstance() {
            return DJISDKManager.getInstance().getDJIProduct();
    }

    // Maybe will have use in the future, currently no.
    public static boolean isHandHeldConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIHandHeld;
    }

    public static boolean isAircraftConnected() {
        return getProductInstance() != null && getProductInstance() instanceof DJIAircraft;
    }

    // Returns the current product connected as an aircraft type.
    private static synchronized DJIAircraft getAircraftInstance() {
        if (!isAircraftConnected()) return null;
        return (DJIAircraft) getProductInstance();
    }

    /**
     * In case we get to a fatal error we need to print the error reason to the error log
     * (and create it if needed).
     * In addition, we would like to mail the error to the relevant owner if the user will accept it.
     * 31.10.2016 - Owner is Idan Yitzhak(idany@eyesatop.com)
     * Finally, we will make final steps to close the program , and return relevant drones home.
     * Then , exit the progrem.
     */
    public static void fatalError(String reason) {

        try {
            if(!logger.is_log_exists(LoggerTypes.ERROR_DBG)) {
                logger.add_logger(LoggerTypes.ERROR_DBG, "fatal_error_dbg");
            }
            logger.write_message(LoggerTypes.ERROR_DBG,"FAIL: " + reason);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(1);
    }

    /**
     * In case we get to a fatal error we need to print the error reason to the error log
     * (and create it if needed).
     * In addition, we would like to mail the error to the relevant owner if the user will accept it.
     * 31.10.2016 - Owner is Idan Yitzhak(idany@eyesatop.com)
     */
    public static void nonFatalError(String reason) {

        try {
            if(!logger.is_log_exists(LoggerTypes.ERROR_DBG)) {
                logger.add_logger(LoggerTypes.ERROR_DBG, "fatal_error_dbg");
            }
            logger.write_message(LoggerTypes.ERROR_DBG,"FAIL: " + reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function responsable for the current state update.
     * Once we discover a major change(like remote disconnect or drone disconnect) we will
     * have to inform the application communicating with us about it.
     */
    private void updateCurrentState(){

        try {

            ControllerDroneState newCurrentState;

            if (getAircraftInstance() == null || getAircraftInstance().getRemoteController() == null || !getAircraftInstance().getRemoteController().isConnected()) {
                newCurrentState = ControllerDroneState.NOTHING_CONNECTED;
            } else if (getAircraftInstance() == null ||
                    getAircraftInstance().getFlightController() == null ||
                    !getAircraftInstance().getFlightController().isConnected()) {
                newCurrentState = ControllerDroneState.ONLY_CONTROLLER;
            } else {
                newCurrentState = ControllerDroneState.CONTROLLER_AND_DRONE;
            }

            if (newCurrentState == ControllerDroneState.ONLY_CONTROLLER && currentState != ControllerDroneState.ONLY_CONTROLLER) {
                logger.write_message(LoggerTypes.OUTPUT, "Only Controller Connected");
            } else if (newCurrentState == ControllerDroneState.NOTHING_CONNECTED && currentState != ControllerDroneState.NOTHING_CONNECTED) {
                logger.write_message(LoggerTypes.OUTPUT, "Nothing Connected");
            }

            if(currentState != newCurrentState) {
                logger.write_message(LoggerTypes.PROGREM_DBG, "State Change" +
                        "\n\tOld State: " + currentState +
                        "\n\tNew State: " + newCurrentState);
            }
            currentState = newCurrentState;
        }
        catch(Exception e){
            e.printStackTrace();
            nonFatalError("Couldn't update the current state, the error : " + e.getMessage());
        }
    }

    // The start of the application. here we just start loggers and all relevant threads.
    // Also, we will do the SDK init here.
    public void onCreate() {
        super.onCreate();

        try {
            logger = new MainLogger("pipeApp_logs");
            logger.add_logger(LoggerTypes.PROGREM_DBG, "program_dbg_log");
            logger.add_logger(LoggerTypes.DJI_DBG,"dji_dbg_log");
            logger.add_logger(LoggerTypes.OUTPUT,"output_log");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            OutputDemoThread thread = new OutputDemoThread();
            thread.start();
        }
        catch (Exception e){
            fatalError("Couldn't start the output thread. Error: " + e.getMessage());
        }

        /**
         * handles SDK Registration using the API_KEY
         */
        logger.write_message(LoggerTypes.DJI_DBG, "Comes into the initSDKManager");
        DJISDKManager.getInstance().initSDKManager(this, mDJISDKManagerCallback);
    }

    /**
     * Main listener, for the SDK manager call backs.
     */
    private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {

        /**
         * When the registration done, in case of success we will start the connection to the product.
         * @param error - Contains the error information in case of SDK failure on registration.
         */
        public void onGetRegisteredResult(DJIError error) {
            if(error == DJISDKError.REGISTRATION_SUCCESS) {
                DJISDKManager.getInstance().startConnectionToProduct();
            } else {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "SDK registeration failed",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
            logger.write_message(LoggerTypes.DJI_DBG, error.getDescription());
        }

        // Listener for the product change.
        // We getting here when the program starts and when the first remote controller is connected.
        // If we disconnect remote controller and plug it back again, we won't be here.
        public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {

            if(newProduct != null) {
                newProduct.setDJIBaseProductListener(mDJIBaseProductListener);
            }
            updateCurrentState();
        }

        private DJIBaseProduct.DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProduct.DJIBaseProductListener() {

            // Every time a product has change in it's components, we get here.
            // Idan TBD: add switch case for key, with new listener for each component. just in case
            // during air time some compponent will be damaged.
            public void onComponentChange(DJIBaseProduct.DJIComponentKey key, DJIBaseComponent oldComponent, DJIBaseComponent newComponent) {

                if(newComponent != null) {
                    newComponent.setDJIComponentListener(mDJIComponentListener);
                }
                updateCurrentState();
            }

            // So far, I noticed that this listener being called when drone is connected or disconnected.
            public void onProductConnectivityChanged(boolean isConnected) {updateCurrentState();}
        };
        private DJIBaseComponent.DJIComponentListener mDJIComponentListener = new DJIBaseComponent.DJIComponentListener() {

            /** When a component changes it's connectivity we get here. but without the key, so
             *  it's worthless.
             *  but we can get any kind of information we want here from getProduct method.
            */
            public void onComponentConnectivityChanged(boolean isConnected) {updateCurrentState();}

        };
    };

}
