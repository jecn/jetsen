package com.chanlin.jetsencloud.entity;

/**
 * Created by ChanLin on 2018/1/15.
 */
public class QuestionContent {
    private String uuid;//题目uuid
    private int type;//题目类型 1 选择；2 判断/3 填空；4 解答
    private int options;//选择题选项数
    private String answer;
    private String title;//题干
    private String title_key;//题干地址，可能为HTML，可能为base64 图片
    private String pid_title;//父题干
    private String pid_title_key;//父提干， 已解析，可能为HTML，也可能为 base64图片
    private String parse;//解析
    private String parse_key;//解析地址，可能为HTML，也可能为base64图片

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_key() {
        return title_key;
    }

    public void setTitle_key(String title_key) {
        this.title_key = title_key;
    }

    public String getPid_title() {
        return pid_title;
    }

    public void setPid_title(String pid_title) {
        this.pid_title = pid_title;
    }

    public String getPid_title_key() {
        return pid_title_key;
    }

    public void setPid_title_key(String pid_title_key) {
        this.pid_title_key = pid_title_key;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
    }

    public String getParse_key() {
        return parse_key;
    }

    public void setParse_key(String parse_key) {
        this.parse_key = parse_key;
    }
}
