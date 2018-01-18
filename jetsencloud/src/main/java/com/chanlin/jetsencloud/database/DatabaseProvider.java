package com.chanlin.jetsencloud.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public class DatabaseProvider extends ContentProvider {
    public static final String AUTHOR = "ChanLin";
    private static final String TAG = "DatabaseProvider";

    private static final UriMatcher sURLMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    private static final int UserInfo = 0;
    private static final int Book = 1;
    private static final int CourseStandardTree = 2;
    private static final int ResourceTree = 3;
    private static final int QuestionPeriod = 4;
    private static final int QuestionPeriodDetail = 5;

    static {
        sURLMatcher.addURI(AUTHOR, DatabaseObject.UserInfo + "", UserInfo);
        sURLMatcher.addURI(AUTHOR, DatabaseObject.Book + "", Book);
        sURLMatcher.addURI(AUTHOR, DatabaseObject.CourseStandardTree + "", CourseStandardTree);
        sURLMatcher.addURI(AUTHOR, DatabaseObject.ResourceTree + "", ResourceTree);
        sURLMatcher.addURI(AUTHOR, DatabaseObject.QuestionPeriod + "", QuestionPeriod);
        sURLMatcher.addURI(AUTHOR, DatabaseObject.QuestionPeriodDetail + "", QuestionPeriodDetail);
    }
    private DatabaseOpenHelper dbOpenHelper;
    @Override
    public boolean onCreate() {
        Log.e(TAG,"onCreate");
        dbOpenHelper = DatabaseOpenHelper.getInstance(getContext());
        DatabaseUtils.setAppContext(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        int result = sURLMatcher.match(uri);
        if (result == UriMatcher.NO_MATCH)
            return cursor;

        String table  = null;
        String where = null;
        switch (result) {
            case UserInfo:
                table = DatabaseObject.UserInfo;
                break;
            case Book:
                table = DatabaseObject.Book;
                break;
            case CourseStandardTree:
                table = DatabaseObject.CourseStandardTree;
                break;
            case ResourceTree:
                table = DatabaseObject.ResourceTree;
                break;
            case QuestionPeriod:
                table = DatabaseObject.QuestionPeriod;
                break;
            case QuestionPeriodDetail:
                table = DatabaseObject.QuestionPeriodDetail;
                break;
            default:
                throw new IllegalStateException("Unrecognized URI:" + uri);
        }

        if (!TextUtils.isEmpty(where)) {
            selection = where + selection;
        }

        cursor = dbOpenHelper.getReadableDatabase().query(table, projection,
                selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int result = sURLMatcher.match(uri);
        String table = null;
        switch (result){
            case UserInfo:
                table = DatabaseObject.UserInfo;
                break;
            case Book:
                table = DatabaseObject.Book;
                break;
            case CourseStandardTree:
                table = DatabaseObject.CourseStandardTree;
                break;
            case ResourceTree:
                table = DatabaseObject.ResourceTree;
                break;
            case QuestionPeriod:
                table = DatabaseObject.QuestionPeriod;
                break;
            case QuestionPeriodDetail:
                table = DatabaseObject.QuestionPeriodDetail;
                break;
            default:
                throw new IllegalStateException("Unrecognized URI:" + uri);
        }
        long result_id = dbOpenHelper.getWritableDatabase().insert(table,
                null, values);
        if (result > 0) {
            Uri result_uri = DatabaseUtils.createReultUri(AUTHOR, table,
                    result_id);
            getContext().getContentResolver().notifyChange(result_uri, null);
            return result_uri;
        } else {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = sURLMatcher.match(uri);
        if (result == UriMatcher.NO_MATCH)
            return 0;
        String table = null;
        String where = null;
        switch (result){
            case UserInfo:
                table = DatabaseObject.UserInfo;
                break;
            case Book:
                table = DatabaseObject.Book;
                break;
            case CourseStandardTree:
                table = DatabaseObject.CourseStandardTree;
                break;
            case ResourceTree:
                table = DatabaseObject.ResourceTree;
                break;
            case QuestionPeriod:
                table = DatabaseObject.QuestionPeriod;
                break;
            case QuestionPeriodDetail:
                table = DatabaseObject.QuestionPeriodDetail;
                break;
            default:
                throw new IllegalStateException("Unrecognized URI:" + uri);
        }
        if (!TextUtils.isEmpty(where)) {
            selection = where + selection;
        }
        int delete_result = dbOpenHelper.getWritableDatabase().delete(table,
                selection, selectionArgs);
        if (delete_result > 0) {
            Uri result_uri = DatabaseUtils.createReultUri(AUTHOR, table, -1);
            getContext().getContentResolver().notifyChange(result_uri, null);
        }
        return delete_result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int result = sURLMatcher.match(uri);

        if (result == UriMatcher.NO_MATCH)
            return 0;

        String table = null;
        String where = null;
        switch (result) {
            case UserInfo:
                table = DatabaseObject.UserInfo;
                break;
            case Book:
                table = DatabaseObject.Book;
                break;
            case CourseStandardTree:
                table = DatabaseObject.CourseStandardTree;
                break;
            case ResourceTree:
                table = DatabaseObject.ResourceTree;
                break;
            case QuestionPeriod:
                table = DatabaseObject.QuestionPeriod;
                break;
            case QuestionPeriodDetail:
                table = DatabaseObject.QuestionPeriodDetail;
                break;
            default:
                throw new IllegalStateException("Unrecognized URI:" + uri);
        }
        if (!TextUtils.isEmpty(where)) {
            selection = where + selection;
        }

        int update_result = dbOpenHelper.getWritableDatabase().update(table, values,
                selection, selectionArgs);
        if (update_result > 0) {
            Uri result_uri = DatabaseUtils.createReultUri(AUTHOR, table, -1);
            getContext().getContentResolver().notifyChange(result_uri, null);
        }
        return update_result;

    }

    private String createWhere(String fieldName, Uri uri) {
        try {
            String id = uri.getPathSegments().get(1);
            Integer.parseInt(id);
            String where = " " + fieldName + " = " + id + " ";
            return where;
        } catch (NumberFormatException e) {
            Log.e(TAG, "id must be Integer: " + uri);
            return null;
        }
    }
}
