package com.huayinghealth.testaar.entity;

/**
 * Created by ChanLin on 2018/5/21.
 * jetsen
 * TODO:
 */
public class Theater {

    private int user_type;
    private int user_id;
    private String name;
    private String school_name;
    private String school_code;
    private int status;
    private String domain;

    private String school_token;

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_code() {
        return school_code;
    }

    public void setSchool_code(String school_code) {
        this.school_code = school_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSchool_token() {
        return school_token;
    }

    public void setSchool_token(String school_token) {
        this.school_token = school_token;
    }
}
