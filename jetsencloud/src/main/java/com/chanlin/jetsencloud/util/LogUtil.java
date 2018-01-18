package com.chanlin.jetsencloud.util;

import android.util.Log;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class LogUtil {
    public static final boolean isDebug = true;
    public static void showInfo(String tagName,String logText){
        if (isDebug) {
            Log.i(tagName, logText);
        }
    }
    public static void showWarn(String tagName,String logText){
        if (isDebug) {
            Log.w(tagName, logText);
        }
    }
    public static void showError(String tagName,String logText){
        if (isDebug) {
            Log.e(tagName, logText);
        }
    }

}
