package devices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VietBac on 11/11/2016.
 */
public class TemperatureSensor extends Device {
    boolean operationStatus;
    float value;
    String timestamp;
    String location;

    public TemperatureSensor(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp) {
        super(nameDevice, id, type, groupCode, classCode, instanceCode, nodeIp);
    }

    public void setoperationStatus(boolean operationStatus){
        this.operationStatus = operationStatus;
    }
    public void setValue(float value){
        this.value = value;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public void updateData(boolean operationStatus,float value,String timestamp){
        this.operationStatus = operationStatus;
        this.value = value;
        this.timestamp = timestamp;
    }
    public void updateDataFromJson(JSONObject jsonData) throws JSONException {
        this.operationStatus = jsonData.getBoolean("operationStatus");
        this.value = ((float) jsonData.getDouble("value"));
        this.timestamp = jsonData.getString("timestamp");
    }
    public float getValue(){return value;}
    public String getTimestamp(){return timestamp;}
    public String getLocation(){return this.location;}

}
