package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.QuestionPeriod;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/15.
 */
public class QuestionPeriodGridViewAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater layoutInflater;
    ArrayList<QuestionPeriod> questionPeriodList;
    public QuestionPeriodGridViewAdapter(Context context, ArrayList<QuestionPeriod> questionPeriods){
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.questionPeriodList = questionPeriods;
    }
    public void updateAdapter(ArrayList<QuestionPeriod> questionPeriods){
        this.questionPeriodList = questionPeriods;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return questionPeriodList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_question_period_grid_view, null);
            holder.course_name = (TextView) convertView.findViewById(R.id.tv_course_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final QuestionPeriod question = questionPeriodList.get(position);
        holder.course_name.setText(mContext.getResources().getString(R.string.question_period)+" "+(question.getTitle()));
        return convertView;
    }
    private class ViewHolder {
        private TextView course_name;
    }
}
