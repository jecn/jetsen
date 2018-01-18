package com.huayinghealth.testaar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chanlin.jetsencloud.JetsenMainActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = new Intent(this,JetsenMainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("api_url","http://dev.approute.kai12.cn/api/route");
        extras.putString("user_id", "11024");
        extras.putString("user_name", "深圳向");
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
