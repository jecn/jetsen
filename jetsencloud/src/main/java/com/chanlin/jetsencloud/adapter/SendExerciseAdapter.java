package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.QuestionPeriod;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/13.
 */
public class SendExerciseAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<QuestionPeriod> questionPeriodList = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<String>();
    private LayoutInflater layoutInflater;

    public SendExerciseAdapter(Context context, ArrayList<QuestionPeriod> questionPeriodList, ArrayList<String> list) {
        this.mContext = context;
        this.questionPeriodList = questionPeriodList;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return questionPeriodList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionPeriodList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.exercise_list_item, null);
            holder.period_name = (TextView) convertView.findViewById(R.id.period_name);
            holder.imgcheck = (ImageView) convertView.findViewById(R.id.imgbtn_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).equals("1")){
            holder.imgcheck.setImageResource(R.mipmap.period_check_on);
        } else {
            holder.imgcheck.setImageResource(R.mipmap.period_check_off);
        }
        holder.period_name.setText(questionPeriodList.get(position).getTitle());

        return convertView;
    }
    private class ViewHolder {
        private TextView period_name;
        private ImageView imgcheck;
    }
}
