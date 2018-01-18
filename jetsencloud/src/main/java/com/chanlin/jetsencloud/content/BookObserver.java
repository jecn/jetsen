package com.chanlin.jetsencloud.content;

import android.database.ContentObserver;
import android.os.Handler;

import com.chanlin.jetsencloud.util.LogUtil;

/**
 * Created by ChanLin on 2018/1/10.
 * jetsenCloud
 * TODO:
 */

public class BookObserver extends ContentObserver {
    private static String TAG = "BookObserver";
    public static final int BookObserver_update = 30001;
    private Handler mHandler;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public BookObserver(Handler handler) {
        super(handler);
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        LogUtil.showInfo(TAG,"BookObserver onchange");
        super.onChange(selfChange);
        try{
            mHandler.sendEmptyMessage(BookObserver_update);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
