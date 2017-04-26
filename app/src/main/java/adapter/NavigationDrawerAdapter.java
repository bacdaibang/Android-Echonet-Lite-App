package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import bacdaibang.ui_mqtt.R;

/**
 * Created by VietBac on 12/13/2016.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> items;


    public NavigationDrawerAdapter(Context mContext){
        this.mContext = mContext;
    }
    public NavigationDrawerAdapter(Context mContext, String[] listItems){
        this.mContext = mContext;
        this.items = Arrays.asList(listItems);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =inflater.inflate(R.layout.adapter_nav_drawer_item,viewGroup,false);
        TextView text = (TextView)v.findViewById(R.id.text_nav_drawer_item);
        ImageView image = (ImageView)v.findViewById(R.id.image_nav_drawer_item);
        text.setText(items.get(i));
        image.setImageResource(R.drawable.home_icon);
        return v;
    }


}
