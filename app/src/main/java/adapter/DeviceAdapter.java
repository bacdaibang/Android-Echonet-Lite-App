package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bacdaibang.ui_mqtt.R;
import devices.Device;

/**
 * Created by VietBac on 10/25/2016.
 */
public class DeviceAdapter extends BaseAdapter{
    public final static String SEND_BROADCAST_MESSSAGE_MQTT_COMMMAND = "send.messsage.command.mqtt";
    public final static String EXTRA_DEVICE_NAME_SEND_COMMAND = "extra.devicename.command";
    public final static String EXTRA_DEVICE_ID_SEND_COMMAND = "extra.deviceid.command";
    public final static String EXTRA_DEVICE_RELAY_SEND_COMMAND = "extra.deviceid.command";
    public final static String EXTRA_COMMAND = "extra.command";

    List<Device> mDevice;
    Context mContext;


    public DeviceAdapter(Context mContext, List<Device> mDevice){
        this.mContext=mContext;
        this.mDevice=mDevice;
    }


    @Override
    public int getCount() {
        return this.mDevice.size();
    }

    @Override
    public Device getItem(int i) {
        return mDevice.get(i);
    }
    public Device getItemDeviceId(int id){
        for(int i=0;i<mDevice.size();i++){
            if(mDevice.get(i).getId() == id){
                return mDevice.get(i);
            }
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=null;
        if(mDevice.get(i).getGroupCode() == 0){
            row = layoutInf.inflate(R.layout.item_device_sensor,viewGroup,false);
            TextView nameDevice = (TextView)row.findViewById(R.id.nameDevice);
            ImageView imageDevice = (ImageView)row.findViewById(R.id.imageDevice);
            TextView valueDevice = (TextView)row.findViewById(R.id.value);
            TextView timestamp = (TextView)row.findViewById(R.id.timestamp);
            TextView unit = (TextView)row.findViewById(R.id.unit);

            if(mDevice.get(i).getClassCode() == 18){
                nameDevice.setText("Độ ẩm");
                unit.setText("%");
            }
        }else if(mDevice.get(i).getGroupCode() == 5){
            row = layoutInf.inflate(R.layout.item_device_relay,viewGroup,false);
            TextView nameDevice = (TextView)row.findViewById(R.id.nameDevice);
            ImageView imageDevice = (ImageView)row.findViewById(R.id.imageDevice);
            final int id = mDevice.get(i).getId();
            final Switch switchLighting = (Switch)row.findViewById(R.id.status);
            if(mDevice.get(i).getClassCode() == 1533){
                switchLighting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        switchLighting.setChecked(isChecked);
                        Intent commandSend = new Intent();
                        commandSend.setAction(SEND_BROADCAST_MESSSAGE_MQTT_COMMMAND);
                        JSONObject messageCommand = new JSONObject();
                        if(isChecked){
                            try {
                                messageCommand.put("id",id);
                                messageCommand.put("group_code",mDevice.get(i).getGroupCode());
                                messageCommand.put("class_code",mDevice.get(i).getClassCode());
                                messageCommand.put("instance_code",mDevice.get(i).getInstanceCode());
                                messageCommand.put("node_ip",mDevice.get(i).getNodeIp());
                                messageCommand.put("OperationStatus","true");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                messageCommand.put("id",id);
                                messageCommand.put("group_code",mDevice.get(i).getGroupCode());
                                messageCommand.put("class_code",mDevice.get(i).getClassCode());
                                messageCommand.put("instance_code",mDevice.get(i).getInstanceCode());
                                messageCommand.put("node_ip",mDevice.get(i).getNodeIp());
                                messageCommand.put("OperationStatus","false");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        commandSend.putExtra(EXTRA_COMMAND,messageCommand.toString());
                        mContext.sendBroadcast(commandSend);
                    }
                });
                nameDevice.setText(mDevice.get(i).getName());
                imageDevice.setImageResource(mDevice.get(i).getIcon());
            }
        }

//        if(mDevice.get(i).getType().equals("Sensor")){
//            row = layoutInf.inflate(R.layout.item_device_sensor,viewGroup,false);
//            TextView nameDevice = (TextView)row.findViewById(R.id.nameDevice);
//            ImageView imageDevice = (ImageView)row.findViewById(R.id.imageDevice);
//            TextView valueDevice = (TextView)row.findViewById(R.id.value);
//            TextView timestamp = (TextView)row.findViewById(R.id.timestamp);
//            TextView unit = (TextView)row.findViewById(R.id.unit);
//
//            if(mDevice.get(i).getName().equalsIgnoreCase("Temperature Sensor")){
//                nameDevice.setText("Nhiệt Độ");
//                unit.setText("*C");
//            }else if(mDevice.get(i).getName().equalsIgnoreCase("Humidity Sensor")){
//                nameDevice.setText("Độ ẩm");
//                unit.setText("%");
//            }else if(mDevice.get(i).getName().equalsIgnoreCase("Gasleak Sensor")){
//                nameDevice.setText("Rò rỉ gas");
//                unit.setText("");
//            }else if(mDevice.get(i).getName().equalsIgnoreCase("Illuminance Sensor")){
//                nameDevice.setText("Ánh sáng");
//                unit.setText("Lux");
//            }
//            valueDevice.setText(""+mDevice.get(i).getValue()+"");
//            imageDevice.setImageResource(mDevice.get(i).getIcon());
//            timestamp.setText(""+mDevice.get(i).getTimestamp()+"");
//        }else if(mDevice.get(i).getType().equalsIgnoreCase("Switch")){
//            row = layoutInf.inflate(R.layout.item_device_relay,viewGroup,false);
//            TextView nameDevice = (TextView)row.findViewById(R.id.nameDevice);
//            ImageView imageDevice = (ImageView)row.findViewById(R.id.imageDevice);
//            final int id = mDevice.get(i).getId();
//            final Switch switchLighting = (Switch)row.findViewById(R.id.status);
////            switchLighting.setChecked(mDevice.get(i).getOperationStatus());
////            switchLighting.setTextOff("Tắt");
////            switchLighting.setTextOn("Bật");
//            //Khi có thay đổi về trạng thái của switch thì sẽ gửi broadcast 1 message dạng json có thông điệp operation status sẽ được thay đổi
//            switchLighting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    switchLighting.setChecked(isChecked);
//                    Intent commandSend = new Intent();
//                    commandSend.setAction(SEND_BROADCAST_MESSSAGE_MQTT_COMMMAND);
//                    JSONObject messageCommand = new JSONObject();
//                    if(isChecked){
//                        try {
//                            messageCommand.put("id",id);
//                            messageCommand.put("group_code",mDevice.get(i).getGroupCode());
//                            messageCommand.put("class_code",mDevice.get(i).getClassCode());
//                            messageCommand.put("instance_code",mDevice.get(i).getInstanceCode());
//                            messageCommand.put("node_ip",mDevice.get(i).getNodeIp());
//                            messageCommand.put("OperationStatus","true");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }else{
//                        try {
//                            messageCommand.put("id",id);
//                            messageCommand.put("group_code",mDevice.get(i).getGroupCode());
//                            messageCommand.put("class_code",mDevice.get(i).getClassCode());
//                            messageCommand.put("instance_code",mDevice.get(i).getInstanceCode());
//                            messageCommand.put("node_ip",mDevice.get(i).getNodeIp());
//                            messageCommand.put("OperationStatus","false");
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    commandSend.putExtra(EXTRA_COMMAND,messageCommand.toString());
//                    mContext.sendBroadcast(commandSend);
//                }
//            });
//
//
//            if(mDevice.get(i).getName().equalsIgnoreCase("Lighting")){
//                nameDevice.setText("Bóng đèn thường");
//            }else{
//                nameDevice.setText("This is Switch'Name");
//            }
//            imageDevice.setImageResource(mDevice.get(i).getIcon());
//        }
//        row.setTag(getItem(i).getId());
        return row;
    }
}
