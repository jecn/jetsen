package com.chanlin.jetsencloud.entity;

import java.io.Serializable;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class ResourceTree implements Serializable{
    private int course_standard_id;
    private  String uuid;
    private String key;
    private String title;
    private long size;
    private int type;
    private String file_url;

    public int getCourse_standard_id() {
        return course_standard_id;
    }

    public void setCourse_standard_id(int course_standard_id) {
        this.course_standard_id = course_standard_id;
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

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    @Override
    public String toString() {
        return "ResourceTree{" +
                "course_standard_id=" + course_standard_id +
                ", uuid='" + uuid + '\'' +
                ", key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", type=" + type +
                ", file_url='" + file_url + '\'' +
                '}';
    }
}
