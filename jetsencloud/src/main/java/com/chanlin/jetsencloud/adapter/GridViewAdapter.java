package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;

/**
 * Created by ChanLin on 2018/1/9.
 * jetsenCloud
 * TODO:
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private String[] course_name= null;
    private LayoutInflater layoutInflater;
    public GridViewAdapter(Context context, String[] Strs){
        this.mContext = context;
        this.course_name = Strs;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return course_name.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.course_item, null);
            holder.course_name = (TextView) convertView.findViewById(R.id.tv_course_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.course_name.setText(course_name[position].toString());
        return convertView;
    }

    private class ViewHolder {
        private TextView course_name;
    }

}
