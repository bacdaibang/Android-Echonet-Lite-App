package bacdaibang.ui_mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;

import adapter.DeviceAdapter;

/**
 * Created by VietBac on 11/10/2016.
 */
public class MqttConnection{

    public final static String BROADCAST_CONNECT_MQTT_SUCCESS= "broadcast.connect.success.mqtt";    //dang ki: ActivityMain
    public final static String BROADCAST_CONNECT_MQTT_FAIL= "broadcast.connect.failed.mqtt";        //dang ki: ActivitySignin
    public final static String BROADCAST_CONNECT_MQTT_LOST= "broadcast.connect.lost.mqtt";

    public final static String BROADCAST_SUBCRIBE_MQTT_SUCCESS= "broadcast.subcribe.success.mqtt";
    public final static String BROADCAST_SUBCRIBE_MQTT_FAIL= "broadcast.subcribe.failed.mqtt";

    public final static String BROADCAST_MESSAGE_MQTT_ARRIVED= "broadcast.messsage.arrived.mqtt";



    public final static String EXTRA_MESSAGE_ARRIVED_CONTENT = "extra.message.arrived.content";
    public final static String EXTRA_MESSAGE_ARRIVED_QOS = "extra.message.arrived.qos";
    public final static String EXTRA_MESSAGE_ARRIVED_TIMESTAMP = "extra.message.arrived.timestamp";
    public final static String EXTRA_MESSAGE_ARRIVED_TOPIC = "extra.message.arrived.topic";



    private String userName;
    private String passWord;
    private String host;
    private int port;
    private Context mContext;

    private MqttAndroidClient client;
    private MqttService service;
    private BroadcastReceiver mBroadcastReceiver;

    public MqttConnection(final Context mContext, final String userName, String passWord, String host, int port){
        this.mContext = mContext;
        this.userName = userName;
        this.passWord = passWord;
        this.host = host;
        this.port = port;
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(ActivityMain.BROADCAST_COMMAND)){


                    String message = intent.getStringExtra(DeviceAdapter.EXTRA_COMMAND);
                    try {
                        Toast.makeText(mContext,"SENDING: "+message,Toast.LENGTH_SHORT).show();
                        client.publish("user/"+userName+"/command",message.getBytes(),1,false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                if(intent.getAction().equals(ActivityMain.BROADCAST_SUBCRIBE)){
                    try {
                        subcribe();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BROADCAST_CONNECT_MQTT_SUCCESS);
//        intentFilter.addAction(BROADCAST_CONNECT_MQTT_FAIL);
        intentFilter.addAction(ActivityMain.BROADCAST_SUBCRIBE);
        intentFilter.addAction(BROADCAST_SUBCRIBE_MQTT_SUCCESS);
        intentFilter.addAction(BROADCAST_SUBCRIBE_MQTT_FAIL);

        intentFilter.addAction(ActivityMain.BROADCAST_COMMAND);
        mContext.registerReceiver(mBroadcastReceiver,intentFilter);

    }
    public void startConnection() throws MqttException {
        client = new MqttAndroidClient(mContext,host, MqttClient.generateClientId());

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Intent lostConnect =  new Intent(BROADCAST_CONNECT_MQTT_LOST);
                mContext.sendBroadcast(lostConnect);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Intent messageArrived = new Intent(BROADCAST_MESSAGE_MQTT_ARRIVED);

                Log.e("MQTT Connnection","Message Arrived: "+message.toString());
                messageArrived.putExtra(EXTRA_MESSAGE_ARRIVED_CONTENT,message.toString());
                messageArrived.putExtra(EXTRA_MESSAGE_ARRIVED_QOS,message.getQos());
                messageArrived.putExtra(EXTRA_MESSAGE_ARRIVED_TOPIC,topic);
                Calendar c = java.util.Calendar.getInstance();
                int second = c.get(java.util.Calendar.SECOND);
                int minute = c.get(Calendar.MINUTE);
                int hour = c.get(Calendar.HOUR);
                String timestamp =  ""+second+" : "+minute+" : "+hour+"";
                messageArrived.putExtra(EXTRA_MESSAGE_ARRIVED_TIMESTAMP,timestamp);
                mContext.sendBroadcast(messageArrived);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions option = new MqttConnectOptions();
        option.setUserName(userName);
        option.setPassword(passWord.toCharArray());
        IMqttToken connectCallback = client.connect(option);
        connectCallback.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.e("MQTT connection","Successed");
                Intent connected = new Intent(BROADCAST_CONNECT_MQTT_SUCCESS);
                mContext.sendBroadcast(connected);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e("MQTT connection","Fail"+exception.toString());
                Intent failed = new Intent(BROADCAST_CONNECT_MQTT_FAIL);
                mContext.sendBroadcast(failed);
            }
        });

    }

    public void subcribe() throws MqttException {
        IMqttToken subcribeCallback = client.subscribe(userName,0);
        subcribeCallback.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Intent subcribed = new Intent(BROADCAST_SUBCRIBE_MQTT_SUCCESS);
                mContext.sendBroadcast(subcribed);
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Intent failed = new Intent(BROADCAST_SUBCRIBE_MQTT_FAIL);
                mContext.sendBroadcast(failed);
            }

        });

    }

    public void unSubcribe(){
        try {
            client.unsubscribe(userName);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        client.close();
    }

}
