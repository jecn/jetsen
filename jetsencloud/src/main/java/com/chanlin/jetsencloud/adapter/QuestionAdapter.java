package com.chanlin.jetsencloud.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.chanlin.jetsencloud.QuestionViewActivity;
import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.controller.QuestionController;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.database.DatabaseUtils;
import com.chanlin.jetsencloud.entity.QuestionContent;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.util.FileUtils;
import com.chanlin.jetsencloud.util.SDCardUtils;
import com.chanlin.jetsencloud.util.StringUtils;
import com.chanlin.jetsencloud.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
                    ToastUtils.shortToast(mContext,R.string.download_success);

                   // list = questionController.getQuestionPeriodList();
                    //更新列表
                    Object ob = msg.obj;
                    updateItem(ob);
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
    private Handler mMainHandler;
    public void setActivityHandler(Handler mMainHandler){
        this.mMainHandler = mMainHandler;
    }
    public void setListView(ListView view){
        this.listView = view;
    }
    /**
     * 刷新item
     */
    private void updateItem(Object ob){
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
        questionPeriod.setIsDownload("1");
        notifyDataSetChanged();
        //刷新数据库
        DatabaseService.updateQuestionPeriodTable(questionPeriod.getCourse_standard_id(),questionPeriod.getId(),questionPeriod.getTitle(),questionPeriod.getIsDownload());
/*
        //判断是否是全部下载
        if (ob != null){
            String all = ob.toString();
            mMainHandler.sendEmptyMessage(MessageConfig.download_resource_question_message);
        }*/
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
        //hodler.file_title.setText(mContext.getResources().getString(R.string.question_period)+" "+(question.getTitle()));
        hodler.file_title.setText(question.getTitle());
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
                    //删除题目,先删课时，再删题目，删本地题目文件
                    DatabaseService.deleteQuestionPeriodListAnddetails(question.getCourse_standard_id());

                    question.setIsDownload("0");
                    hodler.down.setImageResource(R.mipmap.img_download);
                    notifyDataSetChanged();
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

                        questionController.getQuestionPeriodDetailList(null,question);

                    }else {
                        ToastUtils.shortToast(mContext,R.string.no_sdcard);
                    }
                }
            }
        });

        hodler.file_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(isDownload) && "1".equals(isDownload)){//1 表示已经下载了，
                    //需要读取数据库数据，然后循环的 列举题目
                    List<QuestionPeriodDetail> periodDetails = DatabaseService.findQuestionPeriodDetailList(question.getId());
                    getViewOfQuestions(periodDetails);
                }else {
                    ToastUtils.shortToast(mContext,R.string.not_download_file);
                }
            }
        });
        return view;
    }

    private void getViewOfQuestions(List<QuestionPeriodDetail> periodDetails){
        //拼凑 HTML
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">");
        sb.append("<style type=\"text/css\">\n" +
                ".div1 {\n" +
                "\theight: auto;\n" +
                "\twidth: 92%;\n" +
                "\tmargin-top: 5%;\n" +
                "\tmargin-right: auto;\n" +
                "\tmargin-bottom: 10%;\n" +
                "\tmargin-left: auto;\n" +
                "}" +
                "</style>");
        sb.append("</head>");
        sb.append("<body>");
//        sb.append("<center>");
        sb.append("  <div class=\"div1\">");
        for (int i = 0 ; i < periodDetails.size(); i++){
            QuestionPeriodDetail periodDetail = periodDetails.get(i);
            String url = periodDetail.getUrl();
            if (!StringUtils.isEmpty(url)){
                boolean fileExit = FileUtils.isFileExists(url);
                if (fileExit) {
                    String jstr = FileUtils.getJsonFile(url);
                    if (StringUtils.isEmpty(jstr)){

                    }else {
                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(jstr);
                            QuestionContent content = pullJson(jsonObject);
                            sb.append((i+1)+".");
                            String pid_title = content.getPid_title();
                            if (!StringUtils.isEmpty(pid_title)){
                                sb.append("<p>" + new String(pid_title.getBytes(),"UTF-8") + "</p>");

                               // sb.append("<p>" + new String(pid_title.getBytes(),"UTF-8") + "</p>");
                            }
                            String  pid_title_key= content.getPid_title_key();
                            if (!StringUtils.isEmpty(pid_title_key)){
                                sb.append("<p>" + new String(pid_title_key.getBytes(),"UTF-8") + "</p>");
                                //sb.append("<p>" + new String(pid_title_key.getBytes(),"UTF-8") + "</p>");
                            }
                            String title = content.getTitle();
                            if (!StringUtils.isEmpty(title)){
                                sb.append("<p>" + new String(title.getBytes(),"UTF-8") + "</p>");
                               // sb.append("<p>" + new String(title.getBytes(),"UTF-8") + "</p>");
                            }
                            String title_key= content.getTitle_key();
                            if (!StringUtils.isEmpty(title_key)){
                                sb.append("<p>" + new String(title_key.getBytes(),"UTF-8") + "</p>");
                              //  sb.append("<p>" + new String(title_key.getBytes(),"UTF-8") + "</p>");
                            }
                            String answer= content.getAnswer();
                            if (!StringUtils.isEmpty(answer)){
                                sb.append("<p>【答案】" + new String(answer.getBytes(),"UTF-8") + "</p>");
                              //  sb.append("<p>【答案】" + new String(answer.getBytes(),"UTF-8") + "</p>");
                            }
                            String parse = content.getParse();
                            if (!StringUtils.isEmpty(parse)){
                                sb.append("<p>【解析】" + new String(parse.getBytes(),"UTF-8") + "</p>");
                                //sb.append("<p>【解析】" + new String(parse.getBytes(),"UTF-8") + "</p>");
                            }
                            String parse_key = content.getParse_key();
                            if (!StringUtils.isEmpty(parse_key)){
                                sb.append("<p>" + new String(parse_key.getBytes(),"UTF-8") + "</p>");
                            }
                            sb.append("<p></p>");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        sb.append("</div>");
        //sb.append("</center>");
        sb.append("</body>\n" +
                "</html>");

        Log.i("html",sb.toString());
        Intent it = new Intent(mContext, QuestionViewActivity.class);
//        Bundle bd = new Bundle();
//        bd.putString("html",sb.toString());
        //try {
            //it.putExtra("html",new String(sb.toString().getBytes("gbk"),"utf-8"));
       // } catch (UnsupportedEncodingException e) {
        //    e.printStackTrace();
           it.putExtra("html",sb.toString());
       // }
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(it);

       // String code = getEncoding(sb.toString());
      //  Log.i("code ",code);

    }

    public  String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    }
    private QuestionContent pullJson(JSONObject jsonObject) throws JSONException{
        if (jsonObject != null){
            QuestionContent content = new QuestionContent();
            content.setUuid(jsonObject.getString("uuid"));
            content.setType(jsonObject.getInt("type"));
            content.setOptions(jsonObject.getInt("options"));
            content.setAnswer(jsonObject.getString("answer"));
            content.setTitle(jsonObject.getString("title"));
            content.setTitle_key(jsonObject.getString("title_key"));
            content.setPid_title(jsonObject.getString("pid_title"));
            content.setPid_title_key(jsonObject.getString("pid_title_key"));
            content.setParse(jsonObject.getString("parse"));
            content.setParse_key(jsonObject.getString("parse_key"));
            return content;
        }else {
            return null;
        }
    }
    class ViewHodler{

        TextView file_title;
        ImageView down;
    }
}
