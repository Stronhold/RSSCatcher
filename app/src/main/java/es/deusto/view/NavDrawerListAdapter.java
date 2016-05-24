package es.deusto.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elpoeta.menulateralslide.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
         
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        NavDrawerItem drawerItem = navDrawerItems.get(position);
        if(drawerItem.getIcon() != null && drawerItem.getIcon() != "") {
            String uri = drawerItem.getIcon();
            String decodedUri = Uri.decode(uri);
            ImageLoader.getInstance().displayImage("file:///" + decodedUri, imgIcon);
        }
        else{
            String imageUri = "drawable://" + R.drawable.rss_add;
            ImageLoader.getInstance().displayImage(imageUri, imgIcon);
        }
        txtTitle.setText(drawerItem.getTitle());
        
        // displaying count
        // check whether it set visible or not
        if(drawerItem.getCounterVisibility()){
        	txtCount.setText(drawerItem.getCount());
        }else{
        	// hide the counter view
        	txtCount.setVisibility(View.GONE);
        }
        
        return convertView;
	}

}
