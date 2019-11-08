package com.liuzhongqiang.myapplication.uritool;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriUtils {

    /**
     * @param context mContext
     * @param path media path
     * @return media uri
     */
    public static Uri getMediaUriFromPath(Context context, String path){
        Uri externalMediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = null;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "=?",
                new String[]{path.substring(path.lastIndexOf('/') + 1)},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            uri = ContentUris.withAppendedId(externalMediaUri, id);
            cursor.close();
        }
        return uri;
    }

    /**
     * @param context mContext
     * @param uri media uri
     * @return media path
     */
    public static String getMediaPathFromUri(Context context, Uri uri){
        if (uri == null){
            return null;
        }
        String path = null;
        Uri externalMediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                new String[]{MediaStore.Audio.Media.DATA},
                MediaStore.Audio.Media._ID + "=?",
                new String[]{uri.getLastPathSegment()},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            cursor.close();
        }
        return path;
    }
}
