package com.foodrecipe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.model.ResponseData;
import com.foodrecipe.model.myRecipesModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodrecipe.Utils.setList;

public class recipeDetail extends AppCompatActivity {
    AppCompatTextView placeholderText;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AppCompatImageView iv_mainImage, back, save, saved, edit;
    AppCompatTextView tv_mainText, tv_procedure_text, tv_ingredient_text;
    RatingBar ratingBar;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countryrecipe_rv);

        placeholderText = findViewById(R.id.placeholderText);
//        shimmer_rv_countryRecipe = findViewById(R.id.shimmer_rv_countryRecipe);

        iv_mainImage = findViewById(R.id.iv_mainImage);
        back = findViewById(R.id.back);
        save = findViewById(R.id.save);
        saved = findViewById(R.id.saved);
        edit = findViewById(R.id.edit);
        tv_mainText = findViewById(R.id.tv_mainText);
        tv_procedure_text = findViewById(R.id.tv_procedure_text);
        tv_ingredient_text = findViewById(R.id.tv_ingredient_text);
        ratingBar = findViewById(R.id.ratingBar);

        if (getIntent().getBooleanExtra("editable", false)) {
            save.setVisibility(View.GONE);
            saved.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        } else
            new checkVal().execute();
        //save custom list to sharedpref
        try {
            sharedPreferences = Utils.SharedPref(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor = Utils.editor(sharedPreferences);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.GONE);
                saved.setVisibility(View.VISIBLE);
                if (Utils.guest == false) {
                    myRecipesModel mrecipeSaved = new myRecipesModel();
                    mrecipeSaved.setId(getIntent().getStringExtra("id"));
                    mrecipeSaved.setImgUrl(getIntent().getStringExtra("photo"));
                    mrecipeSaved.setIngredients(getIntent().getStringExtra("ingredient_text"));
                    mrecipeSaved.setRating(Float.parseFloat(getIntent().getStringExtra("ratingBar")));
                    mrecipeSaved.setRecipeName(getIntent().getStringExtra("mainText"));
                    mrecipeSaved.setRecipeProcedure(getIntent().getStringExtra("procedure_text"));
                    Utils.saved.add(mrecipeSaved);
                    setList("saved", Utils.saved, editor);
                }
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.VISIBLE);
                saved.setVisibility(View.GONE);
                Utils.saved.remove(position);
                setList("saved", Utils.saved, editor);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(recipeDetail.this,AddRecipesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("photo", getIntent().getStringExtra("photo"));
                bundle.putString("id", getIntent().getStringExtra("id"));
                bundle.putString("recipename", getIntent().getStringExtra("mainText"));
                bundle.putString("ingredients", getIntent().getStringExtra("ingredient_text"));
                bundle.putString("procedure", getIntent().getStringExtra("procedure_text"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + getIntent().getStringExtra("photo")).placeholder(R.drawable.placeholder).into(iv_mainImage);
        tv_mainText.setText(getIntent().getStringExtra("mainText"));
        tv_procedure_text.setText(getIntent().getStringExtra("procedure_text"));
        tv_ingredient_text.setText(getIntent().getStringExtra("ingredient_text"));
        ratingBar.setRating(Math.round(Float.parseFloat(getIntent().getStringExtra("ratingBar"))));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(final RatingBar ratingBar, float v, boolean b) {
                final float rating = ratingBar.getRating();
                Log.e("rating", "onTouch: " + rating + "\n" + ratingBar.getRating());
                EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
                Call<ResponseData> call = service.update_rating(String.valueOf(rating), Utils.username, getIntent().getStringExtra("mainText"));
                call.enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                        if (response.body().status.equals("true")) {
                            ratingBar.setRating(rating);
                            Toast.makeText(recipeDetail.this, response.body().message, Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

                        } else {
                            Toast.makeText(recipeDetail.this, response.body().message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Toast.makeText(recipeDetail.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class checkVal extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... strings) {
            String id = getIntent().getStringExtra("id");
            Log.e("save", "item id: " + getIntent().getStringExtra("id"));
            for (int i = 0; i < Utils.saved.size(); i++) {
                if (Utils.saved.get(i).getId().equals(id)) {
                    Log.e("save", "doInBackground: " + Utils.saved.get(i).getId() + "\n" + i + "\n" + Utils.saved.size());
                    flag = true;
                    position = i;
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (flag) {
                save.setVisibility(View.GONE);
                saved.setVisibility(View.VISIBLE);
            } else {
                save.setVisibility(View.VISIBLE);
                saved.setVisibility(View.GONE);
            }
        }
    }
}

