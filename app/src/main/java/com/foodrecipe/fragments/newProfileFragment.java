package com.foodrecipe.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.activity.LoginActivity;
import com.foodrecipe.circleTransformation;
import com.foodrecipe.model.ResponseData;
import com.foodrecipe.model.UploadObject;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newProfileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private AppCompatImageView iv_user,edit;
    private AppCompatTextView tv_user, btn_profile_logout, tv_recipe, tv_rating, btn_profile_save;
    private AppCompatEditText email_input, username_input, phone_input, password_input;
    private String phone, photo, uname, name, email, password;
    private final int INPUT_FILE_REQUEST_CODE = 100;
    private File file;
    private static Uri outputFileUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, container, false);
        initializeSharedPref();

        tv_recipe = view.findViewById(R.id.tv_recipe);
        btn_profile_save = view.findViewById(R.id.btn_profile_save);
        edit = view.findViewById(R.id.edit);
        tv_rating = view.findViewById(R.id.tv_rating);
        iv_user = view.findViewById(R.id.iv_user);
        tv_user = view.findViewById(R.id.tv_name);
        email_input = view.findViewById(R.id.email_input);
        password_input = view.findViewById(R.id.password_input);
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_input.setEnabled(true);
                password_input.setEnabled(true);
                username_input.setEnabled(true);
                phone_input.setEnabled(true);
                btn_profile_logout.setVisibility(View.GONE);
                btn_profile_save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
            }
        });
        btn_profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.setVisibility(View.VISIBLE);
                validateData(String.valueOf(tv_user.getText()), phone_input.getText().toString(), email_input.getText().toString(), password_input.getText().toString(), username_input.getText().toString());
//                updateProfile(String.valueOf(tv_user.getText()), phone_input.getText().toString(), email_input.getText().toString(), password_input.getText().toString(), username_input.getText().toString());
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

        //keylisteners
        email_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(username_input, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        password_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    btn_profile_save.performClick();
                    btn_profile_save.setPressed(true);
                    btn_profile_save.invalidate();
                }
                return false;
            }
        });

        phone_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(password_input, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        username_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(phone_input, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        setFields();

        return view;
    }

    private void setFields() {
        tv_recipe.setText(String.valueOf(Utils.user_recipes));
        tv_rating.setText(String.valueOf(Utils.user_rating));
        if (uname != null)
            username_input.setText(uname);
        if (name != null)
            tv_user.setText(name);
        if (phone != null)
            phone_input.setText(phone);
        if (email != null)
            email_input.setText(email);
        if (password != null)
            password_input.setText(password);
        if (photo != null && !photo.equals(""))
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + photo).transform(new circleTransformation()).placeholder(R.drawable.user).error(R.drawable.user).into(iv_user);
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
        password = sharedPreferences.getString("password", null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INPUT_FILE_REQUEST_CODE && requestCode != UCrop.REQUEST_CROP) {

            if (data == null || data.getData() == null) {
                UCrop.of(outputFileUri, outputFileUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(1000, 1000)
                        .start(getContext(), this, UCrop.REQUEST_CROP);
            } else {
                UCrop.Options options = new UCrop.Options();
                UCrop.of(data.getData(), outputFileUri)
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(1000, 1000)
                        .start(getContext(), this, UCrop.REQUEST_CROP);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && data != null && data.getData() != null) {
            String filePath = UCrop.getOutput(data).getPath();
            file = new File(filePath);
//            new uploadPhoto().execute();
            uploadPhoto1();
            Picasso.get().load(outputFileUri).transform(new circleTransformation()).placeholder(R.drawable.user).into(iv_user);
        }

    }

    private void validateData(final String nameET, final String phoneET, final String emailET, final String passET, final String usernameET) {
        if (nameET.trim().equals("") || nameET.trim().equals(" ")) {
            Toast.makeText(getContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
        } else if (phoneET.trim().equals("") || phoneET.trim().equals(" ")) {
            Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else if (emailET.trim().equals("") || emailET.trim().equals(" ")) {
            Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (passET.trim().equals("") || passET.trim().equals(" ")) {
            Toast.makeText(getContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (usernameET.trim().equals("") || usernameET.trim().equals(" ")) {
            Toast.makeText(getContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
        } else
            updateProfile(nameET, phoneET, emailET, passET, usernameET);
    }


    private void uploadPhoto1() {
        Map<String, String> map = new HashMap<>();
        map.put("id", Utils.userID);
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = null;
        if (file != null && file.exists()) {
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        }
        EndPointUrl uploadImage = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<UploadObject> fileUpload = uploadImage.update_profile_photo(fileToUpload, map);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                Toast.makeText(getContext(), "Profile Picture Update Failed", Toast.LENGTH_LONG).show();
            }
        });
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

    private class uploadPhoto extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Map<String, String> map = new HashMap<>();
            map.put("id", Utils.userID);
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = null;
            if (file != null && file.exists()) {
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            }
            EndPointUrl uploadImage = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<UploadObject> fileUpload = uploadImage.update_profile_photo(fileToUpload, map);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Profile Picture Update Failed", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
    }

    public void updateProfile(final String name, final String phone, final String email, final String pass, final String username) {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = apiService.admin_update_profile(Utils.userID, name.trim(), phone.trim(), email.trim(), pass.trim(), username.trim());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("true")) {
//                        initialize Editor
                        SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                        editor.putString("username", username);
                        editor.putString("password", pass);
                        editor.putString("name", name);
                        editor.putString("phone", phone);
                        editor.putString("email", email);
                        editor.apply();
                        email_input.setEnabled(false);
                        username_input.setEnabled(false);
                        phone_input.setEnabled(false);
                        password_input.setEnabled(false);
                        btn_profile_logout.setVisibility(View.VISIBLE);
                        btn_profile_save.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response.body().message, Toast.LENGTH_LONG).show();
                        updateUserData();
                        Log.i("msg", "" + response.body().message);
                    } else {
                        Toast.makeText(getContext(), response.body().message, Toast.LENGTH_LONG).show();
                    }
                } else
                    Toast.makeText(getContext(), "Server not responding", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
