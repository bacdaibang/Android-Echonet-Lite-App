package devices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bacdaibang on 18/03/2017.
 */

public class ElectricEnergySensor extends Device {
    boolean operationStatus;
    String timestamp;
    int value;
    String location;
    public ElectricEnergySensor(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp) {
        super(nameDevice, id, type, groupCode, classCode, instanceCode, nodeIp);
    }

    public void setOperationStatus(boolean operationStatus){
        this.operationStatus = operationStatus;
    }
    public void setValue(int value){
        this.value = value;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getLocation(){return this.location;}
    public void updateDataFromJson(JSONObject jsonData) throws JSONException {
        this.operationStatus = jsonData.getBoolean("OperationStatus");
        this.value = (int) jsonData.getDouble("value");
        this.timestamp = jsonData.getString("timestamp");
    }
}
