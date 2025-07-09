package com.example.myapplication.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.domain.PriceListItem;
import com.example.myapplication.domain.dto.EventInfoResponse;
import com.example.myapplication.domain.dto.GuestResponse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {

    private FileUtils() {
    }

    public static List<MultipartBody.Part> convertUrisToMultipart(Context context, List<Uri> imageUris) {
        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : imageUris) {
            File file = new File(getRealPathFromURI(context, uri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
            imageParts.add(body);
        }
        return imageParts;
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

