package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chanlin.jetsencloud.JetsenResourceActivity;
import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.controller.QuestionController;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.database.DatabaseUtils;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.util.SDCardUtils;
import com.chanlin.jetsencloud.util.StringUtils;
import com.chanlin.jetsencloud.util.ToastUtils;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/12.
 * jetsenCloud
 * TODO:
 */

public class QuestionAdapter extends BaseAdapter {
    private static  final String TAG = "QuestionAdapter";
    Context mContext;
    private ListView listView;
    LayoutInflater layoutInflater;
    QuestionController questionController;
    public static int pubPosition;//点击下载的是哪一个position
    ArrayList<QuestionPeriod> list = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MessageConfig.question_period_details_http_success_MESSAGE:
                    ToastUtils.shortToast(mContext,"下载完成！");

                   // list = questionController.getQuestionPeriodList();
                    //更新列表
                    updateItem();
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

    public void setListView(ListView view){
        this.listView = view;
    }
    /**
     * 刷新item
     */
    private void updateItem(){
        if (listView == null){
            return;
        }
        // 获取当前可以看到的item位置 
        int visiblePosition = listView.getFirstVisiblePosition();
        // 获取点击的view  
        View view = listView.getChildAt(pubPosition - visiblePosition);
        //list.get(pubPosition).setIsDownload("1");
        QuestionPeriod questionPeriod = (QuestionPeriod) getItem(pubPosition);
        ImageView iv = (ImageView) view.findViewById(R.id.down);
        iv.clearAnimation();
        iv.setImageResource(R.mipmap.img_delete);
        //刷新数据库
        DatabaseService.createQuestionPeriodTable(questionPeriod.getCourse_standard_id(),questionPeriod.getId(),questionPeriod.getTitle(),questionPeriod.getIsDownload());

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        final String isDownload = question.getIsDownload();
        if (!StringUtils.isEmpty(isDownload) && "1".equals(isDownload)){//1 表示已经下载了，
            hodler.down.setImageResource(R.mipmap.img_delete);
        }else {
            hodler.down.setImageResource(R.mipmap.img_download);
        }
        hodler.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(isDownload) && "1".equals(isDownload)){
                    question.setIsDownload("0");
                    hodler.down.setImageResource(R.mipmap.img_download);
                }else {
                    //动态授权
                    if (!JetsenResourceActivity.mIsGrant){
                        ToastUtils.shortToast(mContext,R.string.no_permission);
                        return;
                    }
                    if(SDCardUtils.isSDCardEnable()){
                        String fileDir = SDCardUtils.getSDCardPath() + SDCardUtils.questionJsonFile;
                        //下载json列表里面需要获取后循环下载
                        if (!CommonUtils.isNetworkAvailable(mContext)) {
                            ToastUtils.shortToast(mContext,R.string.http_exception);
                            return;
                        }
                        pubPosition = position;
                        hodler.down.setImageResource(R.mipmap.img_loading);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
                        LinearInterpolator lin = new LinearInterpolator();
                        animation.setInterpolator(lin);
                        if (animation != null){
                            hodler.down.startAnimation(animation);
                        }

                        questionController.getQuestionPeriodDetailList(question.getCourse_standard_id(),question.getId());

                    }else {
                        ToastUtils.shortToast(mContext,R.string.no_sdcard);
                    }
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
