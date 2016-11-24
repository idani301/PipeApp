package InfoTypes;

import java.util.ArrayList;

import dji.common.airlink.DJISignalInformation;
import dji.sdk.airlink.DJIAirLink;
import dji.sdk.airlink.DJILBAirLink;

/**
 * Created by einav on 22/11/2016.
 */

public class AirLinkInfo extends GeneralInfoType {

    int[] channelSignalsStrength;
    ArrayList<DJISignalInformation> lightBrightSignalInfo;

    public AirLinkInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiAirLink(DJIAirLink djiAirLink){

        if(djiAirLink == null){
            setDataVFalse("djiAirLink = null inside updateFromDjiAirLink");
            return;
        }
        dataV = true;

        try {

            DJILBAirLink djiLBAirLink = djiAirLink.getLBAirLink();

            djiLBAirLink.setDJILBAirLinkUpdatedAllChannelSignalStrengthsCallback(new DJILBAirLink.DJILBAirLinkUpdatedAllChannelSignalStrengthsCallback() {
                @Override
                public void onResult(int[] ints) {
                    channelSignalsStrength = ints;
                }
            });

            djiLBAirLink.setDJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback(new DJILBAirLink.DJILBAirLinkUpdatedLightbridgeModuleSignalInformationCallback() {
                @Override
                public void onResult(ArrayList<DJISignalInformation> arrayList) {
                    lightBrightSignalInfo = arrayList;
                }
            });
        }
        catch(Exception e){
            setDataVFalse("Error inside updateFromDjiAirLink : " + e.getMessage());
        }
    }

    public String toString(){

        if(dataV == false){
            return "\tAir Link info is invalid. Reason: " + invalidReason + "\n";
        }

        String finalString = "\tAir Link Info: ";

        if(channelSignalsStrength != null && channelSignalsStrength.length > 0){
            finalString += "\n\t\tChannel Signals Strength: ";
            for(int i=0; i < channelSignalsStrength.length; i++){
                finalString += "\n\t\t\tSig[" + i + "] = " + channelSignalsStrength[i];
            }
        }
        else {
            finalString += "\n\t\tChannel Signals Strength Empty";
        }

        if(lightBrightSignalInfo != null && lightBrightSignalInfo.size() > 0){
            finalString += "\n\t\tLight Bright Signal Info : ";
            for(int i=0; i < lightBrightSignalInfo.size(); i++){
                finalString += "\n\t\t\tSig[" + i + "] = " + lightBrightSignalInfo.get(i);
            }
        }
        else{
            finalString += "\n\t\tLight Bright Signal info is empty";
        }

        finalString += "\n";

        return finalString;
    }
}
