package InfoTypes;

/**
 * Created by einav on 23/11/2016.
 */

public class GeneralInfoType {
    protected boolean dataV;
    protected String invalidReason;

    public GeneralInfoType(){
        setDataVFalse("Unset Yet");
    }

    public void setDataVFalse(String reason){
        dataV = false;
        invalidReason = reason;
    }
}
