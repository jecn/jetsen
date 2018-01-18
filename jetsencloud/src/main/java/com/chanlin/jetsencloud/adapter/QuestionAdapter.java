package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanlin.jetsencloud.JetsenResourceActivity;
import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.controller.QuestionController;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.util.SDCardUtils;
import com.chanlin.jetsencloud.util.ToastUtils;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/12.
 * jetsenCloud
 * TODO:
 */

public class QuestionAdapter extends BaseAdapter {
    private static  final String TAG = "ResourceAdapter";
    Context mContext;
    LayoutInflater layoutInflater;
    QuestionController questionController;
    ArrayList<QuestionPeriod> list = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageConfig.question_period_details_http_success_MESSAGE:
                    ToastUtils.shortToast(mContext,"下载完成！");
                   // list = questionController.getQuestionPeriodList();
                    break;

            }
        }
    };
    public QuestionAdapter(Context context, ArrayList<QuestionPeriod> resourceTreeArrayList){
        mContext = context;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        questionController = new QuestionController(mContext,mHandler);
        this.list = resourceTreeArrayList;
    }
    public void updateList(ArrayList<QuestionPeriod> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHodler hodler;
        if (convertView == null){
            hodler = new ViewHodler();
            view = layoutInflater.inflate(R.layout.item_down_question,parent,false);
            hodler.file_title = (TextView) view.findViewById(R.id.file_title);
            hodler.down = (ImageView) view.findViewById(R.id.down);
            view.setTag(hodler);
        }else{
            hodler = (ViewHodler) convertView.getTag();
        }
        final QuestionPeriod question = list.get(position);
        hodler.file_title.setText(mContext.getResources().getString(R.string.question_period)+" "+(question.getTitle()));
        hodler.down.setImageResource(R.mipmap.img_download);
        hodler.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态授权
                if (!JetsenResourceActivity.mIsGrant){
                    ToastUtils.shortToast(mContext,R.string.no_permission);
                    return;
                }
                if(SDCardUtils.isSDCardEnable()){
                    String fileDir = SDCardUtils.getSDCardPath() + SDCardUtils.questionJsonFile;
                    //下载json列表里面需要获取后循环下载
                    questionController.getQuestionPeriodDetailList(question.getCourse_standard_id(),question.getId());

                }else {
                    ToastUtils.shortToast(mContext,R.string.no_sdcard);
                }
            }
        });
        return view;
    }

    class ViewHodler{

        TextView file_title;
        ImageView down;
    }
}
