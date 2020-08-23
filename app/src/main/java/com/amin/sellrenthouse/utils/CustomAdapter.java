package com.amin.sellrenthouse.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amin.sellrenthouse.R;
import com.amin.sellrenthouse.entities.House;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private List<House> list;
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, List<House> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;

    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = mInflater.inflate(R.layout.custom_layout, null);
        holder = new ViewHolder();
        holder.tv =  view.findViewById(R.id.textViewItem);
        holder.iv =  view.findViewById(R.id.imgIcon);
        view.setTag(holder);

        House item = list.get(position);
        holder.tv.setText(item.toString());
        holder.iv.setImageResource(item.getType().getImage());

        if (position % 2 == 1){
            int intColor = Color.parseColor("#CAF0DD");
            view.setBackgroundColor(intColor);
        }
        if (position % 2 == 0){
            int intColor = Color.parseColor("#B9F3D6");
            view.setBackgroundColor(intColor);
        }
        return view;
    }

    static class ViewHolder
    {
        TextView tv;
        ImageView iv;

    }

}