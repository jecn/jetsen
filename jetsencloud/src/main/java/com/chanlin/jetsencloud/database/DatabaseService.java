package com.chanlin.jetsencloud.database;

import android.database.Cursor;
import android.util.Log;

import com.chanlin.jetsencloud.entity.Book;
import com.chanlin.jetsencloud.entity.CourseStandardTree;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.QuestionPeriodDetail;
import com.chanlin.jetsencloud.entity.ResourceTree;
import com.chanlin.jetsencloud.util.FileUtils;
import com.chanlin.jetsencloud.util.LogUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class DatabaseService {
    private static final String TAG = "DatabaseService";
    /**
     * 存用户数据
     */
    public static boolean createUserInfo(int user_teacher_id, String user_name, int user_sex,
                                         String user_avatar,String user_school_name, String user_clasies){
        String where_cause = DatabaseObject.UserInfoTable.user_teacher_id
                + " =? ";
        String[] where_args = new String[] {String.valueOf(user_teacher_id)};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.UserInfo,null,
                    DatabaseObject.UserInfoTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.updateRecordFromTable(DatabaseObject.UserInfo,null,
                        DatabaseObject.UserInfoTable.getContentValues(user_teacher_id, user_name, user_sex,
                                                    user_avatar,user_school_name, user_clasies),
                        where_cause,where_args);
                Log.i(TAG, "createUserInfo update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.UserInfoTable.getContentValues(user_teacher_id, user_name, user_sex,
                                user_avatar,user_school_name, user_clasies),
                        DatabaseObject.UserInfo,null);
                Log.i(TAG, "createUserInfo insertRecordIntoTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 存教材数据
     */
    public static boolean createBook(int course_id, int book_id, String book_name){
        String where_cause = DatabaseObject.bookTable.course_id
                + " =? and "
                + DatabaseObject.bookTable.book_id
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_id),String.valueOf(book_id)};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.Book,null,
                    DatabaseObject.bookTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.updateRecordFromTable(DatabaseObject.Book,null,
                        DatabaseObject.bookTable.getContentValues(course_id, book_id, book_name),
                        where_cause,where_args);
                Log.i(TAG, "createBook update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.bookTable.getContentValues(course_id, book_id, book_name),
                        DatabaseObject.Book,null);
                Log.i(TAG, "createBook insertRecordIntoTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 存课标树数据
     */
    public static boolean createCourseTree(int book_id, int id, String description,int parentId, int hasChild){
        String where_cause = DatabaseObject.CourseStandardTreeTable.tree_book_id
                + " =? and "
                + DatabaseObject.CourseStandardTreeTable.tree_id
                + " =?";
        String[] where_args = new String[] {String.valueOf(book_id),String.valueOf(id)};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.CourseStandardTree,null,
                    DatabaseObject.CourseStandardTreeTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.updateRecordFromTable(DatabaseObject.CourseStandardTree,null,
                        DatabaseObject.CourseStandardTreeTable.getContentValues(book_id, id, description,parentId, hasChild),
                        where_cause,where_args);
                Log.i(TAG, "createBook update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.CourseStandardTreeTable.getContentValues(book_id, id, description,parentId, hasChild),
                        DatabaseObject.CourseStandardTree,null);
                Log.i(TAG, "createBook insertRecordIntoTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 存资源列表数据
     */
    public static String createResourceTree(int course_standard_id, String uuid, String key,
                                             String title,long size,int type,String file_url){
        String where_cause = DatabaseObject.ResourceTreeTable.resource_course_standard_id
                + " =? and "
                + DatabaseObject.ResourceTreeTable.resource_uuid
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_standard_id),uuid};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.ResourceTree,null,
                    DatabaseObject.ResourceTreeTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                /*DatabaseUtils.updateRecordFromTable(DatabaseObject.ResourceTree,null,
                        DatabaseObject.ResourceTreeTable.getContentValues(course_standard_id, uuid, key,title,
                                size,type,file_url),
                        where_cause,where_args);
                Log.i(TAG, "createResourceTree update");*/
                return cursor.getString(6);//把URL返回回去
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.ResourceTreeTable.getContentValues(course_standard_id, uuid, key,title,
                                size,type,file_url),
                        DatabaseObject.ResourceTree,null);
                Log.i(TAG, "createResourceTree insertRecordIntoTable");
                return file_url;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return file_url;
    }


    /**
     * 改资源地址
     */
    public static boolean updateResourceTree(int course_standard_id, String uuid, String key,
                                             String title,long size,int type,String file_url){
        String where_cause = DatabaseObject.ResourceTreeTable.resource_course_standard_id
                + " =? and "
                + DatabaseObject.ResourceTreeTable.resource_uuid
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_standard_id),uuid};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.ResourceTree,null,
                    DatabaseObject.ResourceTreeTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.updateRecordFromTable(DatabaseObject.ResourceTree,null,
                        DatabaseObject.ResourceTreeTable.getContentValues(course_standard_id, uuid, key,title,
                                size,type,file_url),
                        where_cause,where_args);
                Log.i(TAG, "createResourceTree update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.ResourceTreeTable.getContentValues(course_standard_id, uuid, key,title,
                                size,type,file_url),
                        DatabaseObject.ResourceTree,null);
                Log.i(TAG, "createResourceTree insertRecordIntoTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 课堂习题课时列表Q
     */
    public static String createQuestionPeriodTable(int course_standard_id, int id, String title,String isDownload){
        String where_cause = DatabaseObject.QuestionPeriodTable.question_period_course_standard_id
                + " =? and "
                + DatabaseObject.QuestionPeriodTable.question_period_id
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_standard_id),String.valueOf(id)};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriod,null,
                    DatabaseObject.QuestionPeriodTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
             /*   DatabaseUtils.updateRecordFromTable(DatabaseObject.QuestionPeriod,null,
                        DatabaseObject.QuestionPeriodTable.getContentValues(course_standard_id, id,title,isDownload),
                        where_cause,where_args);
                Log.i(TAG, "createQuestionPeriodTable update");
                */
                String str = cursor.getString(3);
                return cursor.getString(3);//把是否 存在数据库字段返回
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.QuestionPeriodTable.getContentValues(course_standard_id, id,title,isDownload),
                        DatabaseObject.QuestionPeriod,null);
                Log.i(TAG, "createQuestionPeriodTable insertRecordIntoTable");
                return isDownload;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isDownload;
    }



    /**
     * 课堂习题课时列表Q
     */
    public static boolean updateQuestionPeriodTable(int course_standard_id, int id, String title,String isDownload){
        String where_cause = DatabaseObject.QuestionPeriodTable.question_period_course_standard_id
                + " =? and "
                + DatabaseObject.QuestionPeriodTable.question_period_id
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_standard_id),String.valueOf(id)};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriod,null,
                    DatabaseObject.QuestionPeriodTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
               DatabaseUtils.updateRecordFromTable(DatabaseObject.QuestionPeriod,null,
                        DatabaseObject.QuestionPeriodTable.getContentValues(course_standard_id, id,title,isDownload),
                        where_cause,where_args);
                Log.i(TAG, "updateQuestionPeriodTable update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.QuestionPeriodTable.getContentValues(course_standard_id, id,title,isDownload),
                        DatabaseObject.QuestionPeriod,null);
                Log.i(TAG, "updateQuestionPeriodTable updateQuestionPeriodTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    /**
     * 存 课堂-习题课时详情
     */
    public static boolean createQuestionPeriodDetailTable(int course_standard_id, String uuid, String key,String url){
        String where_cause = DatabaseObject.QuestionPeriodDetailTable.detail_question_period_id
                + " =? and "
                + DatabaseObject.QuestionPeriodDetailTable.detail_uuid
                + " =?";
        String[] where_args = new String[] {String.valueOf(course_standard_id),uuid};
        Cursor cursor = null;
        try {
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriodDetail,null,
                    DatabaseObject.QuestionPeriodDetailTable.projection,where_cause,
                    where_args,null);
            if (cursor != null && cursor.moveToFirst()) {
                DatabaseUtils.updateRecordFromTable(DatabaseObject.QuestionPeriodDetail,null,
                        DatabaseObject.QuestionPeriodDetailTable.getContentValues(course_standard_id, uuid,key,url),
                        where_cause,where_args);
                Log.i(TAG, "createQuestionPeriodDetailTable update");
                return true;
            }else {
                DatabaseUtils.insertRecordIntoTable(
                        DatabaseObject.QuestionPeriodDetailTable.getContentValues(course_standard_id, uuid,key,url),
                        DatabaseObject.QuestionPeriodDetail,null);
                Log.i(TAG, "createQuestionPeriodDetailTable insertRecordIntoTable");
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }


    /**
     * 查教材表
     */
    public static ArrayList<Book> findBookList(int course_id){
        ArrayList<Book> bookList = new ArrayList<Book>();
        LogUtil.showInfo(TAG,"findBookList course_id="+course_id);
        String where_cause = DatabaseObject.bookTable.course_id
                + " =? ";
        String[] where_args = new String[] { String.valueOf(course_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.Book,
                    null, DatabaseObject.bookTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                Book book = new Book();
                book.setCourse_id(course_id);
                book.setId(cursor.getInt(1));
                book.setName(cursor.getString(2));
                bookList.add(book);
                LogUtil.showInfo("database", "findBookList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findBookList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findBookList exception:");
        }finally{
            LogUtil.showInfo("database","findBookList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }

    /**
     * 查课标树
     */
    public static ArrayList<CourseStandardTree> findCourseStandardTreeList(int tree_book_id){
        ArrayList<CourseStandardTree> bookList = new ArrayList<CourseStandardTree>();
        LogUtil.showInfo(TAG,"findBookList tree_book_id="+tree_book_id);
        String where_cause = DatabaseObject.CourseStandardTreeTable.tree_book_id
                + " =? ";
        String[] where_args = new String[] { String.valueOf(tree_book_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.CourseStandardTree,
                    null, DatabaseObject.CourseStandardTreeTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                CourseStandardTree book = new CourseStandardTree();
                book.setBook_id(tree_book_id);
                book.setId(cursor.getInt(1));
                book.setDescription(cursor.getString(2));
//                book.setChild(cursor.getString(3));
                book.setParentId(cursor.getInt(3));
                book.setHasChild(cursor.getInt(4));
                bookList.add(book);
                LogUtil.showInfo("database", "findCourseStandardTreeList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findCourseStandardTreeList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findCourseStandardTreeList exception:");
        }finally{
            LogUtil.showInfo("database","findCourseStandardTreeList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }


    public static ArrayList<CourseStandardTree> findCourseStandardTreeList(int tree_book_id, int parentId){
        ArrayList<CourseStandardTree> bookList = new ArrayList<CourseStandardTree>();
        LogUtil.showInfo(TAG,"findBookList tree_book_id="+tree_book_id);
        String where_cause = DatabaseObject.CourseStandardTreeTable.tree_book_id
                + " =? and "
                + DatabaseObject.CourseStandardTreeTable.tree_parent_id
                + " =? ";
        String[] where_args = new String[] { String.valueOf(tree_book_id),String.valueOf(parentId)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.CourseStandardTree,
                    null, DatabaseObject.CourseStandardTreeTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                CourseStandardTree book = new CourseStandardTree();
                book.setBook_id(tree_book_id);
                book.setId(cursor.getInt(1));
                book.setDescription(cursor.getString(2));
//                book.setChild(cursor.getString(3));
                book.setParentId(cursor.getInt(3));
                book.setHasChild(cursor.getInt(4));
                bookList.add(book);
                LogUtil.showInfo("database", "findCourseStandardTreeList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findCourseStandardTreeList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findCourseStandardTreeList exception:");
        }finally{
            LogUtil.showInfo("database","findCourseStandardTreeList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }

    /**
     * 查资源列表
     */
    public static ArrayList<ResourceTree> findResourceTreeList(int resource_course_standard_id){
        ArrayList<ResourceTree> bookList = new ArrayList<ResourceTree>();
        LogUtil.showInfo(TAG,"findBookList resource_course_standard_id="+resource_course_standard_id);
        String where_cause = DatabaseObject.ResourceTreeTable.resource_course_standard_id
                + " =? and "
                + DatabaseObject.ResourceTreeTable.resource_file_url
                + " is not null";
        String[] where_args = new String[] { String.valueOf(resource_course_standard_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.ResourceTree,
                    null, DatabaseObject.ResourceTreeTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                ResourceTree book = new ResourceTree();
                book.setCourse_standard_id(resource_course_standard_id);
                book.setUuid(cursor.getString(1));
                book.setKey(cursor.getString(2));
                book.setTitle(cursor.getString(3));
                book.setSize(cursor.getLong(4));
                book.setType(cursor.getInt(5));
                book.setFile_url(cursor.getString(6));
                bookList.add(book);
                LogUtil.showInfo("database", "findCourseStandardTreeList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findCourseStandardTreeList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findCourseStandardTreeList exception:");
        }finally{
            LogUtil.showInfo("database","findCourseStandardTreeList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }

    /**
     * 查课堂习题课时列表
     */
    public static ArrayList<QuestionPeriod> findQuestionPeriodList(int question_period_course_standard_id){
        ArrayList<QuestionPeriod> bookList = new ArrayList<QuestionPeriod>();
        LogUtil.showInfo(TAG,"findBookList question_period_course_standard_id="+question_period_course_standard_id);
        String where_cause = DatabaseObject.QuestionPeriodTable.question_period_course_standard_id
                + " =? ";
        String[] where_args = new String[] { String.valueOf(question_period_course_standard_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriod,
                    null, DatabaseObject.QuestionPeriodTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                QuestionPeriod book = new QuestionPeriod();
                book.setCourse_standard_id(question_period_course_standard_id);
                book.setId(cursor.getInt(1));
                book.setTitle(cursor.getString(2));
                book.setIsDownload(cursor.getString(3));
                bookList.add(book);
                LogUtil.showInfo("database", "findResourceTreeList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findResourceTreeList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findResourceTreeList exception:");
        }finally{
            LogUtil.showInfo("database","findResourceTreeList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }


    /**
     * 查课堂习题课时列表
     */
    public static ArrayList<QuestionPeriodDetail> findQuestionPeriodDetailList(int detail_question_period_id){
        ArrayList<QuestionPeriodDetail> bookList = new ArrayList<QuestionPeriodDetail>();
        LogUtil.showInfo(TAG,"findBookList detail_question_period_id="+detail_question_period_id);
        String where_cause = DatabaseObject.QuestionPeriodDetailTable.detail_question_period_id
                + " =? ";
        String[] where_args = new String[] { String.valueOf(detail_question_period_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriodDetail,
                    null, DatabaseObject.QuestionPeriodDetailTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                QuestionPeriodDetail book = new QuestionPeriodDetail();
                book.setQuestion_period_id(detail_question_period_id);
                book.setUuid(cursor.getString(1));
                book.setKey(cursor.getString(2));
                book.setUrl(cursor.getString(3));
                bookList.add(book);
                LogUtil.showInfo("database", "findQuestionPeriodDetailList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findQuestionPeriodDetailList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findQuestionPeriodDetailList exception:");
        }finally{
            LogUtil.showInfo("database","findQuestionPeriodDetailList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }


    /**'
     * 查找 已经下载了的文件
     * @param detail_question_period_id
     * @return
     */
    public static ArrayList<QuestionPeriodDetail> findQuestionPeriodDetailListWhereUrlNotNull(int detail_question_period_id){
        ArrayList<QuestionPeriodDetail> bookList = new ArrayList<QuestionPeriodDetail>();
        LogUtil.showInfo(TAG,"findBookList detail_question_period_id="+detail_question_period_id);
        String where_cause = DatabaseObject.QuestionPeriodDetailTable.detail_question_period_id
                + " =? and "
                + DatabaseObject.QuestionPeriodDetailTable.detail_file_url
                + " is not null";
        String[] where_args = new String[] { String.valueOf(detail_question_period_id)};
        Cursor cursor=null;
        try {
            cursor=DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriodDetail,
                    null, DatabaseObject.QuestionPeriodDetailTable.projection, where_cause,
                    where_args, null);
            while(cursor.moveToNext()){
                QuestionPeriodDetail book = new QuestionPeriodDetail();
                book.setQuestion_period_id(detail_question_period_id);
                book.setUuid(cursor.getString(1));
                book.setKey(cursor.getString(2));
                book.setUrl(cursor.getString(3));
                bookList.add(book);
                LogUtil.showInfo("database", "findQuestionPeriodDetailList:"+bookList.toString());
            }
            LogUtil.showInfo("database","findQuestionPeriodDetailList over:");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.showInfo("database","findQuestionPeriodDetailList exception:");
        }finally{
            LogUtil.showInfo("database","findQuestionPeriodDetailList finally:");
            if(cursor != null ) cursor.close();
        }
        return bookList;
    }

    /**
     * 删除资源 文件数据和数据库数据
     */
    public static void deleteResourceTree(String uuid){
        String where_cause = DatabaseObject.ResourceTreeTable.resource_uuid
                + " =? ";
        String[] where_args = new String[]{String.valueOf(uuid)};
        Cursor cursor = null;
        try{
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.ResourceTree,
                    null,DatabaseObject.ResourceTreeTable.projection, where_cause,where_args,null);
            while (cursor.moveToNext()){
                ResourceTree resourceTree = new ResourceTree();
                resourceTree.setFile_url(cursor.getString(6));

                boolean a = FileUtils.deleteFile(new File(cursor.getString(6)));
                LogUtil.showInfo("resource",cursor.getString(6));
                LogUtil.showInfo("delete resource",String.valueOf(a));
            }
            int count  = DatabaseUtils.deleteRecordFromTable(DatabaseObject.ResourceTree,null,where_cause,where_args);
            LogUtil.showInfo("delete count",String.valueOf(count));
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.showInfo("database","deleteResourceTree exception:");
        }finally {
            LogUtil.showInfo("database","deleteResourceTree finally:");
            if(cursor != null ) cursor.close();
        }


    }
    /**
     * //删除题目,先删课时，再删题目，删本地题目文件
     * 传入课标树id，删除 课时表中的课标树id相关的数据
     */
    public static void deleteQuestionPeriodListAnddetails(int course_standard_id){
        String where_cause = DatabaseObject.QuestionPeriodTable.question_period_course_standard_id
                + " =? ";
        String[] where_args = new String[]{String.valueOf(course_standard_id)};
        Cursor cursor = null;
        try{
            cursor = DatabaseUtils.getRecordsFromTable(DatabaseObject.QuestionPeriod,
                    null, DatabaseObject.QuestionPeriodTable.projection, where_cause, where_args,null);
            while (cursor.moveToNext()){
                QuestionPeriod questionPeriod = new QuestionPeriod();
                questionPeriod.setCourse_standard_id(cursor.getInt(0));
                questionPeriod.setId(cursor.getInt(1));
                int countPeriod = DatabaseUtils.deleteRecordFromTable(DatabaseObject.QuestionPeriod,null,where_cause,where_args);
                String where_detail_cause = DatabaseObject.QuestionPeriodDetailTable.detail_question_period_id
                        + " =? ";
                String[] where_detail_args = new String[]{String.valueOf(course_standard_id)};
                int countPeriodDetails = DatabaseUtils.deleteRecordFromTable(DatabaseObject.QuestionPeriodDetail,null,where_detail_cause,where_detail_args);
                LogUtil.showInfo("delete question","countPeriod="+countPeriod + " /t countPeriodDetails="+countPeriodDetails);
            }

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.showInfo("database","deleteQuestionPeriodListAnddetails exception:");
        }finally {
            LogUtil.showInfo("database","deleteQuestionPeriodListAnddetails finally:");
            if(cursor != null ) cursor.close();
        }
    }
}














