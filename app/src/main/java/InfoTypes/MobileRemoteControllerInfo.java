package InfoTypes;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.mobilerc.DJIMobileRemoteController;

/**
 * Created by einav on 20/11/2016.
 */

public class MobileRemoteControllerInfo extends GeneralInfoType {

    private String firmwareVersion;

    public MobileRemoteControllerInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiMobile(DJIMobileRemoteController djiMobileRemoteController){

        if(djiMobileRemoteController == null){
            setDataVFalse("Mobile remote controller not valid");
            return;
        }
        try{
            djiMobileRemoteController.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    firmwareVersion = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    firmwareVersion= djiError.getDescription();
                }
            });
        }
        catch(Exception e) {
            setDataVFalse("Error inside updateFromDjiMobile: " + e.getMessage());
        }
    }

    public String toString(){
        if(dataV == false){
            return "\tMobile Remote Controller invalid. Reason: " + invalidReason + "\n";
        }

        return "\tMobile Remote Controller Info: " +
                "\n\t\tFirmware Version : " + firmwareVersion + "\n";
    }
}
