package com.foodrecipe.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.model.ResponseData;
import com.foodrecipe.model.myProfile;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatEditText et_profilename, et_USERNAME, et_phonenumber, et_email, et_password;
    AppCompatTextView tv_forgetpwd, tv_signup, tv_backlogin, tv_guestLogin;
    TextInputLayout inputPassword, inputUsername;
    AppCompatEditText et_login_username, et_PWD;
    MaterialButton btn_login, btn_submit, btn_signup;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    View loginDetails, signupDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        //view
        loginDetails = findViewById(R.id.loginDetails);
        signupDetails = findViewById(R.id.signupDetails);

        progressBar = findViewById(R.id.progressbar);
        tv_backlogin = findViewById(R.id.tv_backtologin);

        //login
        inputPassword = findViewById(R.id.input_password);
        inputUsername = findViewById(R.id.input_username);
        et_login_username = findViewById(R.id.et_login_username);
        et_PWD = findViewById(R.id.et_PWD);
        tv_guestLogin = findViewById(R.id.tv_guestLogin);

        tv_forgetpwd = findViewById(R.id.tv_forgetpwd);
        tv_signup = findViewById(R.id.tv_signup);

        //button
        btn_login = findViewById(R.id.btn_login);
        btn_submit = findViewById(R.id.btn_submit);
        btn_signup = findViewById(R.id.btn_signup);

        //signup
        et_profilename = findViewById(R.id.et_profilename);
        et_USERNAME = findViewById(R.id.et_USERNAME);
        et_phonenumber = findViewById(R.id.et_phonenumber);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);


        //click listeners
        tv_backlogin.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_forgetpwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_guestLogin.setOnClickListener(this);

        //keylisteners
        et_login_username.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_PWD, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        et_PWD.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    btn_login.performClick();
                    btn_login.setPressed(true);
                    btn_login.invalidate();
                }
                return false;
            }
        });

        et_profilename.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_USERNAME, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        et_USERNAME.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_phonenumber, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        et_phonenumber.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_email, InputMethodManager.SHOW_FORCED);
                }
                return false;
            }
        });

        et_password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    btn_signup.performClick();
                    btn_signup.setPressed(true);
                    btn_signup.invalidate();
                }
                return false;
            }
        });
        //shared pref

        try {
            sharedPreferences = Utils.SharedPref(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // use the shared preferences and editor as you normally would
        String uname = sharedPreferences.getString("username", null);
        String pass = sharedPreferences.getString("password", null);
        if (uname != null && pass != null) {
            progressBar.setVisibility(View.VISIBLE);
            tv_guestLogin.setVisibility(View.GONE);
            inputUsername.setVisibility(View.GONE);
            inputPassword.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);
            tv_signup.setVisibility(View.GONE);
            tv_forgetpwd.setVisibility(View.GONE);
            submitData(uname, pass);
        }
    }

    private class getProfile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            if (Utils.userID != null || Utils.userID != "" || Utils.userID != " ") {
                EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
                Call<List<myProfile>> call = apiService.getMyProfile(Utils.userID);
                call.enqueue(new Callback<List<myProfile>>() {
                    @Override
                    public void onResponse(Call<List<myProfile>> call, Response<List<myProfile>> response) {
                        if (response.body() != null) {
                            SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                            editor.putString("photo", response.body().get(0).getProfilePhoto());
                            editor.putString("phone", response.body().get(0).getPhone());
                            editor.putString("email", response.body().get(0).getEmailid());
                            editor.putString("name", response.body().get(0).getName());
                            editor.putString("password", response.body().get(0).getPwd());
                            editor.apply();
                        } else
                            Toast.makeText(LoginActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<myProfile>> call, Throwable t) {
                        Log.d("TAG", "Response = " + t.toString());
                    }
                });
            }
            return null;
        }
    }

    private void submitData(final String username, final String password) {
        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = service.user_login(username.trim(), password.trim());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("true")) {
                        SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.apply();
                        Utils.username = username;
                        Utils.userID = response.body().id;
                        new getProfile().execute();
                        Intent intent = new Intent(LoginActivity.this, baseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        inputUsername.setVisibility(View.VISIBLE);
                        inputPassword.setVisibility(View.VISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        tv_signup.setVisibility(View.VISIBLE);
                        tv_forgetpwd.setVisibility(View.VISIBLE);
                        tv_guestLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    inputUsername.setVisibility(View.VISIBLE);
                    inputPassword.setVisibility(View.VISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    tv_signup.setVisibility(View.VISIBLE);
                    tv_forgetpwd.setVisibility(View.VISIBLE);
                    tv_guestLogin.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                inputUsername.setVisibility(View.VISIBLE);
                inputPassword.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                tv_signup.setVisibility(View.VISIBLE);
                tv_forgetpwd.setVisibility(View.VISIBLE);
                tv_guestLogin.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void forgotPassword() {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = apiService.forgotPassword(et_login_username.getText().toString());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body() != null) {
                    if (response.body().status.equals("true")) {
                        Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                        Log.i("msg", "" + response.body().message);
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(LoginActivity.this, "Server not responding", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }

    private boolean validateData(final String nameET, final String phoneET, final String emailET, final String passET, final String usernameET) {
        if (nameET.trim().equals("") || nameET.trim().equals(" ")) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phoneET.trim().equals("") || phoneET.trim().equals(" ")) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailET.trim().equals("") || emailET.trim().equals(" ")) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passET.trim().equals("") || passET.trim().equals(" ")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameET.trim().equals("") || usernameET.trim().equals(" ")) {
            Toast.makeText(this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;

    }


    private void signup(final String nameET, final String phoneET, final String emailET, final String passET, final String usernameET) {

        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = service.user_registration(nameET.trim(), phoneET.trim(), emailET.trim(), usernameET.trim(), passET.trim());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.body().status.equals("true")) {
                    SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                    editor.putString("username", usernameET);
                    editor.putString("password", passET);
                    editor.putString("name", nameET);
                    editor.putString("phone", phoneET);
                    editor.putString("email", emailET);
                    editor.apply();
                    Utils.username = usernameET;
                    Utils.userID = response.body().id;
                    if (loginDetails.getVisibility() == View.GONE) {
                        loginDetails.setVisibility(View.VISIBLE);
                        signupDetails.setVisibility(View.GONE);
                        tv_backlogin.setVisibility(View.GONE);
                        tv_forgetpwd.setVisibility(View.VISIBLE);
                        btn_signup.setVisibility(View.GONE);
                        tv_signup.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();

                } else {
                    if (loginDetails.getVisibility() == View.GONE) {
                        loginDetails.setVisibility(View.VISIBLE);
                        signupDetails.setVisibility(View.GONE);
                        tv_backlogin.setVisibility(View.GONE);
                        tv_forgetpwd.setVisibility(View.VISIBLE);
                        btn_signup.setVisibility(View.GONE);
                        tv_signup.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                tv_signup.setVisibility(View.GONE);
                loginDetails.setVisibility(View.GONE);
                signupDetails.setVisibility(View.VISIBLE);
                tv_backlogin.setVisibility(View.VISIBLE);
                tv_forgetpwd.setVisibility(View.GONE);
                btn_signup.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Please Try Again Later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (validateData(
                        et_profilename.getText().toString(),
                        et_phonenumber.getText().toString(),
                        et_email.getText().toString(),
                        et_password.getText().toString(),
                        et_USERNAME.getText().toString())) {
                    signup(
                            et_profilename.getText().toString(),
                            et_phonenumber.getText().toString(),
                            et_email.getText().toString(),
                            et_password.getText().toString(),
                            et_USERNAME.getText().toString());
                }
                break;
            case R.id.tv_signup:
                tv_signup.setVisibility(View.GONE);
                loginDetails.setVisibility(View.GONE);
                signupDetails.setVisibility(View.VISIBLE);
                tv_backlogin.setVisibility(View.VISIBLE);
                tv_forgetpwd.setVisibility(View.GONE);
                tv_guestLogin.setVisibility(View.GONE);
                btn_signup.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_forgetpwd:
                if (inputPassword.getVisibility() == View.VISIBLE)
                    inputPassword.setVisibility(View.GONE);
                if (tv_guestLogin.getVisibility() == View.VISIBLE)
                    tv_guestLogin.setVisibility(View.GONE);
                if (btn_login.getVisibility() == View.VISIBLE) {
                    btn_login.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                }
                tv_forgetpwd.setVisibility(View.GONE);
                tv_backlogin.setVisibility(View.VISIBLE);
                inputUsername.setHint("Email");
                break;
            case R.id.btn_login:
                if (et_login_username.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your User Name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (et_PWD.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your Password", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                tv_guestLogin.setVisibility(View.GONE);
                inputUsername.setVisibility(View.GONE);
                inputPassword.setVisibility(View.GONE);
                btn_login.setVisibility(View.GONE);
                tv_signup.setVisibility(View.GONE);
                tv_forgetpwd.setVisibility(View.GONE);
                submitData(et_login_username.getText().toString(), et_PWD.getText().toString());
                break;
            case R.id.btn_submit:
                if (et_login_username.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter your Email", Toast.LENGTH_LONG).show();
                    return;
                }
                forgotPassword();
                break;
            case R.id.tv_backtologin:
                if (signupDetails.getVisibility() == View.VISIBLE)
                    signupDetails.setVisibility(View.GONE);
                if (loginDetails.getVisibility() == View.GONE)
                    loginDetails.setVisibility(View.VISIBLE);
                if (inputPassword.getVisibility() == View.GONE)
                    inputPassword.setVisibility(View.VISIBLE);
                if (tv_guestLogin.getVisibility() == View.GONE)
                    tv_guestLogin.setVisibility(View.VISIBLE);
                if (tv_signup.getVisibility() == View.GONE)
                    tv_signup.setVisibility(View.VISIBLE);
                if (btn_login.getVisibility() == View.GONE) {
                    btn_login.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.GONE);
                }
                tv_forgetpwd.setVisibility(View.VISIBLE);
                tv_backlogin.setVisibility(View.GONE);
                inputUsername.setHint("Username");
                break;

            case R.id.tv_guestLogin:
                Utils.guest = true;
                Utils.username = "guest";
                SharedPreferences.Editor editor = Utils.editor(sharedPreferences);
                editor.clear();
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, baseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
