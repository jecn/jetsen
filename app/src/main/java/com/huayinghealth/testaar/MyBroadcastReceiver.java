package com.huayinghealth.testaar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.chanlin.jetsencloud.JetsenMainActivity;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.util.Constant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/3/9.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals(Constant.SEND_EXERCISES)){
            Bundle bundle = intent.getExtras();
            int course_standard_id = bundle.getInt("course_standard_id");
            ArrayList<QuestionPeriodDetail> details = (ArrayList<QuestionPeriodDetail>) bundle.getSerializable("questionList");

            Toast.makeText(mContext, "发送...course_standard_id=" + course_standard_id+"\n"+details.toString(), Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(mContext, JetsenMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent1);
        }
        if (intent.getAction().equals(Constant.SEND_PREPARE_RESOURCE)){
            Bundle bundle = intent.getExtras();
            String string = bundle.getSerializable("resourceTree").toString();
            Toast.makeText(mContext, "发送..." + "  " +string, Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(mContext, JetsenMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent1);
        }
    }
}
