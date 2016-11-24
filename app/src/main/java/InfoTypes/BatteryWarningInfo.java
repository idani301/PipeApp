package InfoTypes;

import dji.common.battery.DJIBatteryWarningInformation;

/**
 * Created by einav on 21/11/2016.
 */

public class BatteryWarningInfo extends GeneralInfoType {

    private boolean currentOverload        = false;
    private boolean overHeating            = false;
    private boolean lowTemperature         = false;
    private boolean shortCircuit           = false;
    private boolean customDischargeEnabled = false;
    private short underVoltageBatteryCellIndex = 0;
    private short damagedBatteryCellIndex      = 0;

    public BatteryWarningInfo(){
        setDataVFalse("Unset yet");
    }

    public void updateFromDjiBatteryWarningInfo(DJIBatteryWarningInformation djiBatteryWarningInformation){

        if(djiBatteryWarningInformation == null){
            setDataVFalse("djiBatteryWarningInformation is null. check this issue.");
            return;
        }

        try {
            currentOverload = djiBatteryWarningInformation.isCurrentOverload();
            overHeating = djiBatteryWarningInformation.isOverHeating();
            lowTemperature = djiBatteryWarningInformation.isLowTemperature();
            shortCircuit = djiBatteryWarningInformation.isShortCircuit();
            customDischargeEnabled = djiBatteryWarningInformation.isCustomDischargeEnabled();
            underVoltageBatteryCellIndex = djiBatteryWarningInformation.getUnderVoltageBatteryCellIndex();
            damagedBatteryCellIndex = djiBatteryWarningInformation.getDamagedBatteryCellIndex();
            dataV = true;
        }
        catch (Exception e) {
            setDataVFalse("Error inside updateFromDjiBatteryWarningInfo: " + e.getMessage());
        }
    }

    public boolean hasError() {
        return this.currentOverload || this.overHeating || this.lowTemperature || this.shortCircuit || this.underVoltageBatteryCellIndex != 0 || this.damagedBatteryCellIndex != 0 || this.customDischargeEnabled;
    }

    public String toString(int numberOfSpaces){

        if(dataV = false){
            return "Battery Warning Info not valid. Reason: " + invalidReason + "\n";
        }

        String space = "\n";
        for(int i=0; i<numberOfSpaces; i++){
            space += "\t";
        }

        return "Warning Info" + space +
                "currentOverload             : " + currentOverload + space +
                "overHeating                 : " + overHeating + space +
                "lowTemperature              : " + lowTemperature + space +
                "shortCircuit                : " + shortCircuit + space +
                "customDischargeEnabled      : " + customDischargeEnabled + space +
                "UnderVoltageBatteryCellIndex: " + underVoltageBatteryCellIndex + space +
                "damagedBatteryCellIndex     : " + damagedBatteryCellIndex + space +
                "hasError                    : " + hasError() + "\n";
    }
}
