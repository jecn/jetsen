package com.chanlin.jetsencloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanlin.jetsencloud.adapter.GridViewAdapter;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.http.CommonUtils;
import com.chanlin.jetsencloud.util.CacheActivity;
import com.chanlin.jetsencloud.util.Constant;
import com.chanlin.jetsencloud.util.LogUtil;
import com.chanlin.jetsencloud.util.StringUtils;
import com.chanlin.jetsencloud.util.SystemShare;
import com.chanlin.jetsencloud.util.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class JetsenMainActivity extends AppCompatActivity {
    private static final String TAG = "JetsenMainActivity";
    /**\
     * 测试账号密码：
     *  szcsx     707851
     */
    private String host;
    private Context mContext;
    private String name;//姓名
    private String avatar;//头像
    private String sex;//性别
    private String school_name;//学校名字
    private String schoolCode;//学校id
    private String token;//token
//    private static String[][] course = new String[][]{};//带的科目
    private String[] course_ids;
    private String[] course_names;
    private String file_host;//文件下载的host地址
    /*
    *    {"code":0,"data":{"teacher_id":10040,"name":"深圳向","sex":1,"avatar":"","school_name":"K12开发学校01（高中）",
    *       "class":[{"id":3,"name":"(3)班","grade_name":"高一年级","course":[{"id":1,"name":"语文"}]},
    *                {"id":505,"name":"(34)班","grade_name":"高一年级","course":[{"id":1,"name":"语文"}]}]},"msg":"success"}
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_jetsen_main);
        this.mContext = this;
        Intent userIntent = getIntent();
        Bundle bd = userIntent.getExtras();
        /**
         extras.putString("api_url", getServerAPI());
         extras.putString("user_id", getUserID());
         extras.putString("user_name", getAlias());
         extras.putString("avatar", getPhoto());
         extras.putString("school_code", getSchoolID());
         extras.putString("school_name", getSchoolName());
         extras.putString("teacher_id", getTeacherID());
         extras.putString("file", getDownloadUrl());
         extras.putString("file_up", getUploadUrl());
         extras.putString("token", getAccessToken());
         extras.putStringArray("course_ids", ids);
         extras.putStringArray("course_names", names);
         */

        //Constant.k12tokenValue = bd.getString(Constant.k12token);
        host = bd.getString("api_url");
        avatar = bd.getString("avatar");
        name = bd.getString("user_name");
        sex = bd.getString("sex");
        school_name = bd.getString("school_name");
        schoolCode = bd.getString("school_code");
        token = bd.getString("token");
        file_host = bd.getString("file");
        course_ids = bd.getStringArray("course_ids");
        course_names = bd.getStringArray("course_names");
//        for (int i = 0 ; i < course_ids.length; i ++){
//            course[i][0] = course_ids[i];
//            course[i][1] = course_names[i];
//        }
        /*school_name = "K12开发学校01（高中）";
        name = "张飞";
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBsaWNhdGlvbiI6InRvYl9wYWQiLCJzY2hvb2xfY29kZSI6ImsxMnNjaG9vbF8wMV9kZXYiLCJ1c2VyX2lkIjoxMTAyNCwiZXhwIjoxNTE2NDMxODU5LCJsb2dpbl90eXBlIjoxfQ.9ImuBQoXK-QQ-WPRUpP7VCuxdAzgSqMRJN3d1rj8bPc";
        schoolCode = "k12school_01_dev";
        course = new String[][]{{"1","语文"},{"2","数学"},{"3","英语"}};
//        avatar = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515480046265&di=cfb78a8d2e029c15e8ba0e138991587f&imgtype=0&src=http%3A%2F%2Favatar.wshang.com%2F151214%2F1450077753_x.jpg";
        avatar = "";
        host = "http://dev.approute.kai12.cn/api/route";
        file_host = "http://121.41.99.232:50001/";
*/
        SystemShare.setSettingString(mContext, Constant.Host,host);
        SystemShare.setSettingString(mContext, Constant.file_download_host,file_host);
        SystemShare.setSettingString(mContext, Constant.k12token,token);
        SystemShare.setSettingString(mContext, Constant.k12code,schoolCode);
        initView();
        initData();
        initListener(this);

    }

    private ImageView user_head_iv;
    private TextView tv_user_name;
    private TextView tv_user_school;
    private GridView gv_course;
    private TextView tv_sendexercise, tv_prepare_resource;
    private GridViewAdapter gridViewAdapter;

    private void initView(){
        user_head_iv = (ImageView) findViewById(R.id.user_head_iv);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_name.setText(name);
        tv_user_school = (TextView) findViewById(R.id.tv_user_school);
        tv_user_school.setText(school_name);
        gv_course = (GridView) findViewById(R.id.gv_course);
        gridViewAdapter = new GridViewAdapter(mContext,course_names);
        gv_course.setAdapter(gridViewAdapter);

        tv_sendexercise = (TextView) findViewById(R.id.tv_sendexercise);
        tv_sendexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后跳转 传递值到另外一个页面
                Intent it  = new Intent(JetsenMainActivity.this,JetsenSendExerciseActivity.class);
                it.putExtra("courceId", 1 + "");
                JetsenMainActivity.this.startActivity(it);
            }
        });
        tv_prepare_resource = (TextView) findViewById(R.id.tv_prepare_resource);
        tv_prepare_resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后跳转 传递值到另外一个页面
                Intent it  = new Intent(JetsenMainActivity.this,JetsenPrepareResourceActivity.class);
                it.putExtra("courceId", 1 + "");
                JetsenMainActivity.this.startActivity(it);
            }
        });
    }

    private void initData(){
        LogUtil.showInfo(TAG,"init data");
        int headImg = 0;
        if (!StringUtils.isEmpty(sex) && Constant.SEX.equals(sex)){
            headImg = R.mipmap.man;
        }else {
            headImg = R.mipmap.woman;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(headImg)
                .showImageForEmptyUri(headImg)
                .showImageOnFail(headImg)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true).build();

        ImageLoader.getInstance().displayImage(avatar,user_head_iv,options);
    }
    private  void initListener(final JetsenMainActivity jetsenMainActivity){
        jetsenMainActivity.gv_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // LogUtil.showWarn(TAG, course[position][1]);
                if(!CommonUtils.isNetworkAvailable(mContext)){
                    ToastUtils.shortToast(mContext,getResources().getString(R.string.no_network));
                    return;
                }
                //点击后跳转 传递值到另外一个页面
                Intent it  = new Intent(jetsenMainActivity.mContext,JetsenResourceActivity.class);
                it.putExtra("courceId", course_ids[position]);
                jetsenMainActivity.startActivity(it);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            int course_standard_id = bundle.getInt("course_standard_id");
            ArrayList<QuestionPeriodDetail> details = (ArrayList<QuestionPeriodDetail>) bundle.getSerializable("questionList");

            Toast.makeText(this, "发送...course_standard_id=" + course_standard_id+"\n"+details.toString(), Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 1002 && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            String string = bundle.getSerializable("resourceTree").toString();
            Toast.makeText(this, "发送..." + "  " +string, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CacheActivity.finishActivity();
            finish();
            Log.e("onKeyDown", "onKeyDown");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
