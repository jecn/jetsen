package com.huayinghealth.testaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.view.LoadingProgressDialog;
import com.huayinghealth.testaar.entity.Theater;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/5/16.
 * jetsen
 * TODO:
 */

public class FirstActivity extends Activity implements OnClickListener{
    private Button bt_one,bt_two,bt_three,bt_four,bt_five;
    private TextView tv_content;
    private Context mContext;
    private FirstController controller;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoadingProgressDialog.loadingDialog.dismiss();
            String result_json = msg.obj.toString();
            tv_content.setText(result_json);
            switch (msg.what){
                case Constant.one_1:

                    break;
                case Constant.two_1:
                    try {
                        //JSONObject jsonObject = new JSONObject(result_json);
                       // int code = jsonObject.getInt("code");
                        //if (0 == code){
                            JSONObject object = new JSONObject(result_json);
                            //JSONArray jsonArray = object.getJSONArray("list");
                            final ArrayList<Theater> theaters  = JSON.parseObject(object.getString("list"),  new TypeReference<ArrayList<Theater>>() {});
                            //弹框
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("选择学校");
                            String[] strs = new String[theaters.size()];
                            for (int i = 0; i<theaters.size();i++) {
                                strs[i] = theaters.get(i).getSchool_name() + "\t" + theaters.get(i).getName();
                            }
                            builder.setItems(strs, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //选择某个学校身份
                                    Theater t = theaters.get(which);
                                    int user_id = t.getUser_id();
                                    String name = t.getName();
                                    String school_name = t.getSchool_name();
                                    String school_code = t.getSchool_code();
                                    String token = t.getSchool_token();

                                    SystemShare.setSettingInt(mContext, com.huayinghealth.testaar.Constant.user_id, user_id);
                                    SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.user_name, name);
                                    SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.school_name,school_name);
                                    SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.school_code,school_code);
                                    SystemShare.setSettingString(mContext, com.huayinghealth.testaar.Constant.token,token);

                                }
                            });

                            builder.show();
                       // }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.three_1:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        this.mContext = this;
        bt_one = (Button) findViewById(R.id.bt_one);
        bt_two = (Button) findViewById(R.id.bt_two);
        bt_three = (Button) findViewById(R.id.bt_three);
        bt_four = (Button) findViewById(R.id.bt_four);
        bt_five = (Button) findViewById(R.id.bt_five);

        bt_one.setOnClickListener(this);
        bt_two.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        bt_four.setOnClickListener(this);
        bt_five.setOnClickListener(this);
        tv_content = (TextView) findViewById(R.id.tv_content);
        controller = new FirstController(this, mHandler);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        LoadingProgressDialog.show(mContext,true,true);
        switch (id){
            case R.id.bt_one:
                controller.cloud_login();
                break;
            case R.id.bt_two:
                controller.relation_list();
                break;
            case R.id.bt_three:
                controller.identity_login();
                break;
            case R.id.bt_four:
                controller.teacher_info();
                break;
            case R.id.bt_five:
                Intent it = new Intent(mContext, MainActivity.class);
                startActivity(it);
                break;

        }
    }
}
