package devices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VietBac on 12/2/2016.
 */
public class LightingNormal extends Device {
    boolean operationStatus;
    String timestamp;
    String location;

    public LightingNormal(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp) {
        super(nameDevice, id, type, groupCode, classCode, instanceCode, nodeIp);
    }
    public void updateDataFromJson(JSONObject jsonData) throws JSONException {
        this.operationStatus = jsonData.getBoolean("operationStatus");
        this.timestamp = jsonData.getString("timestamp");
    }

    public void updateStatus(boolean update){
        operationStatus =update;
    }
    public String getTimestamp(){return timestamp;}
    public String getLocation(){return this.location;}
    public boolean getOperationStatus(){return operationStatus;}
    public void setOperationStatus(boolean status){ operationStatus = status;}
}
