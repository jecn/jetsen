package com.chanlin.jetsencloud.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.adapter.QuestionAdapter;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.HttpCallBack;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.http.OKHttpUtil;
import com.chanlin.jetsencloud.http.ReqCallBack;
import com.chanlin.jetsencloud.util.Constant;
import com.chanlin.jetsencloud.util.LogUtil;
import com.chanlin.jetsencloud.util.SDCardUtils;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.util.ToastUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ChanLin on 2018/1/12.
 * jetsenCloud
 * TODO:
 */

public class QuestionController {
    private Context mContext;
    private Handler mMainHandler;
    private String Host;
    private String file_download_host;
    private int downloadedCount = 0;//已经下载的数量
    public QuestionController(Context context, Handler handler){
        Host = SystemShare.getSettingString(context,Constant.Host);
        file_download_host = SystemShare.getSettingString(context,Constant.file_download_host);
        this.mContext = context;
        this.mMainHandler = handler;
    }

    /**
     * 网络获取  课时列表
     */
    public void getQuestionPeriodList(@NonNull int course_standard_id) {
        if (!CommonUtils.isNetworkAvailable(mContext)) {
            return;
        }

        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
        String token = SystemShare.getSettingString(mContext,Constant.k12token);
        String code = SystemShare.getSettingString(mContext,Constant.k12code);
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Host+"?course_standard_id="+course_standard_id)
                .addHeader(Constant.k12appKey,Constant.k12appValue)
                .addHeader(Constant.k12avKey,Constant.k12avValue)
                .addHeader(Constant.k12url,Constant.code_question_period_list)
                .addHeader(Constant.k12code, code)
                .addHeader(Constant.k12token, token)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.question_period_standard_http_success_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.question_period_standard_http_false_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.question_period_standard_http_exception_MESSAGE);
                    success.sendToTarget();
                }
            }
        });
    }


    /**
     *  获取课时 习题详情， 循环下载Jason
     * @param question_period_id
     */
    public void getQuestionPeriodDetailList(final int course_standard_id , final int question_period_id) {
        if (!CommonUtils.isNetworkAvailable(mContext)) {
            ToastUtils.shortToast(mContext,mContext.getResources().getString(R.string.http_exception));
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
        String token = SystemShare.getSettingString(mContext,Constant.k12token);
        String code = SystemShare.getSettingString(mContext,Constant.k12code);
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Host+"?course_standard_id="+course_standard_id+"&question_period_id="+question_period_id)
                .addHeader(Constant.k12appKey,Constant.k12appValue)
                .addHeader(Constant.k12avKey,Constant.k12avValue)
                .addHeader(Constant.k12url,Constant.code_question_period_details)
                .addHeader(Constant.k12code, code)
                .addHeader(Constant.k12token, token)
                .build();
        LogUtil.showError("getQuestionPeriodDetailList","getQuestionPeriodDetailList url { "+ request.url().toString() + " }");

        LogUtil.showError("getQuestionPeriodDetailList","getQuestionPeriodDetailList request { "+ request.headers().toString()+" }");
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    /*Message success = mMainHandler.obtainMessage(MessageConfig.question_period_details_http_success_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();*/
                    //获取成功，接下来 循环  解析json 下载
                    downloadedCount = 0;//请求服务器成功清除原来的换成题目个数
                    try {
                        downloadJson(question_period_id,result_json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (null != mMainHandler){
                            Message success = mMainHandler.obtainMessage(MessageConfig.question_period_details_http_exception_MESSAGE);
                            success.sendToTarget();
                        }
                    }
                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.question_period_details_http_false_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.question_period_details_http_exception_MESSAGE);
                    success.sendToTarget();
                }
            }
        });

    }

    private void downloadJson(final int question_period_id,String jstr) throws JSONException {
        //ArrayList<QuestionPeriodDetail> periodDetails = new ArrayList<>();
        JSONObject dataJson = new JSONObject(jstr);
        if (!dataJson.has("list"))
            return;
        JSONArray resultsArrayJson = dataJson.getJSONArray("list");
        final int count = resultsArrayJson.length();

        for (int a = 0; a < count; a++) {
            JSONObject resultsJson = resultsArrayJson.getJSONObject(a);
            final QuestionPeriodDetail detail = new QuestionPeriodDetail();
            detail.setQuestion_period_id(question_period_id);
            detail.setKey(resultsJson.getString("key"));
            detail.setUuid(resultsJson.getString("uuid"));
            String fileDir = SDCardUtils.getSDCardPath() + SDCardUtils.questionJsonFile;
            OKHttpUtil.downLoadFile(file_download_host + detail.getKey(), fileDir, new ReqCallBack<QuestionPeriodDetail>() {
                @Override
                public void successCallBack(File file) {
                    String filePath = file.getPath();//获取文件下载的路径
                    //更新下载地址
                    DatabaseService.createQuestionPeriodDetailTable(detail.getQuestion_period_id(),detail.getUuid(),detail.getKey(),filePath);

                    //更新UI
                    downloadedCount++;
                    if (count <= downloadedCount){
                        //表示全部下载完成
                        if (null != mMainHandler){
                            Message success = mMainHandler.obtainMessage(MessageConfig.question_period_details_http_success_MESSAGE);
                            //success.obj = QuestionAdapter.pubPosition;
                            success.sendToTarget();
                        }
                    }
                }

                @Override
                public void failedCallBack() {
                    //ToastUtils.shortToast(mContext,R.string.download_error);
                }
            });

        }
    }
}
