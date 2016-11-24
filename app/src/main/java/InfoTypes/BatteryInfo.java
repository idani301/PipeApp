package InfoTypes;

import dji.common.battery.DJIBatteryState;
import dji.common.battery.DJIBatteryWarningInformation;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.battery.DJIBattery;
import eyesatop.pipeapp.PipeApp;

/**
 * Battery Info contains all the needed data for a battery of a drone.
 */
public class BatteryInfo extends GeneralInfoType {

    private String batteryFirmware;
    private int batteryRemainingPercent;
    private float batteryTemperature;
    private int lifeTimeRemainingPercent;
    private BatteryWarningInfo warningInfo = new BatteryWarningInfo();

    /**
     * Constructor.
     * By default sets the dataV to false;
     */
    public BatteryInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiBattery(DJIBattery djiBattery){

        dataV = true;

        if(djiBattery == null){
            setDataVFalse("djiBattery is null. Drone probably not connected");
            return;
        }

        try {
            djiBattery.setBatteryStateUpdateCallback(new DJIBattery.DJIBatteryStateUpdateCallback() {
                @Override
                public void onResult(DJIBatteryState djiBatteryState) {
                    batteryRemainingPercent  = djiBatteryState.getBatteryEnergyRemainingPercent();
                    batteryTemperature       = djiBatteryState.getBatteryTemperature();
                    lifeTimeRemainingPercent = djiBatteryState.getLifetimeRemainingPercent();
                }
            });
            djiBattery.getFirmwareVersion(new DJICommonCallbacks.DJICompletionCallbackWith<String>() {
                @Override
                public void onSuccess(String s) {
                    batteryFirmware = s;
                }

                @Override
                public void onFailure(DJIError djiError) {
                    batteryFirmware = djiError.getDescription();
                }
            });

            djiBattery.getCurrentWarningInformation(new DJICommonCallbacks.DJICompletionCallbackWith<DJIBatteryWarningInformation>() {
                @Override
                public void onSuccess(DJIBatteryWarningInformation djiBatteryWarningInformation) {
                    warningInfo.updateFromDjiBatteryWarningInfo(djiBatteryWarningInformation);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    warningInfo.setDataVFalse(djiError.getDescription());
                }
            });
        }
        catch(Exception e){
            PipeApp.nonFatalError("Couldn't update battery info");
            setDataVFalse("Had Error inside updateFromDjiBattery: " + e.getMessage());
        }
    }

    public String toString(){

        if(dataV == false){
            return "\tBattery Info: Not valid. Reason: " +  invalidReason + "\n";
        }

        return "\tBattery Info:" +
                "\n\t\tFirmware         : " + batteryFirmware +
                "\n\t\tTemperature      : " + batteryTemperature +
                "\n\t\tRemaining Percent: " + batteryRemainingPercent + "%" +
                "\n\t\tLifeTime Percent : " + lifeTimeRemainingPercent + "%" +
                "\n\t\t" + warningInfo.toString(3);
    }
}
