package com.foodrecipe.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.foodrecipe.model.ResponseData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profileFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    AppCompatImageView iv_logo;
    private TextInputLayout input_profilename, input_username, input_phonenumber, input_email, input_password;
    private AppCompatEditText et_profilename, et_USERNAME, et_phonenumber, et_email, et_password;
    private String uname, pass, email, phone, name,photo;
    private MaterialButton btn_profile_edit, btn_profile_save, btn_signup;
    private AppCompatTextView btn_profile_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        initializeSharedPref();
        iv_logo = view.findViewById(R.id.iv_logo);
        input_email = view.findViewById(R.id.input_email);
        input_profilename = view.findViewById(R.id.input_profilename);
        input_username = view.findViewById(R.id.input_username);
        input_phonenumber = view.findViewById(R.id.input_phonenumber);
        input_password = view.findViewById(R.id.input_password);

        et_profilename = view.findViewById(R.id.et_profilename);
        et_USERNAME = view.findViewById(R.id.et_USERNAME);
        et_phonenumber = view.findViewById(R.id.et_phonenumber);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);

        btn_signup = view.findViewById(R.id.btn_signup);
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

        btn_profile_edit = view.findViewById(R.id.btn_profile_edit);
        btn_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_email.setEnabled(true);
                input_profilename.setEnabled(true);
                input_username.setEnabled(true);
                input_phonenumber.setEnabled(true);
                input_password.setEnabled(true);
                btn_profile_edit.setVisibility(View.GONE);
                btn_profile_save.setVisibility(View.VISIBLE);
            }
        });

        btn_profile_save = view.findViewById(R.id.btn_profile_save);
        btn_profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData(et_profilename.getText().toString(),
                        et_phonenumber.getText().toString(),
                        et_email.getText().toString(),
                        et_password.getText().toString(),
                        et_USERNAME.getText().toString());
            }
        });

        input_email.setEnabled(false);
        input_profilename.setEnabled(false);
        input_username.setEnabled(false);
        input_phonenumber.setEnabled(false);
        input_password.setEnabled(false);

        if (btn_signup.getVisibility() == View.VISIBLE)
            btn_signup.setVisibility(View.VISIBLE);

        setFields();

        return view;
    }

    private void setFields() {
        if (uname != null)
            et_USERNAME.setText(uname);
        if (pass != null)
            et_password.setText(pass);
        if (name != null)
            et_profilename.setText(name);
        if (phone != null)
            et_phonenumber.setText(phone);
        if (email != null)
            et_email.setText(email);
        if (photo != null&&!photo.equals(""))
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/profile"+photo).into(iv_logo);
        else
            Picasso.get().load(R.drawable.recipe_logo_selected).into(iv_logo);
    }

    private void validateData(final String nameET, final String phoneET, final String emailET, final String passET, final String usernameET) {
        if (nameET.trim().equals("") || nameET.trim().equals(" ")) {
            if (name != null) {
                validateData(name, phoneET, emailET, passET, usernameET);
            } else
                Toast.makeText(getContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
        } else if (phoneET.trim().equals("") || phoneET.trim().equals(" ")) {
            if (phone != null) {
                validateData(nameET, phone, emailET, passET, usernameET);
            } else
                Toast.makeText(getContext(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        } else if (emailET.trim().equals("") || emailET.trim().equals(" ")) {
            if (email != null) {
                validateData(nameET, phoneET, email, passET, usernameET);
            } else
                Toast.makeText(getContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (passET.trim().equals("") || passET.trim().equals(" ")) {
            if (pass != null) {
                validateData(nameET, phoneET, emailET, pass, usernameET);
            } else
                Toast.makeText(getContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (usernameET.trim().equals("") || usernameET.trim().equals(" ")) {
            if (uname != null) {
                validateData(nameET, phoneET, emailET, passET, uname);
            } else
                Toast.makeText(getContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
        } else
            updateProfile(nameET, phoneET, emailET, passET, usernameET);

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
        pass = sharedPreferences.getString("password", null);
        name = sharedPreferences.getString("name", null);
        phone = sharedPreferences.getString("phone", null);
        email = sharedPreferences.getString("email", null);
    }

    public void updateProfile(final String name, final String phone, final String email, final String pass, final String username) {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = apiService.admin_update_profile(Utils.userID,name.trim(), phone.trim(), email.trim(), pass.trim(), username.trim());
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
                        input_email.setEnabled(false);
                        input_profilename.setEnabled(false);
                        input_username.setEnabled(false);
                        input_phonenumber.setEnabled(false);
                        input_password.setEnabled(false);
                        btn_profile_edit.setVisibility(View.VISIBLE);
                        btn_profile_save.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response.body().message, Toast.LENGTH_LONG).show();
                        updateUserData();
                        setFields();
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
