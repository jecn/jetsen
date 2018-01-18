package com.chanlin.jetsencloud.entity;

import java.io.Serializable;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class QuestionPeriodDetail implements Serializable{
    private int question_period_id;
    private String uuid;
    private String key;
    private String url;
    private boolean ischecked;//发送的是否被选中

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public int getQuestion_period_id() {
        return question_period_id;
    }

    public void setQuestion_period_id(int question_period_id) {
        this.question_period_id = question_period_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String title) {
        this.key = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "QuestionPeriodDetail{" +
                "question_period_id=" + question_period_id +
                ", uuid='" + uuid + '\'' +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
