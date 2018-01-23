package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.entity.QuestionPeriod;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/23.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Context mContext;
    ArrayList<QuestionPeriod> questionPeriodList;
    private int click_item = 0;

    private OnItemClickListener mItemClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<QuestionPeriod> questionPeriods) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.questionPeriodList = questionPeriods;
    }

    public void updateAdapter(ArrayList<QuestionPeriod> questionPeriods){
        click_item = 0;
        this.questionPeriodList = questionPeriods;
        notifyDataSetChanged();
    }

    public void updateAdapter(int position, ArrayList<QuestionPeriod> questionPeriods){
        this.click_item = position;
        this.questionPeriodList = questionPeriods;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_question_period_grid_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        viewHolder.mTxt = (TextView) view.findViewById(R.id.tv_course_name);
        viewHolder.mImageView = (ImageView) view.findViewById(R.id.view_scroll);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewholder, int position) {
        final QuestionPeriod question = questionPeriodList.get(position);
        viewholder.mTxt.setText(mContext.getResources().getString(R.string.question_period) + "" + question.getTitle());
        if (position == click_item){
            viewholder.mTxt.setTextColor(mContext.getResources().getColor(R.color.colorTitle));
            viewholder.mImageView.setVisibility(View.VISIBLE);
        } else {
            viewholder.mTxt.setTextColor(mContext.getResources().getColor(R.color.common_title_bg));
            viewholder.mImageView.setVisibility(View.INVISIBLE);
        }
        viewholder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return questionPeriodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        TextView mTxt;
        ImageView mImageView;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
