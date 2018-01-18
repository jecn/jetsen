package com.chanlin.jetsencloud.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by ChanLin on 2018/1/8.
 * jetsenCloud
 * TODO:
 */

public final class DatabaseUtils {
    private static Context sContext;

    public static void setAppContext(Context ctx) {
        sContext = ctx;
    }

    public static Uri createReultUri(String author, String table, long result) {
        Uri result_uri = Uri.parse("content://" + author + "/" + table);
        if (result >= 0)
            result_uri = Uri.withAppendedPath(result_uri,
                    String.valueOf(result));
        return result_uri;
    }

    public static Cursor getRecordsFromTable(String tableName,
                                             String uriParams, String[] project, String selection,
                                             String[] selectionArgs, String orderBy) {
        if (sContext == null)
            return null;
        Uri uri_query = Uri.parse("content://" + DatabaseProvider.AUTHOR + "/" + tableName);
        if (!TextUtils.isEmpty(uriParams))
            uri_query = Uri.withAppendedPath(uri_query, uriParams);
        Cursor cursor = sContext.getContentResolver().query(uri_query, project,
                selection, selectionArgs, orderBy);
        return cursor;
    }

    public static int updateRecordFromTable(String tableName, String uriParams,
                                            ContentValues values, String where, String[] selectionArgs) {
        if (sContext == null)
            return -1;
        Uri uri_query = Uri.parse("content://" + DatabaseProvider.AUTHOR + "/" + tableName);
        if (!TextUtils.isEmpty(uriParams))
            uri_query = Uri.withAppendedPath(uri_query, uriParams);
        int result = sContext.getContentResolver().update(uri_query, values,
                where, selectionArgs);
        return result;
    }

    public static int deleteRecordFromTable(String tableName, String uriParams,
                                            String where, String[] selectionArgs) {
        if (sContext == null)
            return -1;
        Uri uri_query = Uri.parse("content://" + DatabaseProvider.AUTHOR + "/" + tableName);
        if (!TextUtils.isEmpty(uriParams))
            uri_query = Uri.withAppendedPath(uri_query, uriParams);
        int result = sContext.getContentResolver().delete(uri_query, where,
                selectionArgs);
        return result;
    }

    public static Uri insertRecordIntoTable(ContentValues value,
                                            String tableName, String uriParams) {
        if (sContext == null)
            return null;
        Uri uri_query = Uri.parse("content://" + DatabaseProvider.AUTHOR + "/" +tableName);
        if (!TextUtils.isEmpty(uriParams))
            uri_query = Uri.withAppendedPath(uri_query, uriParams);

        Uri result_uri = sContext.getContentResolver().insert(uri_query, value);
        return result_uri;
    }
}
