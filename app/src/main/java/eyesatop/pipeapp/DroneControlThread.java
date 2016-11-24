package eyesatop.pipeapp;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.flightcontroller.DJIFlightControllerDataType;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
import dji.common.flightcontroller.DJIVirtualStickVerticalControlMode;
import dji.common.flightcontroller.DJIVirtualStickYawControlMode;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;
import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import dji.sdk.remotecontroller.DJIRemoteController;

/**
 * Created by einav on 23/11/2016.
 */

public class DroneControlThread extends Thread {

    private DJIFlightController djiFlightController;

    private boolean canContinue = false;

    public DroneControlThread(DJIFlightController djiFlightController){
        this.djiFlightController = djiFlightController;
    }

    public void run(){

        try {

            Thread.sleep(10000);

            canContinue = false;

//            djiFlightController.turnOnMotors(new DJICommonCallbacks.DJICompletionCallback() {
//                @Override
//                public void onResult(DJIError djiError) {
//                    System.out.println("stam");
//                }
//            });

            System.out.println("Take Off Request: " + System.currentTimeMillis());
            djiFlightController.takeOff(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    System.out.println("Done Take Off: " + System.currentTimeMillis());
                }
            });

            while(canContinue == false){
                djiFlightController.setUpdateSystemStateCallback(new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {
                    @Override
                    public void onResult(DJIFlightControllerCurrentState djiFlightControllerCurrentState) {
                        if(djiFlightControllerCurrentState.areMotorsOn() && (djiFlightControllerCurrentState.isFlying())){
                            canContinue = true;
                        }
                    }
                });
                Thread.sleep(10);
            }


            canContinue = false;

            djiFlightController.cancelTakeOff(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    canContinue = true;
                }
            });

            Thread.sleep(100000);

//            Thread.sleep(100);
//
//            djiFlightController.cancelTakeOff(new DJICommonCallbacks.DJICompletionCallback() {
//                @Override
//                public void onResult(DJIError djiError) {
//                    System.out.println("Done Cancel Take Off: " + System.currentTimeMillis());
//                    canContinue = true;
//                }
//            });

            djiFlightController.enableVirtualStickControlMode(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError djiError) {
                    canContinue = true;
                }
            });


//            djiFlightController.setVerticalControlMode(DJIVirtualStickVerticalControlMode.Velocity);
            djiFlightController.setRollPitchControlMode(DJIVirtualStickRollPitchControlMode.Velocity);
//            djiFlightController.setYawControlMode(DJIVirtualStickYawControlMode.AngularVelocity);
            djiFlightController.setVerticalControlMode(DJIVirtualStickVerticalControlMode.Position);


//            DJIFlightControllerDataType.DJIVirtualStickRollPitchControlMaxVelocity
            while(true) {
                djiFlightController.sendVirtualStickFlightControlData(new DJIVirtualStickFlightControlData(5.0F, 5.0F, 5.0F, 30.0F), new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        canContinue = true;
                    }
                });

                Thread.sleep(200);
            }


//            djiFlightController.sendVirtualStickFlightControlData(new DJIVirtualStickFlightControlData(0, 0, 0, 30.0F), new DJICommonCallbacks.DJICompletionCallback() {
//                @Override
//                public void onResult(DJIError djiError) {
//                    canContinue = true;
//                }
//            });
//
//
//            djiFlightController.sendVirtualStickFlightControlData(new DJIVirtualStickFlightControlData(0, 0, 0, 30.0F), new DJICommonCallbacks.DJICompletionCallback() {
//                @Override
//                public void onResult(DJIError djiError) {
//                    canContinue = true;
//                }
//            });

        }
        catch(Exception e){

        }

    }

}
