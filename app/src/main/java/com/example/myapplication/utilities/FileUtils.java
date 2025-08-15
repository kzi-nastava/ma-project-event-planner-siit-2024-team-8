package com.example.myapplication.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {

    private FileUtils() {
    }

    public static List<MultipartBody.Part> convertUrisToMultipart(Context context, List<Uri> imageUris,String partName) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();

        for (Uri uri : imageUris) {
            try {
                // Get input stream from the URI
                InputStream inputStream = context.getContentResolver().openInputStream(uri);

                // Create a temp file in your app's cache dir
                String fileName = getFileNameFromUri(context, uri);
                File tempFile = new File(context.getCacheDir(), fileName);

                // Copy stream to temp file
                OutputStream outputStream = new FileOutputStream(tempFile);
                byte[] buffer = new byte[4096];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.close();
                inputStream.close();

                // Create MultipartBody.Part
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), tempFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData(partName, tempFile.getName(), requestFile);
                imageParts.add(body);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageParts;
    }
    private static String getFileNameFromUri(Context context, Uri uri) {
        String result = "image_" + System.currentTimeMillis();
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        } else if (uri.getScheme().equals("file")) {
            result = new File(uri.getPath()).getName();
        }
        return result;
    }
    public static String getRealPathFromURI(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

}

