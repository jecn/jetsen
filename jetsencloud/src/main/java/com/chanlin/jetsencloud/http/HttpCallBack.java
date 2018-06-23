package com.chanlin.jetsencloud.http;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ChanLin on 2018/1/10.
 * jetsenCloud
 * TODO:
 */

public abstract class HttpCallBack implements Callback {
    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() != 200){
            this.onFalse(response.body().string());
        }
        String result_data = response.body().string();
        try {
            JSONObject js = new JSONObject(result_data);
            int code = js.getInt("code");
            if (code == 0) {
                Log.e("Photo", "onPostExecute");
                Log.e("Photo", "result_data:" + result_data);
                String finally_data = CommonUtils.getDataStrFromResult(result_data);
                Log.e("Photo", "finally_data:" + finally_data);
                if (CommonUtils.isSuccess(result_data)) {
                    this.onSuccess(finally_data);
                } else {
                    this.onFalse(finally_data);
                }
            } else {
                this.onException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public abstract void onSuccess(String var1);

    public abstract void onFalse(String var1);

    public abstract void onException();
}
