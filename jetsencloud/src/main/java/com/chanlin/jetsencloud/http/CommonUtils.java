package com.chanlin.jetsencloud.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import org.json.JSONObject;

/**
 * Created by ChanLin on 2018/1/9.
 * jetsenCloud
 * TODO:
 */

public class CommonUtils {
    public static String getDataStrFromResult(String result_json_str) {
        try {
            JSONObject e = new JSONObject(result_json_str);
            String data_value_temp = e.getString("data");
            if(TextUtils.isEmpty(data_value_temp)) {
                return "";
            } else {
                return data_value_temp;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static boolean isSuccess(String response_data) {
        if(TextUtils.isEmpty(response_data)) {
            return false;
        } else {
            try {
                JSONObject e = new JSONObject(response_data);
                String data_value_temp = e.getString("code");
                return "0".equals(data_value_temp);
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
