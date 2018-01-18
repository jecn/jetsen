package com.chanlin.jetsencloud.http;

import java.io.File;

/**
 * Created by ChanLin on 2018/1/12.
 * jetsenCloud
 * TODO:
 */

public interface ReqCallBack<T> {
    void successCallBack(File file);
    void failedCallBack();
}
