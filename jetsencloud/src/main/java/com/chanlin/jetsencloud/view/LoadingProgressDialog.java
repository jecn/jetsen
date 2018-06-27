package com.chanlin.jetsencloud.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanlin.jetsencloud.R;
import com.chanlin.jetsencloud.util.StringUtils;


public class LoadingProgressDialog {
    public static Dialog loadingDialog;
    protected static Handler handler;
    public static int time = 60000;
    public static String showtext = null;

    public static void show(Context context, boolean isCancel, boolean isRight) {
        if (loadingDialog != null) {
            LoadingProgressDialog.Dissmiss();
        }
        creatDialog(context, "", isCancel, isRight);
    }

    public static void show(Context context, boolean isCancel, boolean isRight,
            int runtime) {
        if (loadingDialog != null) {
            LoadingProgressDialog.Dissmiss();
        }
        time = runtime;
        creatDialog(context, "", isCancel, isRight);
    }

    public static void show(Context context, boolean isCancel, boolean isRight,
            int runtime, String text) {
        if (loadingDialog != null) {
            LoadingProgressDialog.Dissmiss();
        }
        time = runtime;
        showtext = text;
        creatDialog(context, "", isCancel, isRight);
    }
    public static void show(Context context, String msg, boolean isCancel,
            boolean isRight) {
        if (loadingDialog != null) {
            LoadingProgressDialog.Dissmiss();
        }
        creatDialog(context, msg, isCancel, isRight);
    }

    private static void creatDialog(Context context, String msg,
            boolean isCancel, boolean isRight) {
        if (handler != null) {
            handler.removeCallbacks(dismissthis);
        }
        loadingDialog = new LoadingDialog(context, R.style.load_dialog);
        loadingDialog.show();
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        ImageView ivLoading = (ImageView) loadingDialog
                .findViewById(R.id.iv_loading_dialog);
        TextView tv = (TextView)loadingDialog.findViewById(R.id.tv_msg);
        if (StringUtils.isEmpty(msg)){
            tv.setVisibility(View.GONE);
        }else{
            tv.setText(msg);
        }
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        ivLoading.startAnimation(hyperspaceJumpAnimation);
        showtext = null;
        loadingDialog.setCancelable(isCancel);
        loadingDialog.setCanceledOnTouchOutside(false);
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(dismissthis, time);

    }

    static Runnable dismissthis = new Runnable() {
        @Override
        public void run() {
            try {
                LoadingProgressDialog.Dissmiss();
            } catch (Exception e) {

            }
        }
    };

    public static void Dissmiss() {
        try {
            // TODO Auto-generated method stub
            if (loadingDialog != null) {
                time = 60000;
                loadingDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}