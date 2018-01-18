package com.chanlin.jetsencloud.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by ChanLin on 2018/1/9.
 * jetsenCloud
 * TODO:
 */

public class BookTask extends BaseTask {
    public BookTask(Context ctx, String target_address, String user_token, Handler main_handler, boolean is_granted) {
        super(ctx, target_address, user_token, main_handler, is_granted);

    }

    @Override
    public String doTask() {

        return null;
    }

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
}
