package com.chanlin.jetsencloud.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by ChanLin on 2018/1/9.
 * jetsenCloud
 * TODO:
 */

public abstract class BaseTask extends AsyncTask<String, String, String> {
    protected Context mContext;
    protected String mRemoteServerAddress;
    protected String mUserToken;
    protected boolean mIsGranted;
    protected Handler mMainHandler;

    public BaseTask(Context ctx, String target_address, String user_token, Handler main_handler, boolean is_granted) {
        this.mContext = ctx;
        this.mRemoteServerAddress = target_address;
        this.mUserToken = user_token;
        this.mMainHandler = main_handler;
        this.mIsGranted = is_granted;
    }

    protected String doInBackground(String... params) {
        return this.doTask();
    }

    protected void onPostExecute(String result_data) {
        super.onPostExecute(result_data);
        if("999999".equals(result_data)) {
            this.onException();
        } else {
            Log.e("Photo", "onPostExecute");
            Log.e("Photo", "result_data:" + result_data);
            String finally_data = CommonUtils.getDataStrFromResult(result_data);
            Log.e("Photo", "finally_data:" + finally_data);
            if(CommonUtils.isSuccess(result_data)) {
                this.onSuccess(finally_data);
            } else {
                this.onFalse(finally_data);
            }
        }

    }

    public abstract String doTask();

    public abstract void onSuccess(String var1);

    public abstract void onFalse(String var1);

    public abstract void onException();
}
