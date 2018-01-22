package com.chanlin.jetsencloud.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.HttpCallBack;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.http.OKHttpUtil;
import com.chanlin.jetsencloud.util.Constant;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.view.LoadingProgressDialog;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ChanLin on 2018/1/9.
 * jetsenCloud
 * TODO:
 */

public class BookController {
    private Context mContext;
    private Handler mMainHandler;
    private String Host ;

    public BookController(Context context, Handler handler){
        Host = SystemShare.getSettingString(context, Constant.Host);
        this.mContext = context;
        this.mMainHandler = handler;
    }

    //get请求
    public void getBookList(final Handler mMainHandler, String bookId){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            LoadingProgressDialog.loadingDialog.dismiss();
            return;
        }
        if (null != bookId){
            OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
            String token = SystemShare.getSettingString(mContext, Constant.k12token);
            String code = SystemShare.getSettingString(mContext, Constant.k12code);
            final Request request = new Request.Builder()
                    .url(Host+"?course_id="+bookId)
                    .addHeader(Constant.k12appKey, Constant.k12appValue)
                    .addHeader(Constant.k12avKey, Constant.k12avValue)
                    .addHeader(Constant.k12url, Constant.code_book_list)
                    .addHeader(Constant.k12code, code)
                    .addHeader(Constant.k12token, token)
                    .build();
            //new call
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new HttpCallBack() {
                @Override
                public void onSuccess(String result_json) {
                    if (null != mMainHandler){
                        Message success = mMainHandler.obtainMessage(MessageConfig.book_http_success_MESSAGE);
                        success.obj = result_json;
                        success.sendToTarget();
                    }
                }
                @Override
                public void onFalse(String result_json) {
                    if (null != mMainHandler){
                        Message success = mMainHandler.obtainMessage(MessageConfig.book_http_false_MESSAGE);
                        success.obj = result_json;
                        success.sendToTarget();
                    }
                }
                @Override
                public void onException() {
                    if (null != mMainHandler){
                        Message success = mMainHandler.obtainMessage(MessageConfig.book_http_exception_MESSAGE);
                        success.sendToTarget();
                    }
                }
            });

        }
    }

}
