package bacdaibang.ui_mqtt;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by VietBac on 11/7/2016.
 */
public class MyNetwork {
    Context mContext;
    MyNetwork(Context context){
        mContext = context;
    }

    public static final String BROADCAST_INFO_JSON_DATA = "broadcast.info.json";              //Activity: ActivityMain
    public static final String BROADCAST_REQUEST_REGISTER = "broadcast.request.register";     //Activity: ActivityRegister
    public static final String BROADCAST_LAST_STATE_DEVICE = "broadcast.last.state.device";     //Activity: ActivityRegister

    public static final String EXTRA_INFO_JSON_DATA = "extra.info.json";                //Activity: ActivityMain
    public static final String EXTRA_REQUEST_REGISTER = "extra.request.register";       //Activity: ActivityRegister
    public static final String EXTRA_LAST_STATE_DEVICE = "extra.last.state.device";       //Activity: ActivityRegister

    public void getListHome(String userName){
        RequestQueue rq = Volley.newRequestQueue(mContext);
        String stringRq = "http://192.168.1.104/RespondRequest.php?type=get_info_json&user="+userName;


        JsonObjectRequest jsonR = new JsonObjectRequest(Request.Method.GET,stringRq,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(BROADCAST_INFO_JSON_DATA);
                        intent.putExtra(EXTRA_INFO_JSON_DATA,response.toString());
                        mContext.sendBroadcast(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        rq.add(jsonR);
    }
    public void requestRegister(String userName,String password,String email){
        RequestQueue rq = Volley.newRequestQueue(mContext);
        String stringRq = "http://192.168.1.104/RegistRequest.php?u="+userName+"&p="+password+"&e="+email;

        JsonObjectRequest jsonR = new JsonObjectRequest(Request.Method.GET,stringRq,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(BROADCAST_REQUEST_REGISTER);
                        intent.putExtra(EXTRA_REQUEST_REGISTER,response.toString());
                        mContext.sendBroadcast(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        rq.add(jsonR);
    }

    public void GetLastStateDevice(String userName){
        RequestQueue rq = Volley.newRequestQueue(mContext);
        String stringRq = "http://192.168.1.104/RespondRequest.php?type=get_info_json&user="+userName;

        JsonObjectRequest jsonR = new JsonObjectRequest(Request.Method.GET,stringRq,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(BROADCAST_LAST_STATE_DEVICE);
                        intent.putExtra(EXTRA_LAST_STATE_DEVICE,response.toString());
                        mContext.sendBroadcast(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        rq.add(jsonR);
    }
}