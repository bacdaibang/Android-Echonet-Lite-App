package devices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bacdaibang on 19/03/2017.
 */

public class Switch extends Device {
    boolean operationStatus;
    String timestamp;

    public Switch(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp) {
        super(nameDevice, id, type, groupCode, classCode, instanceCode, nodeIp);
    }
    public boolean getOperationStatus(){return this.operationStatus;}
    public void setOperationStatus(boolean status){
        this.operationStatus = status;
    }
    public void updateDataFromJson(JSONObject jsonData) throws JSONException {
        this.operationStatus = jsonData.getBoolean("OperationStatus");
        this.timestamp = jsonData.getString("time_comming");
    }
}
