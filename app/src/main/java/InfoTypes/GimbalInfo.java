package InfoTypes;

import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalAxis;
import dji.common.gimbal.DJIGimbalControllerMode;
import dji.common.gimbal.DJIGimbalEndpointDirection;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.common.gimbal.DJIGimbalState;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.gimbal.DJIGimbal;

/**
 * Created by einav on 20/11/2016.
 */

public class GimbalInfo extends GeneralInfoType {

    private String firmwareVersion = "Unset";
    private float pitchDegree = -1;

    public GimbalInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiGimbal(DJIGimbal djiGimbal){

        if(djiGimbal == null){
            setDataVFalse("gimbal is null, maybe drone/camera are not even connected");
            return;
        }

        dataV = true;

        try {

            djiGimbal.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    firmwareVersion = djiError.getDescription();
                }
            });

            djiGimbal.setGimbalStateUpdateCallback(new DJIGimbal.GimbalStateUpdateCallback() {
                @Override
                public void onGimbalStateUpdate(DJIGimbal djiGimbal, DJIGimbalState djiGimbalState) {
                    pitchDegree = djiGimbalState.getAttitudeInDegrees().pitch;
                }
            });
        }
        catch(Exception e){
            setDataVFalse("Error inside updateFromDjiGimbal: " + e.getMessage());
        }
    }

    public String toString(){

        if(dataV == false){
            return "\tGimbal Info is invalid. Reason: " + invalidReason + "\n";
        }

        return "\tGimbal Info: " +
                "\n\t\tFirmware Version: " + firmwareVersion +
                "\n\t\tPitch Degree    : " + pitchDegree + "\n";
    }
}
