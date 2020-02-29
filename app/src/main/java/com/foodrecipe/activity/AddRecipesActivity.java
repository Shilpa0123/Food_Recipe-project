package com.foodrecipe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.model.UploadObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodrecipe.Utils.getPathFromURI;
import static com.foodrecipe.Utils.getPickImageChooserIntent;

public class AddRecipesActivity extends AppCompatActivity {
    AppCompatEditText et_recipe_name, et_ingredients, et_procedure;
    AppCompatTextView btn_reg;
    AppCompatImageView back;
    SharedPreferences sharedPreferences;
    AppCompatImageView select_image;
    Spinner spinner_country;
    File file;
    public static Uri outputFileUri = null;
    Bundle extras;

    private static final String TAG = AddRecipesActivity.class.getSimpleName();
    public static final int INPUT_FILE_REQUEST_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrecipes);
        // Toast.makeText(getApplicationContext(), "" + uname, Toast.LENGTH_LONG).show();
        inititialize();

        extras = getIntent().getExtras();
        if (extras != null) {
            back.setBackground(getDrawable(R.drawable.back_white));
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + extras.getString("photo")).placeholder(R.drawable.plus_new).into(select_image);
            et_recipe_name.setText(extras.getString("recipename"));
            et_ingredients.setText(extras.getString("ingredients"));
            et_procedure.setText(extras.getString("procedure"));
        }
    }

    private void inititialize() {
        back = findViewById(R.id.back);
        select_image = findViewById(R.id.select_image);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddRecipesActivity.this)
                        .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            startActivityForResult(getPickImageChooserIntent(getPackageManager(), outputFileUri), INPUT_FILE_REQUEST_CODE);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(AddRecipesActivity.this, "Please Enable Permissions", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
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

        btn_reg = findViewById(R.id.btn_submit);

        et_recipe_name = findViewById(R.id.et_recipe_name);
        et_ingredients = findViewById(R.id.et_ingredients);
        et_procedure = findViewById(R.id.et_procedure);
        spinner_country = (Spinner) findViewById(R.id.spinner_country);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras != null) {
                    try {
                        if (new updateRecipe().execute().get())
                            finish();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (new uploadRecipe().execute().get())
                            finish();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


//    private String getImageFromFilePath(Intent data) {
//        boolean isCamera = data == null || data.getData() == null;
//        if (isCamera) return getCaptureImageOutputUri().getPath();
//        else return getPathFromURI(data.getData());
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INPUT_FILE_REQUEST_CODE) {
            String filePath;
            if (data == null || data.getData() == null) {
                filePath = outputFileUri.getPath();
            } else {
                filePath = getPathFromURI(data.getData(), this);
            }
            file = new File(filePath);
            if (filePath != null) {
                Log.e(TAG, "onActivityResult: " + filePath);
                Picasso.get().load(file).placeholder(R.drawable.plus_new).into(select_image);
                back.setBackground(getDrawable(R.drawable.back_white));
            }
        }
    }

    private class uploadRecipe extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Map<String, String> map = new HashMap<>();
            map.put("recipe_name", et_recipe_name.getText().toString());
            map.put("ingredients", et_ingredients.getText().toString());
            map.put("recipe_procedure", et_procedure.getText().toString());
            map.put("country_name", spinner_country.getSelectedItem().toString());
            map.put("created_by", Utils.username);
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = null;
            if (file != null && file.exists()) {
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddRecipesActivity.this, "Please Add A Picture", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            if (spinner_country.getSelectedItemPosition() == 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddRecipesActivity.this, "Please Choose A Country", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            EndPointUrl uploadImage = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<UploadObject> fileUpload = uploadImage.add_recipe(fileToUpload, map);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(AddRecipesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Toast.makeText(AddRecipesActivity.this, "Recipe Upload Failed", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
    }

    private class updateRecipe extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            Map<String, String> map = new HashMap<>();
            map.put("id", extras.getString("id"));
            map.put("recipe_name", et_recipe_name.getText().toString());
            map.put("ingredients", et_ingredients.getText().toString());
            map.put("recipe_procedure", et_procedure.getText().toString());
            map.put("country_name", spinner_country.getSelectedItem().toString());
            map.put("created_by", Utils.username);
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = null;
            if (file != null && file.exists()) {
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            } else {
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), "");
                fileToUpload = MultipartBody.Part.createFormData("file", extras.getString("photo"), mFile);
            }
            if (spinner_country.getSelectedItemPosition() == 0) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(AddRecipesActivity.this, "Please Choose A Country", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            EndPointUrl uploadImage = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<UploadObject> fileUpload = uploadImage.updateRecipe(fileToUpload, map);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(AddRecipesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Toast.makeText(AddRecipesActivity.this, "Recipe Upload Failed", Toast.LENGTH_LONG).show();
                }
            });
            return true;
        }
    }

//    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
//        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            return contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            return cursor.getString(idx);
//        }
//    }

    @Override
    //add this method in your program
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
