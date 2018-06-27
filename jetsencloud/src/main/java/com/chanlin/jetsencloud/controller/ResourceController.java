package com.chanlin.jetsencloud.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.chanlin.jetsencloud.JetsenResourceActivity;
import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.http.HttpCallBack;
import com.chanlin.jetsencloud.http.MessageConfig;
import com.chanlin.jetsencloud.http.OKHttpUtil;
import com.chanlin.jetsencloud.http.ReqCallBack;
import com.chanlin.jetsencloud.util.Constant;
import com.chanlin.jetsencloud.util.SDCardUtils;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ChanLin on 2018/1/11.
 * jetsenCloud
 * TODO:
 */

public class ResourceController {
    private Context mContext;
    private Handler mMainHandler;
    private String Host;
    QuestionController questionController;
    public ResourceController(Context context, Handler handler){
        Host = SystemShare.getSettingString(context, Constant.Host);

        this.mContext = context;
        this.mMainHandler = handler;
        questionController = new QuestionController(mContext,mMainHandler);
    }
    /**
     * 网络获取资源信息
        c传入课标id
     */
    public void getResourceList(@NonNull int course_standard_id){
        if(!CommonUtils.isNetworkAvailable(mContext)){
            return;
        }
        OkHttpClient mOkHttpClient = OKHttpUtil.getInstanceHttpClient();
        String token = SystemShare.getSettingString(mContext, Constant.k12token);
        String code = SystemShare.getSettingString(mContext, Constant.k12code);

        final Request request = new Request.Builder()
                .url(Host+"?course_standard_id="+course_standard_id)
                .addHeader(Constant.k12appKey, Constant.k12appValue)
                .addHeader(Constant.k12avKey, Constant.k12avValue)
                .addHeader(Constant.k12url, Constant.code_resource_list)
                .addHeader(Constant.k12code, code)
                .addHeader(Constant.k12token, token)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new HttpCallBack() {
            @Override
            public void onSuccess(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.resource_list_standard_http_success_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onFalse(String result_json) {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.resource_list_standard_http_false_MESSAGE);
                    success.obj = result_json;
                    success.sendToTarget();
                }
            }
            @Override
            public void onException() {
                if (null != mMainHandler){
                    Message success = mMainHandler.obtainMessage(MessageConfig.resource_list_standard_http_exception_MESSAGE);
                    success.sendToTarget();
                }
            }
        });

    }




    public void downlaodResourceAndQuestion(ArrayList<ResourceTree> resourceTreeList, ArrayList<QuestionPeriod> questionPeriodList){
        //循环下载咯
        for (final ResourceTree tree :
                resourceTreeList) {
            if (!JetsenResourceActivity.mIsGrant){
                ToastUtils.shortToast(mContext, R.string.no_permission);
                return;
            }
            if (SDCardUtils.isSDCardEnable()){
                String fileDir = SDCardUtils.getSDCardPath() + SDCardUtils.fileDir;
                String file_download_host = SystemShare.getSettingString(mContext, Constant.file_download_host);
                OKHttpUtil.downLoadFile(file_download_host + tree.getKey(), fileDir, new ReqCallBack<ResourceTree>() {
                    @Override
                    public void successCallBack(File file) {
                        String filePath = file.getPath();//获取文件下载的路径
                        //更新数据库
                        DatabaseService.updateResourceTree(tree.getCourse_standard_id(),tree.getUuid(),tree.getKey(),tree.getTitle(),tree.getSize(),tree.getType(),filePath);
                        //更新UI
                        mMainHandler.sendEmptyMessage(MessageConfig.download_resource_question_message);
                    }

                    @Override
                    public void failedCallBack() {
                        mMainHandler.sendEmptyMessage(MessageConfig.downlaod_resource_question_error);
                    }
                });
            }else {
                ToastUtils.shortToast(mContext,R.string.no_sdcard);
            }
        }
        for (QuestionPeriod question :
                questionPeriodList) {
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
                //all 标识是 使用一键全部下载
                questionController.getQuestionPeriodDetailList("all",question);
            }else {
                ToastUtils.shortToast(mContext,R.string.no_sdcard);
            }
        }
    }


}
