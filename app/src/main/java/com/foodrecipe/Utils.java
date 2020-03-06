package com.foodrecipe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.foodrecipe.model.myRecipesModel;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.foodrecipe.activity.AddRecipesActivity.outputFileUri;

public class Utils {
    public static String username = "";
    public static String userID = "";
    public static int user_recipes;
    public static double user_rating=0;
    public static Boolean guest = false;
    public static List<myRecipesModel> history = new ArrayList<>();
    public static List<myRecipesModel> saved = new ArrayList<>();

    public static SharedPreferences SharedPref(Context context) throws GeneralSecurityException, IOException {
        String masterKeyAlias = null;
        SharedPreferences sharedPreferences;
        masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        sharedPreferences = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
        return sharedPreferences;
    }

    public static SharedPreferences.Editor editor(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor;
    }

    public static <T> void setList(String key, List<T> list, SharedPreferences.Editor editor) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json, editor);
    }

    private static void set(String key, String value, SharedPreferences.Editor editor) {
        editor.putString(key, value);
        editor.apply();
    }

    public static Intent getPickImageChooserIntent(PackageManager packageManager, Uri file) {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private static Uri getCaptureImageOutputUri() {
        File getImage = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Food Recipe");
        if (!getImage.exists()) {
            getImage.mkdirs();
            if (getImage != null) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                outputFileUri = Uri.fromFile(new File(getImage.getPath(), "foodrecipe_" + timeStamp + ".png"));
            }
        } else {
            if (getImage != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String timeStamp = dateFormat.format(new Date());
                outputFileUri = Uri.fromFile(new File(getImage.getPath(), "foodrecipe_" + timeStamp + ".png"));
            }
        }
        return outputFileUri;
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        if ("com.android.providers.media.documents".equals(contentUri.getAuthority())) {
            final String docId = DocumentsContract.getDocumentId(contentUri);
            final String[] split = docId.split(":");
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            final String[] selectionArgs = new String[]{split[1]};
            final String column = "_data";
            final String[] projection = {column};
            Cursor cursor = context.getContentResolver().query(contentUri, projection, "_id=?", selectionArgs, null);
            cursor.moveToFirst();
            final int index = cursor.getColumnIndexOrThrow(column);
            return cursor.getString(index);

        } else {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

    }
}
