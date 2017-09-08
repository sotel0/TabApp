package com.sweettreat.ar.tabsscroller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.List;


public class CustomBaseAdapter extends BaseAdapter {
    Context context;        //context of main activity
    List<Bitmap> rowItems;  //each image
    int textViewResourceId; //the xml slayout being used

    public CustomBaseAdapter(Context context, int textViewResourceId, List<Bitmap> items) {
        this.context = context;
        this.rowItems = items;
        this.textViewResourceId = textViewResourceId;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(textViewResourceId, null);

            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.listViewImg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       // RowItem rowItem = (RowItem) getItem(position);
        Bitmap bm = (Bitmap) getItem(position);
       // holder.imageView.setImageBitmap(rowItem.getImage());
        holder.imageView.setImageBitmap(bm);

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}
