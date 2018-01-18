package com.chanlin.jetsencloud.entity;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class QuestionPeriod {
    private int course_standard_id;
    private int id;
    private String title;

    public int getCourse_standard_id() {
        return course_standard_id;
    }

    public void setCourse_standard_id(int course_standard_id) {
        this.course_standard_id = course_standard_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "QuestionPeriod{" +
                "course_standard_id=" + course_standard_id +
                ", id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
