package com.chanlin.jetsencloud.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static DatabaseOpenHelper sInstance = null;
    public static final String DATABASE_NAME = "jetsen_cloud_database.db";
    public static final int DATABASE_VERSION = 1;
    public static DatabaseOpenHelper getInstance(Context context){
        if(null == sInstance) {
            sInstance = new DatabaseOpenHelper(context);
        }
        return  sInstance;
    }
    private DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserInfoTable(db);
        createBookTable(db);
        createCourseStandardTreeTable(db);
        createResourceTreeTable(db);
        createQuestionPeriodTable(db);
        createQuestionPeriodDetailTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //创建教师 表
    private void createUserInfoTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.UserInfoTable.DROP_SQL);
        db.execSQL(DatabaseObject.UserInfoTable.CREATE_SQL);
    }
    //创建教材表
    private void createBookTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.bookTable.DROP_SQL);
        db.execSQL(DatabaseObject.bookTable.CREATE_SQL);
    }
    //创建课标树
    private void createCourseStandardTreeTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.CourseStandardTreeTable.DROP_SQL);
        db.execSQL(DatabaseObject.CourseStandardTreeTable.CREATE_SQL);
    }
    //创建资源列表
    private void createResourceTreeTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.ResourceTreeTable.DROP_SQL);
        db.execSQL(DatabaseObject.ResourceTreeTable.CREATE_SQL);
    }
    //创建课堂习题课时表
    private void createQuestionPeriodTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.QuestionPeriodTable.DROP_SQL);
        db.execSQL(DatabaseObject.QuestionPeriodTable.CREATE_SQL);
    }
    //创建课堂-习题课时详情 表
    private void createQuestionPeriodDetailTable(SQLiteDatabase db){
        db.execSQL(DatabaseObject.QuestionPeriodDetailTable.DROP_SQL);
        db.execSQL(DatabaseObject.QuestionPeriodDetailTable.CREATE_SQL);
    }
}
