package com.liuzhongqiang.myapplication.uritool;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class UriUtils {

    public static final String AUDIO = "audio";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";

    public static Uri getMediaUriFromPath(Context context, String path) {
        String mediaType = getMediaType(path);
        if (mediaType == null) {
            return null;
        }
        switch (mediaType) {
            case AUDIO :
                return getAudioUriFromPath(context, path);
            case IMAGE :
                return getImageUriFromPath(context, path);
            case VIDEO :
                return getVideoUriFromPath(context, path);
            default:
                break;
        }
        return null;
    }

    public static String getMediaPathFromUri(Context context, Uri uri) {
        return null;
    }

    public static Uri getAudioUriFromPath(Context context, String path) {
        Uri externalMediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri uri = null;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                null,
                MediaStore.Audio.Media.DISPLAY_NAME + "=?",
                new String[]{path.substring(path.lastIndexOf('/') + 1)},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            uri = ContentUris.withAppendedId(externalMediaUri, id);
            cursor.close();
        }
        return uri;
    }

    public static String getAudioPathFromUri(Context context, Uri uri) {
        if (uri == null) {
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

    public static Uri getImageUriFromPath(Context context, String path) {
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

    public static String getImagePathFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        Uri externalMediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                new String[]{MediaStore.Images.Media.DATA},
                MediaStore.Images.Media._ID + "=?",
                new String[]{uri.getLastPathSegment()},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    public static Uri getVideoUriFromPath(Context context, String path) {
        Uri externalMediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri uri = null;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                null,
                MediaStore.Video.Media.DISPLAY_NAME + "=?",
                new String[]{path.substring(path.lastIndexOf('/') + 1)},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            uri = ContentUris.withAppendedId(externalMediaUri, id);
            cursor.close();
        }
        return uri;
    }

    public static String getVideoPathFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String path = null;
        Uri externalMediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(externalMediaUri,
                new String[]{MediaStore.Video.Media.DATA},
                MediaStore.Video.Media._ID + "=?",
                new String[]{uri.getLastPathSegment()},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            cursor.close();
        }
        return path;
    }

    public static String getMediaType(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension == null && url.indexOf('.') != -1) {
            extension = url.substring(url.lastIndexOf('.') + 1);
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (type != null) {
            Log.i("zzz", type);
            return type.substring(0, type.indexOf('/'));
        }
        return null;
    }
}
