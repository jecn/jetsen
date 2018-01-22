package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.Book;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/13.
 */
public class BooklistViewAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Book> mybooks = null;//教材列表
    private LayoutInflater layoutInflater;

    public BooklistViewAdapter(Context context, ArrayList<Book> mybooks) {
        this.mContext = context;
        this.mybooks = mybooks;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public void updateBookList(ArrayList<Book> books){
        this.mybooks = books;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mybooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mybooks.get(position);
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
            convertView = layoutInflater.inflate(R.layout.booklist_item, null);
            holder.book_name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.book_name.setText(mybooks.get(position).getName());

        return convertView;
    }

    private class ViewHolder {
        private TextView book_name;
    }
}
