package es.deusto.view.Fragments.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import com.elpoeta.menulateralslide.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import es.deusto.model.services.database.dao.Noticia;

/**
 * Created by Sergio on 18/05/2016.
 */
public class ListAdapterWithImage extends BaseAdapter {

    public List<Noticia> data;
    private LayoutInflater inflater = null;

    public ListAdapterWithImage(Context c, List<Noticia> n){
        data = n;
        inflater = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate (R.layout.custom_list, parent, false);
        Noticia n = data.get(position);
        ViewHolder holder = new ViewHolder();
        holder.icon = (ImageView) convertView.findViewById(R.id.imageNewsList);
        holder.name = (TextView) convertView.findViewById(R.id.textView_Name);
        holder.desc = (TextView) convertView.findViewById(R.id.textView_Description);
        holder.name.setText(n.getTitulo());
        holder.desc.setText(n.getDescripcion());
        if(n.getImage() != null){
            ImageLoader.getInstance().displayImage(n.getImage(), holder.icon);

        }
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView desc;
        ImageView icon;
    }
}
