package com.chanlin.jetsencloud.util;

import com.chanlin.jetsencloud.database.DatabaseService;
import com.chanlin.jetsencloud.entity.Book;
import com.chanlin.jetsencloud.entity.CourseStandardTree;
import com.chanlin.jetsencloud.entity.QuestionPeriod;
import com.chanlin.jetsencloud.entity.ResourceTree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChanLin on 2018/1/10.
 * jetsenCloud
 * TODO:
 */

public class JsonSuccessUtil {
    /**
     * 解析教材版本
     *
     * @param courseId
     * @param jstr
     * @return
     * @throws JSONException
     */
    public static ArrayList<Book> getBookList(int courseId, String jstr) throws JSONException {
        ArrayList<Book> books = new ArrayList<>();
        JSONObject dataJson = new JSONObject(jstr);
        if (!dataJson.has("list"))
            return books;
        JSONArray resultsArrayJson = dataJson.getJSONArray("list");
        for (int a = 0; a < resultsArrayJson.length(); a++) {
            JSONObject resultsJson = resultsArrayJson.getJSONObject(a);
            Book book = new Book();
            book.setCourse_id(courseId);
            book.setId(resultsJson.getInt("id"));
            book.setName(resultsJson.getString("name"));
            books.add(book);
            //入库
            DatabaseService.createBook(courseId, book.getId(), book.getName());
        }
        return books;
    }

    public static ArrayList<CourseStandardTree> getCourseStandardTree(int bookId, String jstr) throws JSONException {
        ArrayList<CourseStandardTree> courseStandardTreeList = new ArrayList<>();
        JSONObject dataJson = new JSONObject(jstr);
        if (!dataJson.has("list"))
            return courseStandardTreeList;
        JSONArray resultsArrayJson = dataJson.getJSONArray("list");
        for (int a = 0; a < resultsArrayJson.length(); a++) {
            JSONObject resultsJson = resultsArrayJson.getJSONObject(a);
            CourseStandardTree courseStandardTree = new CourseStandardTree();
            courseStandardTree.setBook_id(bookId);
            courseStandardTree.setId(resultsJson.getInt("id"));
            courseStandardTree.setDescription(resultsJson.getString("description"));
            //courseStandardTree.setChild();
            courseStandardTree.setParentId(0);//根目录为0
            String child = resultsJson.getString("child");
            JSONArray childArray = new JSONArray(child);
            if (childArray != null && childArray.length() > 0) {
                pullChild(bookId, childArray, courseStandardTree.getId());
                courseStandardTree.setHasChild(1);//有子目录
            } else {
                courseStandardTree.setHasChild(0);//没有子目录
            }

            courseStandardTreeList.add(courseStandardTree);
            //入库
            DatabaseService.createCourseTree(bookId, courseStandardTree.getId(), courseStandardTree.getDescription(),
                    courseStandardTree.getParentId(), courseStandardTree.getHasChild());
        }
        return courseStandardTreeList;
    }

    //这个方法需要回调使用
    private static void pullChild(int bookId, JSONArray childArray, int parentId) throws JSONException {
        ArrayList<CourseStandardTree> courseStandardTreeList = new ArrayList<>();
        for (int a = 0; a < childArray.length(); a++) {
            JSONObject resultsJson = childArray.getJSONObject(a);
            CourseStandardTree courseStandardTree = new CourseStandardTree();
            courseStandardTree.setBook_id(bookId);
            courseStandardTree.setId(resultsJson.getInt("id"));
            courseStandardTree.setDescription(resultsJson.getString("description"));
            courseStandardTree.setParentId(parentId);//根目录
            //courseStandardTree.setChild();
            String child = resultsJson.getString("child");
            JSONArray childList = new JSONArray(child);
            if (childArray != null && childList.length() > 0) {
                pullChild(bookId, childList, courseStandardTree.getId());//子目录的 父 id 都为当前传入的id
                courseStandardTree.setHasChild(1);//有子目录
            } else {
                courseStandardTree.setHasChild(0);//没有子目录
            }
            courseStandardTreeList.add(courseStandardTree);
            //入库
            DatabaseService.createCourseTree(bookId, courseStandardTree.getId(), courseStandardTree.getDescription(),
                    courseStandardTree.getParentId(), courseStandardTree.getHasChild());
        }
    }

    /**
     * 解析资源列表
     */
    public static ArrayList<ResourceTree> getResourceTreeList(int course_standard_id, String jstr) throws JSONException {
        ArrayList<ResourceTree> resourceTrees = new ArrayList<>();
        JSONObject dataJson = new JSONObject(jstr);
        if (!dataJson.has("list"))
            return resourceTrees;
        JSONArray resultsArrayJson = dataJson.getJSONArray("list");
        for (int a = 0; a < resultsArrayJson.length(); a++) {
            JSONObject resultsJson = resultsArrayJson.getJSONObject(a);
            ResourceTree tree = new ResourceTree();
            tree.setCourse_standard_id(course_standard_id);
            tree.setUuid(resultsJson.getString("uuid"));
            tree.setKey(resultsJson.getString("key"));
            tree.setTitle(resultsJson.getString("title"));
            tree.setSize(resultsJson.getLong("size"));
            tree.setType(resultsJson.getInt("type"));

            //入库,如果数据库已经存在，则返回这条数据的 文件地址
            String url = DatabaseService.createResourceTree(course_standard_id, tree.getUuid(), tree.getKey(), tree.getTitle(), tree.getSize(), tree.getType(), "");
            tree.setFile_url(url);
            resourceTrees.add(tree);
        }
        return resourceTrees;
    }

    /**
     * 解析课时列表数据
     */
    public static ArrayList<QuestionPeriod> getQuestionPeriodList(int course_standard_id, String jstr) throws JSONException {
        ArrayList<QuestionPeriod> resourceQuestionPeriodList = new ArrayList<>();
        JSONObject dataJson = new JSONObject(jstr);
        if (!dataJson.has("list"))
            return resourceQuestionPeriodList;
        JSONArray resultsArrayJson = dataJson.getJSONArray("list");
        for (int a = 0; a < resultsArrayJson.length(); a++) {
            JSONObject resultsJson = resultsArrayJson.getJSONObject(a);
            QuestionPeriod question = new QuestionPeriod();
            question.setCourse_standard_id(course_standard_id);
            question.setId(resultsJson.getInt("id"));
            question.setTitle(resultsJson.getString("title"));
            question.setIsDownload("0");//默认为下载

            //入库
            String isDownlaod = DatabaseService.createQuestionPeriodTable(course_standard_id, question.getId(), question.getTitle(),question.getIsDownload());
            question.setIsDownload(isDownlaod);
            resourceQuestionPeriodList.add(question);
        }
        return resourceQuestionPeriodList;
    }
}
