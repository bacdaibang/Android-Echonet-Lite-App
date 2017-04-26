package bacdaibang.ui_mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by VietBac on 12/2/2016.
 */
public class ActivitySignIn extends AppCompatActivity {
    public static String EXTRA_USERNAME_MQTTCONNECTION = "extra.username.mqttconnection";
    public static String EXTRA_PASSWORD_MQTTCONNECTION = "extra.password.mqttconnection";
    Button signInBtn,registBtn;
    TextView userName,passWord;
    BroadcastReceiver mBroadcastReceiver;
    public static MqttConnection mqttConnection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);

        mBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(MqttConnection.BROADCAST_CONNECT_MQTT_FAIL)){
                    Toast.makeText(ActivitySignIn.this,"Connection Fail",Toast.LENGTH_LONG).show();
                }
                if(intent.getAction().equals(MqttConnection.BROADCAST_CONNECT_MQTT_SUCCESS)){
                    Toast.makeText(ActivitySignIn.this,"Change Activity",Toast.LENGTH_LONG).show();
                    Intent mIntent = new Intent(getApplicationContext(),ActivityMain.class);
                    mIntent.putExtra(EXTRA_USERNAME_MQTTCONNECTION,userName.getText().toString());
                    mIntent.putExtra(EXTRA_PASSWORD_MQTTCONNECTION,passWord.getText().toString());
                    startActivity(mIntent);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(MqttConnection.BROADCAST_CONNECT_MQTT_FAIL);
        filter.addAction(MqttConnection.BROADCAST_CONNECT_MQTT_SUCCESS);
        registerReceiver(mBroadcastReceiver,filter);


        signInBtn =(Button)findViewById(R.id.signInBtn);
        userName = (TextView)findViewById(R.id.userName);
        passWord = (TextView)findViewById(R.id.passWord);
        registBtn = (Button)findViewById(R.id.registerBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mqttConnection = new MqttConnection(getApplicationContext(),userName.getText().toString(),passWord.getText().toString(),"tcp://192.168.1.104:1883",1883);
                try {
                    mqttConnection.startConnection();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRegister = new Intent(ActivitySignIn.this,ActivityRegister.class);
                startActivity(gotoRegister);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }
}
