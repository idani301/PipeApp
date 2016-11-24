package InfoTypes;

import android.os.Looper;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;

/**
 * Created by einav on 20/11/2016.
 */

public class CameraInfo extends GeneralInfoType {

    private String firmwareVersion;

    private int video_size=-1;
    private DJICodecManager mCodecManager = null;


    public CameraInfo(){

        Looper.prepare();
        setDataVFalse("Unset yet");
        mCodecManager = new DJICodecManager(null,null,200,200);
    }

    public void updateFromDjiCamera(DJICamera djiCamera){

        if(djiCamera == null){
            setDataVFalse("djiCamera is null, camera might be not connected. maybe even Drone");
            return;
        }

        djiCamera.setDJICameraReceivedVideoDataCallback(new DJICamera.CameraReceivedVideoDataCallback() {
            @Override
            public void onResult(byte[] bytes, int i) {

                if(bytes != null){
                    video_size = bytes.length;
                }
                else {
                    video_size = -1;
                }

                byte[] bytes_capture = bytes.clone();

                mCodecManager.sendDataToDecoder(bytes_capture,i);

                System.out.println("stam");
            }
        });

        try {
            dataV = true;
            djiCamera.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    firmwareVersion = djiError.getDescription();
                }
            });
        }
        catch (Exception e) {
            setDataVFalse("Error inside updateFromDjiCamera: "  + e.getMessage());
        }
    }

    public String toString() {

        if(dataV == false){
            return "\tCamera Info is invalid. reason: " + invalidReason + "\n";
        }
        return "\tCamera Info: " +
                "\n\t\tVideo Size      : " + video_size +
                "\n\t\tFirmwareVersion : " + firmwareVersion + "\n";
    }
}
