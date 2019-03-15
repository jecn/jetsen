package com.huayinghealth.testaar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.chanlin.jetsencloud.JetsenMainActivity;
import com.chanlin.jetsencloud.util.SystemShare;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        Intent it = new Intent(this,JetsenMainActivity.class);
        Bundle extras = new Bundle();
        /**
         *
         course_ids=[Ljava.lang.String;@2acd5fc
         avatar=
         teacher_id=158
         file_up=http://fileup.kexinedu.net:8005/
         api_url=http://approute.kexinedu.net/api/route
         user_id=1021
         sex=1
         file=http://file.kexinedu.net:8004/
         token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBsaWNhdGlvbiI6InRvYl9wYWQiLCJzY2hvb2xfY29kZSI6InNfNTExNTAwMTciLCJ1c2VyX2lkIjoxMDIxLCJleHAiOjE1MTY2OTkyNTYsImxvZ2luX3R5cGUiOjF9.MSc1WCT2u4TNeZx6W5eydVc31V21ZuYqwRkFwwEPpV4
         user_name=张海涛
         school_code=s_51150017
         school_name=宜宾科信中学
         course_names=[Ljava.lang.String;@9b21485
         */

        extras.putString("api_url","http://approute.kexinedu.net/api/route");
        int user_id = SystemShare.getSettingInt(this, Constant.user_id);
        extras.putString("user_id", String.valueOf(user_id));
        extras.putString("user_name", "向成林");
        extras.putString("sex","1");
        extras.putString("avatar", "");
        String school_code = SystemShare.getSettingString(this, Constant.school_code);
        extras.putString("school_code", school_code);
        extras.putString("school_name", "宜宾科信中学");
        String teacher_id = SystemShare.getSettingString(this, Constant.teacher_id);
        extras.putString("teacher_id", teacher_id);
        extras.putString("file", "http://file.kexinedu.net:8004/");
        extras.putString("file_up", "http://fileup.kexinedu.net:8005/");
        String[] ids = new String[]{"1","2","3"};
        String token = SystemShare.getSettingString(this, com.huayinghealth.testaar.Constant.token);
        //extras.putString("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBsaWNhdGlvbiI6InRvYl9wYWQiLCJzY2hvb2xfY29kZSI6InNfNTExNTAwMjAiLCJ1c2VyX2lkIjo0NTEwLCJleHAiOjE1MjE3ODk3NzcsImxvZ2luX3R5cGUiOjF9.tDiXl4lnI9zbkVe5U7ow-VttR0oFUToVW9jFX_4WAGE");
        extras.putString("token", token);
        String[] names = new String[]{"语文","数学","英语"};
        extras.putStringArray("course_ids", ids);
        extras.putStringArray("course_names", names);
        //测试地址
        /*
        extras.putString("api_url","http://dev.approute.kai12.cn/api/route");
        extras.putString("user_id", "11024");
        extras.putString("user_name", "深圳向");
        extras.putString("sex","1");
        extras.putString("avatar", "");
        extras.putString("school_code", "k12school_01_dev");
        extras.putString("school_name", "K12开发学校01（高中）");
        extras.putString("teacher_id", "10016");
        extras.putString("file", "http://121.41.99.232:50001/");
        extras.putString("file_up", "http://121.41.99.232:20001/");
        extras.putString("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBsaWNhdGlvbiI6InRvYl9wYWQiLCJzY2hvb2xfY29kZSI6ImsxMnNjaG9vbF8wMV9kZXYiLCJ1c2VyX2lkIjoxMTAyNCwiZXhwIjoxNTE2NDMxODU5LCJsb2dpbl90eXBlIjoxfQ.9ImuBQoXK-QQ-WPRUpP7VCuxdAzgSqMRJN3d1rj8bPc");
        String[] ids = new String[]{"2"};
        String[] names = new String[]{"数学"};
        extras.putStringArray("course_ids", ids);
        extras.putStringArray("course_names", names);
        */
        it.putExtras(extras);
        startActivity(it);
        finish();
    }

    private void getData(){
        /**
         {
         "user_id": "11024",
         "user_name": "深圳向",
         "avatar": "",
         "school_code": "k12school_01_dev",
         "school_name": "K12开发学校01（高中）",
         "teacher_id": "10016",
         "class": [{
         "id": "1",
         "name": "高一年级(1)班",
         "course": [{
         "id": "2",
         "name": "数学"
         }]
         }],
         "file": "http://121.41.99.232:50001/",
         "file_up": "http://121.41.99.232:20001/",
         "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBsaWNhdGlvbiI6InRvYl9wYWQiLCJzY2hvb2xfY29kZSI6ImsxMnNjaG9vbF8wMV9kZXYiLCJ1c2VyX2lkIjoxMTAyNCwiZXhwIjoxNTE2NDMyNjk2LCJsb2dpbl90eXBlIjoxfQ.yeVSvCTPLkEhDosBOoSKBN2PLZaNn6q57jnPAnRyEAU"
         }
         */
    }
}
