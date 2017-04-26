package bacdaibang.ui_mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by VietBac on 12/11/2016.
 */
public class ActivityRegister extends AppCompatActivity {
    Button btnRegister;
    EditText username,password,email;
    BroadcastReceiver mBroadcastReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = username.getText().toString();
                String passwordInput = password.getText().toString();
                String emailInput = email.getText().toString();

                if(usernameInput.length()==0 || passwordInput.length()==0 || emailInput.length()==0){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập đủ các trường",Toast.LENGTH_LONG).show();
                }else{
                    username.setText("");
                    password.setText("");
                    email.setText("");
                    MyNetwork requestRegister = new MyNetwork(getApplicationContext());
                    requestRegister.requestRegister(usernameInput,passwordInput,emailInput);
                }
            }
        });

        mBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(MyNetwork.BROADCAST_REQUEST_REGISTER)){
                    String jsonString = intent.getStringExtra(MyNetwork.EXTRA_REQUEST_REGISTER);
                    Log.e("Value of jSon register",jsonString);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String result = jsonObject.getString("result");
                        if(result.equals("false")){
                            Toast.makeText(getApplicationContext(),"Đăng kí thành công",Toast.LENGTH_LONG).show();
                            Intent gotoRegister = new Intent(ActivityRegister.this,ActivitySignIn.class);
                            startActivity(gotoRegister);
                        }else{
                            Toast.makeText(getApplicationContext(),"Đăng kí không thành công do có tài khoản khác",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(MyNetwork.BROADCAST_REQUEST_REGISTER);
        registerReceiver(mBroadcastReceiver,mFilter);
    }
}
