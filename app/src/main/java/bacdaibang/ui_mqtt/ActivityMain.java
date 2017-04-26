package bacdaibang.ui_mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapter.DeviceAdapter;
import adapter.NavigationDrawerAdapter;
import devices.Device;
import devices.ElectricEnergySensor;
import devices.GasLeakSensor;
import devices.HumiditySensor;
import devices.IlluminanceSensor;
import devices.LightingNormal;
import devices.TemperatureSensor;
import fragment.LocationFragment;

public class ActivityMain extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerContent;
    ActionBarDrawerToggle mDrawerToggle;

    NavigationDrawerAdapter mNavigationDrawerAdapter;

    String[] drawerContentItems = new String[]{"Đăng Xuất"};




    BroadcastReceiver mBroadcastReceiver;
    String username;
    String password;

    public static final String EXTRA_HOME_NAME = "extra.home.name";

    public static final String BROADCAST_SUBCRIBE="broadcast.subcribe";
    public static final String BROADCAST_COMMAND="broadcast.command";
    public static final String EXTRA_COMMAND_RELAY="extra.command.relay";
    public static final String EXTRA_COMMAND_ID="extra.command.id";


    TabLayout mTablayout;
    ViewPager mViewPager;
    MyViewAdapter adapter;
    MyNetwork network;

    public List<LocationFragment> mLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initation(drawerContentItems);


        Intent intent = getIntent();
        username = intent.getStringExtra(ActivitySignIn.EXTRA_USERNAME_MQTTCONNECTION);
        password = intent.getStringExtra(ActivitySignIn.EXTRA_PASSWORD_MQTTCONNECTION);

        network = new MyNetwork(getApplicationContext());
        network.getListHome(username);

        //khoi tao viewpager voi tablayout
        mTablayout= (TabLayout)findViewById(R.id.tabs);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);






        //Xử lý sự kiện khi có dữ liệu để tao view và khi có tin nhắn từ thiết bị publish tới
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Nhận xử lý khi get thông tin danh sách các ngôi nhà, các vị trí trong mỗi ngôi nhà và các thiết bị tương ứng với từng vị trí
                if(intent.getAction().equals(MyNetwork.BROADCAST_INFO_JSON_DATA)){
                    try {
                        String infoStringData = intent.getStringExtra(MyNetwork.EXTRA_INFO_JSON_DATA);
                        JSONObject infoJsonData = new JSONObject(infoStringData);
                        String[] homes = GetHomesFromJson(infoJsonData);                        //Tất cả các ngôi nhà của người dùng

                        for(String tempHome: homes){
                            String[] locations = GetLocationsFromJson(infoJsonData,tempHome);   //Tất cả các vị trí trong nhà tương ứng với 1 ngôi nhà
                            for (String t: locations) {
                                if(PositionInListLocation(mLocations,t) == -1){
                                    mLocations.add(new LocationFragment(t));
                                }
                            }
                        }
                        for(String tempHome: homes){
                            String[] locations = GetLocationsFromJson(infoJsonData,tempHome);   //Tất cả các vị trí trong nhà tương ứng với 1 ngôi nhà
                            for (String t: locations) {
                                if(PositionInListLocation(mLocations,t) == -1){
//
                                }
                                GetDevicesFromJson(infoJsonData,tempHome,t);                    // Đưa tất cả các thiết bị đã đăng kí tới các vị trí trong nhà phù hợp
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    setupViewPager(mViewPager);
//                    mTablayout.setupWithViewPager(mViewPager);

                    //Để đảm bảo rằng các tab đã có đủ dữ liệu
//                    for(int i=0;i<mLocations.size();i++){
//                        mViewPager.setCurrentItem(i);
//                    }
//                    mViewPager.setCurrentItem(0);
//                    notifyDataChange();
//                    tep.GetLastStateDevice(username);
                }
                //Nhận xử lý tất cả các tin nhắn từ MQTT broker tới
                if(intent.getAction().equals(MqttConnection.BROADCAST_MESSAGE_MQTT_ARRIVED)){
                    String content = intent.getStringExtra(MqttConnection.EXTRA_MESSAGE_ARRIVED_CONTENT);
                    try {
                        JSONObject jsonMessage = new JSONObject(content);
                        int id = jsonMessage.getInt("id");                                      // Id của thiết bị
                        String location = jsonMessage.getString("location");                    // Vị trí của thiết bị
                        Device temp=null;
                        // Cập nhật dữ liệu của thiết bị có id trong gói tin nhận được
                        int positionInListLocation = PositionInListLocation(mLocations,location);
                        if(positionInListLocation != -1){
                            mLocations.get(positionInListLocation);

                            try{
                                temp = mLocations.get(positionInListLocation).mAdapter.getItemDeviceId(id);
                            }catch(Exception e){}

                            if(temp !=null){
                                temp.updateDataFromJson(jsonMessage);
//                                mLocations.get(positionInListLocation).mAdapter.notifyDataSetChanged();
                                notifyDataChange();
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(intent.getAction().equals(DeviceAdapter.SEND_BROADCAST_MESSSAGE_MQTT_COMMMAND)){


                    Intent command = new Intent();
                    command.setAction(BROADCAST_COMMAND);
                    String message = intent.getStringExtra(DeviceAdapter.EXTRA_COMMAND);
                    command.putExtra(DeviceAdapter.EXTRA_COMMAND, message);
                    sendBroadcast(command);

                }
                if(intent.getAction().equals(MqttConnection.BROADCAST_CONNECT_MQTT_SUCCESS)){
                    Intent sendSub = new Intent();
                    sendSub.setAction(BROADCAST_SUBCRIBE);
                }

                if(intent.getAction().equals(MyNetwork.BROADCAST_LAST_STATE_DEVICE)){
                   String content = intent.getStringExtra(MyNetwork.EXTRA_LAST_STATE_DEVICE);
                    try {
                        JSONObject jsonContent = new JSONObject(content);
                        Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();
                        //Sau khi cập nhật xong dữ liệu thì bắt đầu subcribe tới broker
                        try {
                            ActivitySignIn.mqttConnection.subcribe();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
        };
        //Đăng kí nhận xử lý tin nhắn đến, get thông tin
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(MyNetwork.BROADCAST_INFO_JSON_DATA);
        mIntentFilter.addAction(MyNetwork.BROADCAST_LAST_STATE_DEVICE);
        mIntentFilter.addAction(MqttConnection.BROADCAST_MESSAGE_MQTT_ARRIVED);
        mIntentFilter.addAction(DeviceAdapter.SEND_BROADCAST_MESSSAGE_MQTT_COMMMAND);
        registerReceiver(mBroadcastReceiver,mIntentFilter);
    }


    //Hàm này trả về danh sách các ngôi nhà của người dùng
    public String[] GetHomesFromJson(JSONObject jsonData){
        List<String> homes = new ArrayList<String>();
        Iterator<String> IHomes = jsonData.keys();
        while(IHomes.hasNext()) {
            homes.add(IHomes.next());
        }

        return homes.toArray(new String[homes.size()]);
    }

    //Hàm này trả về vị trí trong mỗi căn nhà
    public String[] GetLocationsFromJson(JSONObject jsonData, String homeNameI) throws JSONException {
        List<String> locations= new ArrayList<String>();
        Iterator<String> iHomes = jsonData.keys();
        while(iHomes.hasNext()) {
            String homeName = iHomes.next();
            if(homeName.equals(homeNameI)){
                JSONObject locationsJson = jsonData.getJSONObject(homeNameI);
                if(locationsJson != null){
                    Iterator<String> iLocations = locationsJson.keys();
                    while(iLocations.hasNext()){
                        String temp =iLocations.next();
                        locations.add(temp);
                    }
                }
                break;
            }
        }
        return locations.toArray(new String[locations.size()]);
    }
    //Hàm này trả về các thiết bị trong 1 ngôi nhà ở 1 vị trí trong nhà đó
    public void GetDevicesFromJson(JSONObject jsonData, String homeNameI, String nameLocationI) throws JSONException {
        Iterator<String> iHomes = jsonData.keys();
        while(iHomes.hasNext()) {
            String homeName = iHomes.next();
            if(homeName.equals(homeNameI)){
                JSONObject locationsJson = jsonData.getJSONObject(homeNameI);
                if(locationsJson != null){
                    Iterator<String> iLocations = locationsJson.keys();
                    while(iLocations.hasNext()){
                        if(nameLocationI.equals(iLocations.next())){
                            JSONArray devicesArrayJson = locationsJson.getJSONArray(nameLocationI);
                            for(int i=0;i<devicesArrayJson.length();i++){
                                JSONObject deviceJson = devicesArrayJson.getJSONObject(i);
                                String name_device = deviceJson.getString("name_device");
                                int id = deviceJson.getInt("id");
                                String type_device = deviceJson.getString("type_device");
                                int groupCode = deviceJson.getInt("group_code");
                                int classCode = deviceJson.getInt("class_code");
                                int instanceCode = deviceJson.getInt("instance_code");
                                String nodeIp = deviceJson.getString("node_ip");
                                int positionInList = PositionInListLocation(mLocations,nameLocationI);

                                if(positionInList!=-1){
                                    switch (name_device){
                                            case "Temperature Sensor":
                                                TemperatureSensor temp = new TemperatureSensor(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                temp.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(temp);
                                                break;
                                            case "humiditysensor":
                                                HumiditySensor hd = new HumiditySensor(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                hd.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(hd);
                                                break;
                                            case "Gasleak Sensor":
                                                GasLeakSensor gl = new GasLeakSensor(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                gl.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(gl);
                                                break;
                                            case "switch":
                                                LightingNormal ln = new LightingNormal(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                ln.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(ln);
                                                break;
                                            case "Illuminance Sensor":
                                                IlluminanceSensor il = new IlluminanceSensor(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                il.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(il);
                                                break;
                                            case "Electric Energy Sensor":
                                                ElectricEnergySensor ee = new ElectricEnergySensor(name_device,id,type_device, groupCode, classCode, instanceCode, nodeIp);
                                                ee.setLocation(nameLocationI);
                                                mLocations.get(positionInList).mDevice.add(ee);
                                                break;
                                            }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        //sau khi đã có đủ dữ liệu trong mLocations thì sẽ set up viewpager
        setupViewPager(mViewPager);
        mTablayout.setupWithViewPager(mViewPager);
        for(int i=0;i<mLocations.size();i++) {
            mViewPager.setCurrentItem(i);
        }
        mViewPager.setCurrentItem(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        network.GetLastStateDevice(username);

    }
    //Khi có dữ liệu để view thì áp dụng
    public void notifyDataChange(){
        for(int i=0;i<mLocations.size();i++){
                mLocations.get(i).mAdapter.notifyDataSetChanged();
        }
    }
    //Set up ViewPager
    public void setupViewPager(ViewPager viewPager){
        adapter = new MyViewAdapter(getSupportFragmentManager());
        Toast.makeText(getApplicationContext(),mLocations.size()+"",Toast.LENGTH_LONG).show();
        for(int i=0;i<mLocations.size();i++){
            adapter.addFragment(mLocations.get(i),mLocations.get(i).getNameLocation());
        }
        viewPager.setAdapter(adapter);
    }
    //Adapter cho các page in ViewPager
    class MyViewAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mListTitle= new ArrayList<>();

        public MyViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment,String title){
            mFragmentList.add(fragment);
            mListTitle.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitle.get(position);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        ActivitySignIn.mqttConnection.close();
        ActivitySignIn.mqttConnection = null;
    }


    public void Initation(String[] drawerContentItems){                                                                         // DrawerLayout and toggle
        //Set adapter cho listview
        mDrawerContent = (ListView)findViewById(R.id.left_drawer);
        mNavigationDrawerAdapter = new NavigationDrawerAdapter(ActivityMain.this,drawerContentItems);
        mDrawerContent.setAdapter(mNavigationDrawerAdapter);
        mDrawerContent.setOnItemClickListener(new OnClickDrawerItem());
        //Set toggle cho drawer layout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.action_bar_toggle_open,
                R.string.action_bar_toggle_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public static class MyFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public class OnClickDrawerItem implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            OnItemSelected(i);
        }
    }

    public void OnItemSelected(int position){
        MyFragment temp = new MyFragment();
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content_frame,
                temp
        );

        mDrawerContent.setItemChecked(position,true);
        mDrawerLayout.closeDrawer(mDrawerContent);
        setTitle(drawerContentItems[position]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    //Cập nhật lại trạng thái của toggle
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //hàm chỉ ra vị trí nameLocation trong danh sách các fragment vị trí. được dùng để cập nhật đúng vị trí fragment
    public int PositionInListLocation(List<LocationFragment> listLocationFragment,String nameLocation){
        for(int i=0;i<listLocationFragment.size();i++){
            if(listLocationFragment.get(i).getNameLocation().equals(nameLocation)){
                return i;
            }
        }
        return -1;
    }
}








