package com.foodrecipe.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.foodrecipe.R;
import com.foodrecipe.Utils;
import com.foodrecipe.activity.LoginActivity;
import com.foodrecipe.circleTransformation;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class newProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    AppCompatImageView iv_user;
    AppCompatTextView tv_user, email_input, username_input, phone_input, btn_profile_logout;
    String phone, photo, uname, name, email;
    public final int INPUT_FILE_REQUEST_CODE = 100;
    File file;
    private static Uri outputFileUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        initializeSharedPref();

        iv_user = view.findViewById(R.id.iv_user);
        tv_user = view.findViewById(R.id.tv_name);
        email_input = view.findViewById(R.id.email_input);
        username_input = view.findViewById(R.id.username_input);
        phone_input = view.findViewById(R.id.phone_input);
        btn_profile_logout = view.findViewById(R.id.btn_profile_logout);
        btn_profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                editor.clear();
                editor.commit();
                ((AppCompatActivity) getContext()).finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            startActivityForResult(getPickImageChooserIntent(getContext().getPackageManager(), outputFileUri), INPUT_FILE_REQUEST_CODE);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getContext(), "Please Enable Permissions", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });

        setFields();

        return view;
    }

    private void setFields() {
        if (uname != null)
            username_input.setText(uname);
        if (name != null)
            tv_user.setText(name);
        if (phone != null)
            phone_input.setText(phone);
        if (email != null)
            email_input.setText(email);
        if (photo != null && !photo.equals(""))
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/images" + photo).transform(new circleTransformation()).placeholder(R.drawable.user).error(R.drawable.user).into(iv_user);
    }

    private void initializeSharedPref() {
        //shared pref
        try {
            sharedPreferences = Utils.SharedPref(getContext());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUserData();
    }

    private void updateUserData() {
        photo = sharedPreferences.getString("photo", null);
        uname = sharedPreferences.getString("username", null);
        name = sharedPreferences.getString("name", null);
        phone = sharedPreferences.getString("phone", null);
        email = sharedPreferences.getString("email", null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_FILE_REQUEST_CODE) {
            String filePath = null;
            if (data == null || data.getData() == null) {
                UCrop.of(outputFileUri, outputFileUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(1000, 1000)
                        .start((AppCompatActivity) getContext());
            } else {
                UCrop.of(data.getData(), outputFileUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(1000, 1000)
                        .start((AppCompatActivity) getContext(),this,requestCode);
            }

//            file = new File(filePath);
            if (requestCode == UCrop.REQUEST_CROP) {
                filePath = UCrop.getOutput(data).getPath();
                Picasso.get().load(outputFileUri).transform(new circleTransformation()).placeholder(R.drawable.user).into(iv_user);
            }
        }
    }

    private Intent getPickImageChooserIntent(PackageManager packageManager, Uri file) {

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

    private Uri getCaptureImageOutputUri() {
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

    private String getPathFromURI(Uri contentUri, Context context) {
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
