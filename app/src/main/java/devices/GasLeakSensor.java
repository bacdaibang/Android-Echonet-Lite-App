package devices;

/**
 * Created by VietBac on 11/12/2016.
 */
public class GasLeakSensor extends Device {
    boolean operationStatus;
    int value;
    String timestamp;
    int thresholdLevel;
    String location;


    public GasLeakSensor(String nameDevice, int id, String type, int groupCode, int classCode, int instanceCode, String nodeIp) {
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
    public void setThresholdLevel(int thresholdLevel){
        this.thresholdLevel = thresholdLevel;
    }
    public void updateData(boolean operationStatus,int value,String timestamp,int thresholdLevel){
        this.operationStatus = operationStatus;
        this.value = value;
        this.timestamp = timestamp;
        this.thresholdLevel = thresholdLevel;
    }
    public String getLocation(){return this.location;}
}
