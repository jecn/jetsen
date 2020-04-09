package com.huayinghealth.testaar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.HttpCallBack;
import com.chanlin.jetsencloud.util.Constant;
import com.chanlin.jetsencloud.http.OKHttpUtil;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.huayinghealth.testaar.entity.Theater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by ChanLin on 2018/5/16.
 * jetsen
 * TODO:
 */

public class FirstController {

    private Context mContext;
    private Handler mMainHandler;
    private String Host;
    public FirstController(Context context, Handler handler){
        Host = "http://approute.kexinedu.net/api/route";
        this.mContext = context;
        this.mMainHandler = handler;
    }

    /**
     * 云登录
     */
    public void cloud_login(){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
        /*String username = "1530618";
        String password = "123456";*/
        //学生登录
        /*String username = "849054";
        String password = "h123456";*/
        String username = "13030253018";
        String password = "Huaying88";
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        formEncodingBuilder.add("username", username);
        formEncodingBuilder.add("password", password);
        body = formEncodingBuilder.build();
        final Request request = new Request.Builder()
                .url(Host)
                .addHeader(Constant.k12appKey, Constant.k12appValue)
                .addHeader(Constant.k12avKey, Constant.k12avValue)
                .addHeader(Constant.k12url, "cloud/login")
                .addHeader(Constant.k12code, "cloud")
                .post(body)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.one_1);

                    try {
                       // JSONObject jsonObject = new JSONObject(result_json);
                       // int code = jsonObject.getInt("code");
                        //if (0 == code){
                            JSONObject object = new JSONObject(result_json);
                            String file = object.getString("file");
                            String token = object.getString("token");
                            String app_route = object.getString("app_route");
                            String kid = object.getString("kid");
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.file, file);
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.token, token);
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.app_route, app_route);
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.kid, kid);
                       // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){
                    

                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.one_2);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.one_3);
                    success.sendToTarget();
                }
            }
        });

    }


    /**
     * 关系列表
     */
    public void relation_list(){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
        String type = "1";//老师
//        String type = "0";//学生
        final Request request = new Request.Builder()
                .url(Host+"?kid="+SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.kid)+"&user_type="+type)
                .addHeader(Constant.k12appKey, Constant.k12appValue)
                .addHeader(Constant.k12avKey, Constant.k12avValue)
                .addHeader(Constant.k12url, "cloud/relation_list")
                .addHeader(Constant.k12code, "cloud")
                .addHeader(Constant.k12token, SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.token))
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.two_1);
                    success.obj = result_json;
                    success.sendToTarget();



                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){


                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.two_2);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.two_3);
                    success.sendToTarget();
                }
            }
        });
    }


    /**
     * 具体身份登录
     */
    public void identity_login(){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();

        int user_id = SystemShare.getSettingInt(mContext, com.huayinghealth.testaar.Constant.user_id);
        String school_code = SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.school_code);
        String type = "1";//老师
//        String type = "0";//学生
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        formEncodingBuilder.add("user_id", String.valueOf(user_id));
        formEncodingBuilder.add("school_code", school_code);
        formEncodingBuilder.add("type", type);
        body = formEncodingBuilder.build();
        final Request request = new Request.Builder()
                .url(Host)
                .addHeader(Constant.k12appKey, Constant.k12appValue)
                .addHeader(Constant.k12avKey, Constant.k12avValue)
                .addHeader(Constant.k12url, "cloud/identity_login")
                .addHeader(Constant.k12code, "cloud")
                .addHeader(Constant.k12token, SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.token))
                .post(body)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){

                    try {
                       // JSONObject jsonObject = new JSONObject(result_json);
                       // int code = jsonObject.getInt("code");
                       // if (0 == code){
                            JSONObject object = new JSONObject(result_json);
                            String token = object.getString("school_token");
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.token, token);
                      //  }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.three_1);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.three_2);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.three_3);
                    success.sendToTarget();
                }
            }
        });
    }


    /**
     * 用户信息
     */
    public void teacher_info(){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();

        final Request request = new Request.Builder()
                .url(Host)
                .addHeader(Constant.k12appKey, Constant.k12appValue)
                .addHeader(Constant.k12avKey, Constant.k12avValue)
                .addHeader(Constant.k12url, "user/teacher/info")
                .addHeader(Constant.k12code, SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.school_code))
                .addHeader(Constant.k12token, SystemShare.getSettingString(mContext, com.huayinghealth.testaar.Constant.token))
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    try {
                       // JSONObject jsonObject = new JSONObject(result_json);
                       // int code = jsonObject.getInt("code");
                       // if (0 == code){
                            JSONObject object = new JSONObject(result_json);
                            String teacher_id = object.getString("teacher_id");
                            SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.teacher_id, teacher_id);
                       // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.four_1);
                    success.obj = result_json;
                    success.sendToTarget();



                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){


                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.four_2);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(com.huayinghealth.testaar.Constant.four_3);
                    success.sendToTarget();
                }
            }
        });
    }
}
