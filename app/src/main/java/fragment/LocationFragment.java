package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.DeviceAdapter;
import bacdaibang.ui_mqtt.R;
import devices.Device;

/**
 * Created by VietBac on 12/21/2016.
 */
public class LocationFragment extends Fragment {
    public ArrayList<Device> mDevice = new ArrayList<>();
    public ListView mListView=null;
    public DeviceAdapter mAdapter=null;
    private String nameLocation;

    public LocationFragment(){}
    public LocationFragment(String nameLocation){
        this.nameLocation = nameLocation;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location,container,false);
        this.mListView = (ListView) v.findViewById(R.id.myListView);
        this.mAdapter = new DeviceAdapter(getContext(), mDevice);
        this.mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),mDevice.get(position).getId()+"",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
    public String getNameLocation(){
        return nameLocation;
    }
}
