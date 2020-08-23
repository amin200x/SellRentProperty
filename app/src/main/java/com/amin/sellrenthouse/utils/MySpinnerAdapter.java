package com.amin.sellrenthouse.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amin.sellrenthouse.R;
import java.util.List;


public class MySpinnerAdapter extends BaseAdapter {
    private List<String> items;
    private int[] images;
    private LayoutInflater mInfalter;

    public MySpinnerAdapter(Context context, List<String> items, int[]images){
        mInfalter = LayoutInflater.from(context);
        this.items = items;
        this.images = images;
    }
    @Override
    public int getCount() {
        return items.size();

    }

    @Override
    public Object getItem(int position) {

        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        View view = mInfalter.inflate(R.layout.spinner_layout, null);
        holder = new ViewHolder();
        holder.tv = view.findViewById(R.id.textViewItem);
        holder.iv = view.findViewById(R.id.imgIcon);
        view.setTag(holder);

       holder.tv.setText(items.get(position));
       holder.iv.setImageResource(images[position]);

       return view;
    }
    static class ViewHolder{
        TextView tv;
        ImageView iv;
    }
}
