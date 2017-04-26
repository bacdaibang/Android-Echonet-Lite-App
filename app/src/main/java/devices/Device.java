package devices;

import org.json.JSONException;
import org.json.JSONObject;

import bacdaibang.ui_mqtt.R;

/**
 * Created by VietBac on 10/25/2016.
 */
public class Device {

    private String nameDevice;
    private int value;
    private int id;
    private String type;
    private int iCon;
    private int groupCode;
    private int classCode;
    private int instanceCode;
    private String nodeIp;

    public Device(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp){
        super();

        this.type=type;
        this.nameDevice=nameDevice;
        this.id = id;
        this.groupCode = groupCode;
        this.classCode = classCode;
        this.instanceCode = instanceCode;
        this.nodeIp = nodeIp;

        if(nameDevice.equalsIgnoreCase("Temperature Sensor")){
            this.iCon = R.drawable.temperature;
        }else if(nameDevice.equalsIgnoreCase("Humidity Sensor")){
            this.iCon = R.drawable.humidity;
        }else if(nameDevice.equalsIgnoreCase("Gasleak Sensor")){
            this.iCon = R.drawable.gas;
        }else if(nameDevice.equalsIgnoreCase("Switch")){
            this.iCon = R.drawable.light;
        }else if(nameDevice.equalsIgnoreCase("Illuminance Sensor")){
            this.iCon = R.drawable.bright;
        }
    }
    public void updateStatus(boolean update){}
    public void updateDataFromJson(JSONObject jsonData) throws JSONException {};
    public String getName(){
        return  this.nameDevice;
    }
    public int getId(){
        return this.id;
    }

    public int getGroupCode(){
        return this.groupCode;
    }

    public int getClassCode() {
        return classCode;
    }

    public int getInstanceCode() {
        return instanceCode;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public float getValue(){
        return this.value;
    }
    public void setValue(int value){
        this.value =value;
    }
    public String getType(){return this.type;}
    public int getIcon(){ return this.iCon;}
    public String getTimestamp(){return "";}
    public String getLocation(){return "";}
    public void setLocation(String location){}
    public boolean getOperationStatus(){return false;}
    public void setOperationStatus(boolean status){}
}
