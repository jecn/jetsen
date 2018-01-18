package com.chanlin.jetsencloud.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.chanlin.jetsencloud.R;


/**
 *
 * @author ChanLin
 *
 */
public class LoadingDialog extends Dialog {
    Context context;

    public LoadingDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;

    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setCancelable(false);// 设置点击屏幕Dialog不消失
        this.setContentView(R.layout.loading_dialog);

    }

}