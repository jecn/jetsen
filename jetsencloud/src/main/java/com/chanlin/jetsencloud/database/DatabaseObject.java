package com.chanlin.jetsencloud.database;

import android.content.ContentValues;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public final class DatabaseObject {
    //教师信息表
    public static final String UserInfo = "teacher";
    public static final class UserInfoTable{
        public static final String user_teacher_id = "teacher_id";
        private static final String user_name = "name";
        private static final String user_sex = "sex";
        private static final String user_avatar = "avatar";
        private static final String user_school_name = "school_name";
        private static final String user_clasies = "class";
        public static final String[] projection = new String[]{
                user_teacher_id,user_name,user_sex,user_avatar,user_school_name,user_clasies
        };
        public static String CREATE_SQL = "create table if not exists " + UserInfo + "("
                + user_teacher_id + " integer primary key,"
                + user_name + " varchar(200),"
                + user_sex + " integer,"
                + user_avatar + " varchar(200),"
                + user_school_name + " varchar(200),"
                + user_clasies + " varchar(200)"
                + ");";

        public static String DROP_SQL = "drop table if exists " + UserInfo + ";";

        public static ContentValues getContentValues(int teacher_id, String name, int sex, String avatar,
                                                     String school_name, String clasies) {
            ContentValues values = new ContentValues();
            values.put(user_teacher_id, teacher_id);
            values.put(user_name, name);
            values.put(user_sex, sex);
            values.put(user_avatar, avatar);
            values.put(user_school_name, school_name);
            values.put(user_clasies, clasies);
            return values;
        }
    }
    //教材表
    public static final String Book = "book";
    public static final class bookTable{
        public static final String course_id = "course_id";
        public static final String book_id = "id";
        public static final String book_name = "name";
        public static final String[] projection = new String[]{
                course_id,book_id,book_name
        };
        public static String CREATE_SQL = "create table if not exists " + Book + "("
                + course_id + " integer,"
                + book_id + " integer,"
                + book_name + " varchar(200)"
                + ");";

        public static String DROP_SQL = "drop table if exists " + Book + ";";

        public static ContentValues getContentValues(int courseId, int id, String name) {
            ContentValues values = new ContentValues();
            values.put(course_id, courseId);
            values.put(book_id, id);
            values.put(book_name, name);
            return values;
        }
    }
    //课标树
    public static final String CourseStandardTree = "course_standard_tree";
    public static final class CourseStandardTreeTable{
        public static final String tree_book_id = "book_id";
        public static final String tree_id = "id";
        private static final String tree_description = "description";
//        private static final String tree_child = "child";//child 是个json集合类型;
        public static final String tree_parent_id = "parent_id";
        private static final String tree_has_child = "has_child";
        public static final String[] projection = new String[]{
                tree_book_id,tree_id,tree_description,tree_parent_id,tree_has_child
        };
        public static String CREATE_SQL = "create table if not exists " + CourseStandardTree + "("
                + tree_book_id + " integer,"
                + tree_id + " integer,"
                + tree_description + " varchar(200),"
                + tree_parent_id + " integer,"
                + tree_has_child + " integer"
                + ");";

        public static String DROP_SQL = "drop table if exists " + CourseStandardTree + ";";

        public static ContentValues getContentValues(int book_id, int id, String description,int parentId, int hasChild) {
            ContentValues values = new ContentValues();
            values.put(tree_book_id, book_id);
            values.put(tree_id, id);
            values.put(tree_description, description);
            values.put(tree_parent_id,parentId);
            values.put(tree_has_child,hasChild);
            return values;
        }
    }
    //资源列表
    public static final String ResourceTree = "resource_table";
    public static final class ResourceTreeTable{
        public static final String resource_course_standard_id = "course_standard_id";//课标id
        public static final String resource_uuid = "uuid";//文件id
        private static final String resource_key = "key";//文件下载地址
        private static final String resource_title = "title";//资源title
        private static final String resource_size = "size";//资源大小 ，字节
        private static final String resource_type = "type";//type //文件类型(1:word 2:PDF 3:PPT 4:Excel 5:图片 6:视频 7:音频 8:flash
        public static final String resource_file_url = "url";//文件的本地地址
        public static final String[] projection = new String[]{
                resource_course_standard_id,resource_uuid,resource_key,resource_title,resource_size,resource_type,resource_file_url
        };
        public static String CREATE_SQL = "create table if not exists " + ResourceTree + "("
                + resource_course_standard_id + " integer,"
                + resource_uuid + " varchar(200),"
                + resource_key + " varchar(200),"
                + resource_title + " varchar(200),"
                + resource_size + " integer,"
                + resource_type + " integer,"
                + resource_file_url + " varchar(200)"
                + ");";

        public static String DROP_SQL = "drop table if exists " + ResourceTree + ";";

        public static ContentValues getContentValues(int course_standard_id, String uuid, String key,String title,long size,int type,String file_url) {
            ContentValues values = new ContentValues();
            values.put(resource_course_standard_id, course_standard_id);
            values.put(resource_uuid, uuid);
            values.put(resource_key, key);
            values.put(resource_title,title);
            values.put(resource_size,size);
            values.put(resource_type,type);
            values.put(resource_file_url, file_url);
            return values;
        }
    }
    //课堂习题课时列表Q
    public static final String QuestionPeriod = "question_period_table";
    public static final class QuestionPeriodTable{
        public static final String question_period_course_standard_id = "course_standard_id";//课标id
        public static final String question_period_id = "id";//课时id
        private static final String question_period_title = "title";//课时title
        private static final String question_isDownload = "isDownload";//是否下载
        public static final String[] projection = new String[]{
                question_period_course_standard_id,question_period_id,question_period_title,question_isDownload
        };
        public static String CREATE_SQL = "create table if not exists " + QuestionPeriod + "("
                + question_period_course_standard_id + " integer,"
                + question_period_id + " integer,"
                + question_period_title + " varchar(200),"
                + question_isDownload + " varchar(20)"
                + ");";

        public static String DROP_SQL = "drop table if exists " + QuestionPeriod + ";";

        public static ContentValues getContentValues(int course_standard_id, int id, String title, String isDownload) {
            ContentValues values = new ContentValues();
            values.put(question_period_course_standard_id, course_standard_id);
            values.put(question_period_id, id);
            values.put(question_period_title, title);
            values.put(question_isDownload,isDownload);
            return values;
        }
    }
    //11.	课堂-习题课时详情
    public static final String QuestionPeriodDetail = "question_period_detail_table";
    public static final class QuestionPeriodDetailTable{
        public static final String detail_question_period_id = "question_period_id";//课标id
        public static final String detail_uuid = "uuid";//习题的uuid
        private static final String detail_key = "key";//课时title
        public static final String detail_file_url = "url";//习题下载后 的json 文件地址
        public static final String[] projection = new String[]{
                detail_question_period_id,detail_uuid,detail_key,detail_file_url
        };
        public static final String CREATE_SQL = "create table if not exists " + QuestionPeriodDetail + "("
                + detail_question_period_id + " integer,"
                + detail_uuid + " varchar(200),"
                + detail_key + " varchar(200),"
                + detail_file_url + " varchar(200)"
                + ");";

        public static final String DROP_SQL = "drop table if exists " + QuestionPeriodDetail + ";";

        public static ContentValues getContentValues(int question_period_id, String uuid, String key,String url) {
            ContentValues values = new ContentValues();
            values.put(detail_question_period_id, question_period_id);
            values.put(detail_uuid, uuid);
            values.put(detail_key, key);
            values.put(detail_file_url,url);
            return values;
        }
    }
}
