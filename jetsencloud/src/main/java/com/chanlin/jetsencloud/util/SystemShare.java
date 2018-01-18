package com.chanlin.jetsencloud.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ChanLin on 2017/11/20.
 * ProtectEyes
 * TODO:
 */

public class SystemShare {
    private static final String PREFIX_NAME = "jetsen_catch";


    /**
     * 保存String类型数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSettingString(Context context, String key,
                                        String value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }
    /**
     * 获取String类型数据
     *
     * @param context
     */
    public static String getSettingString(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        String strValue = clientPreferences.getString(strKey, "");
        return strValue;
    }

    /**
     * 删除String类型数据
     *
     * @param context
     */
    public static void ClearSettingString(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.remove(strKey);
        prefEditor.commit();
    }

    /**
     * 保存boolean类型数据
     *
     * @param context
     */
    public static void setSettingBoolean(Context context, String strKey,
                                         boolean value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putBoolean(strKey, value);
        prefEditor.commit();
    }

    /**
     * 获取boolean类型数据
     *
     * @param context
     */
    public static boolean getSettingBoolean(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        boolean value = clientPreferences.getBoolean(strKey, false);
        return value;
    }

    public static boolean getSettingBoolean(Context context, String strKey,
                                            boolean value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        boolean ret = clientPreferences.getBoolean(strKey, value);
        return ret;
    }

    /**
     * 保存int类型数据
     *
     * @param context
     * @param value
     */
    public static void setSettingInt(Context context, String strKey, int value) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor prefEditor = clientPreferences.edit();
        prefEditor.putInt(strKey, value);
        prefEditor.commit();
    }

    /**
     * 获取int类型数据
     *
     * @param context
     */
    public static int getSettingInt(Context context, String strKey) {
        SharedPreferences clientPreferences = context.getSharedPreferences(
                PREFIX_NAME, Context.MODE_MULTI_PROCESS);
        int value = clientPreferences.getInt(strKey, 0);
        return value;
    }

}
