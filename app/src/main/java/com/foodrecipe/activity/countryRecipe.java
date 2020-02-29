package com.foodrecipe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.model.myRecipesModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.foodrecipe.Utils.setList;

public class countryRecipe extends AppCompatActivity {
    AppCompatTextView placeholderText, continentName;
    AppCompatImageView back;
    RecyclerView rv_countryRecipes;
    countryrecipeAdapter mAdapter;
    //    private ShimmerFrameLayout shimmer_rv_countryRecipe;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

//    private static final int SWIPE_MIN_DISTANCE = 120;
//    private static final int SWIPE_MAX_OFF_PATH = 300;
//    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
//    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_recipe_layout);

        back = findViewById(R.id.back);
        placeholderText = findViewById(R.id.placeholderText);
        continentName = findViewById(R.id.continentName);
        rv_countryRecipes = findViewById(R.id.rv_countryRecipes);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        shimmer_rv_countryRecipe = findViewById(R.id.shimmer_rv_countryRecipe);

//        SnapHelper snapHelper = new SnapHelperOneByOne();
//        snapHelper.attachToRecyclerView(rv_countryRecipes);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_countryRecipes.setLayoutManager(layoutManager);
//        gestureDetector = new GestureDetector(new SwipeDetector());

        //save custom list to sharedpref
        try {
            sharedPreferences = Utils.SharedPref(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor = Utils.editor(sharedPreferences);
        continentName.setText(getIntent().getStringExtra("countryRecipe"));
        new serverData().execute(getIntent().getStringExtra("countryRecipe"));
    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        shimmer_rv_countryRecipe.startShimmerAnimation();
//
//    }
//
//    @Override
//    public void onPause() {
////        shimmer_rv_countryRecipe.stopShimmerAnimation();
//        super.onPause();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    private class SwipeDetector extends GestureDetector.SimpleOnGestureListener {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
//            // then dismiss the swipe.
//            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
//                finish();
////                overridePendingTransition(R.anim.slidein_down, R.anim.slideout_down);
//
//                return true;
//            }
//
//            // Swipe from left to right.
//            // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
//            // and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
////            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
////                finish();
////                return true;
////            }
//
//            return false;
//        }
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // TouchEvent dispatcher.
//        if (gestureDetector != null) {
//            if (gestureDetector.onTouchEvent(ev))
//                // If the gestureDetector handles the event, a swipe has been
//                // executed and no more needs to be done.
//                return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }
    private class serverData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... countryName) {
            EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<List<myRecipesModel>> call = service.getAllRecipes(countryName[0]);
            call.enqueue(new Callback<List<myRecipesModel>>() {
                @Override
                public void onResponse(Call<List<myRecipesModel>> call, Response<List<myRecipesModel>> response) {
                    if (response.body() != null) {
//                        shimmer_rv_countryRecipe.stopShimmerAnimation();
//                        shimmer_rv_countryRecipe.setVisibility(View.GONE);
                        mAdapter = new countryrecipeAdapter(response.body());
                        rv_countryRecipes.setAdapter(mAdapter);
                    } else {
//                        shimmer_rv_countryRecipe.stopShimmerAnimation();
//                        shimmer_rv_countryRecipe.setVisibility(View.GONE);
                        placeholderText.setVisibility(View.VISIBLE);
                        rv_countryRecipes.setVisibility(View.GONE);
//                    list_view.setAdapter(new ViewRecipesAdapter(response.body(), context));
                    }
                }

                @Override
                public void onFailure(Call<List<myRecipesModel>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
    }

//    public class SnapHelperOneByOne extends LinearSnapHelper {
//
//        @Override
//        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
//
//            if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
//                return RecyclerView.NO_POSITION;
//            }
//
//            final View currentView = findSnapView(layoutManager);
//
//            if (currentView == null) {
//                return RecyclerView.NO_POSITION;
//            }
//
//            final int currentPosition = layoutManager.getPosition(currentView);
//
//            if (currentPosition == RecyclerView.NO_POSITION) {
//                return RecyclerView.NO_POSITION;
//            }
//
//            return currentPosition;
//        }
//    }

    class countryrecipeAdapter extends RecyclerView.Adapter<countryrecipeAdapter.MyViewHolder> {
        List<myRecipesModel> countryRecipes;

        countryrecipeAdapter(List<myRecipesModel> countryRecipes) {
            this.countryRecipes = countryRecipes;
        }

        @Override
        public countryrecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipedetail_rv, parent, false);

            return new countryrecipeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final countryrecipeAdapter.MyViewHolder holder, final int position) {
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + countryRecipes.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.iv_mainImage);
            holder.tv_mainText.setText(countryRecipes.get(position).getRecipeName());
            holder.ratingBar.setRating(Math.round(countryRecipes.get(position).getRating()));
            holder.tv_ingredient_text.setText(countryRecipes.get(position).getIngredients());
            holder.cv_recyclerviewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(countryRecipe.this, recipeDetail.class);
                    intent.putExtra("id", countryRecipes.get(position).getId());
                    intent.putExtra("photo", countryRecipes.get(position).getImgUrl());
                    intent.putExtra("mainText", countryRecipes.get(position).getRecipeName());
                    intent.putExtra("procedure_text", countryRecipes.get(position).getRecipeProcedure());
                    intent.putExtra("ingredient_text", countryRecipes.get(position).getIngredients());
                    intent.putExtra("ratingBar", String.valueOf(countryRecipes.get(position).getRating()));
                    intent.putExtra("editable", false);
                    startActivity(intent);
                    if (Utils.guest == false) {
                        myRecipesModel mrecipeHistory = new myRecipesModel();
                        mrecipeHistory.setId(countryRecipes.get(position).getId());
                        mrecipeHistory.setImgUrl(countryRecipes.get(position).getImgUrl());
                        mrecipeHistory.setIngredients(countryRecipes.get(position).getIngredients());
                        mrecipeHistory.setRating(countryRecipes.get(position).getRating());
                        mrecipeHistory.setRecipeName(countryRecipes.get(position).getRecipeName());
                        mrecipeHistory.setRecipeProcedure(countryRecipes.get(position).getRecipeProcedure());
                        Utils.history.add(mrecipeHistory);
                        setList("recipeHistory", Utils.history, editor);
                    }
                }
            });


        }


        @Override
        public int getItemCount() {
            return countryRecipes.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            AppCompatImageView iv_mainImage;
            AppCompatTextView tv_mainText, tv_ingredient_text;
            CardView cv_recyclerviewMain;
            RatingBar ratingBar;

            MyViewHolder(View view) {
                super(view);
                cv_recyclerviewMain = view.findViewById(R.id.cv_recyclerviewMain);
                iv_mainImage = view.findViewById(R.id.iv_mainImage);
                tv_mainText = view.findViewById(R.id.tv_mainText);
                tv_ingredient_text = view.findViewById(R.id.tv_ingredient_text);
                ratingBar = view.findViewById(R.id.ratingBar);
            }
        }
    }

//    class countryrecipeAdapter extends RecyclerView.Adapter<countryrecipeAdapter.MyViewHolder> {
//        List<myRecipesModel> countryRecipes;
//        int previousPosition;
//
//        countryrecipeAdapter(List<myRecipesModel> countryRecipes) {
//            this.countryRecipes = countryRecipes;
//        }
//
//        @Override
//        public countryrecipeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.countryrecipe_rv, parent, false);
//
//            return new countryrecipeAdapter.MyViewHolder(itemView);
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public void onBindViewHolder(final countryrecipeAdapter.MyViewHolder holder, final int position) {
//            holder.setIsRecyclable(false);
//
//            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + countryRecipes.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.iv_mainImage);
//            holder.tv_mainText.setText(countryRecipes.get(position).getRecipeName());
//            holder.tv_procedure_text.setText(countryRecipes.get(position).getRecipeProcedure());
//            holder.tv_ingredient_text.setText(countryRecipes.get(position).getIngredients());
//            holder.ratingBar.setRating(Math.round(countryRecipes.get(position).getRating()));
//
//            holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//
//                @Override
//                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                    final float rating = holder.ratingBar.getRating();
//                    Log.e("rating", "onTouch: " + rating + "\n" + holder.ratingBar.getRating());
//                    EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
//                    Call<ResponseData> call = service.update_rating(String.valueOf(rating), Utils.username, countryRecipes.get(position).getRecipeName());
//                    call.enqueue(new Callback<ResponseData>() {
//                        @Override
//                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//
//                            if (response.body().status.equals("true")) {
//                                countryRecipes.get(position).setRating(rating);
//                                Toast.makeText(countryRecipe.this, response.body().message, Toast.LENGTH_LONG).show();
//                                //startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
//
//                            } else {
//                                Toast.makeText(countryRecipe.this, response.body().message, Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseData> call, Throwable t) {
//                            Toast.makeText(countryRecipe.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            });
//            if (previousPosition != position) {
//                recipeHistory mrecipeHistory = new recipeHistory();
//
//                mrecipeHistory.setId(countryRecipes.get(position).getId());
//                mrecipeHistory.setImgUrl(countryRecipes.get(position).getImgUrl());
//                mrecipeHistory.setIngredients(countryRecipes.get(position).getIngredients());
//                mrecipeHistory.setRating(countryRecipes.get(position).getRating());
//                mrecipeHistory.setRecipeName(countryRecipes.get(position).getRecipeName());
//                mrecipeHistory.setRecipeProcedure(countryRecipes.get(position).getRecipeProcedure());
//                mList.add(mrecipeHistory);
//
//
//                String json = gson.toJson(mList);
//                setList("recipeHistory", mList);
//                previousPosition = position;
//            }
//        }
//
//        public <T> void setList(String key, List<T> list) {
//            Gson gson = new Gson();
//            String json = gson.toJson(list);
//
//            set(key, json);
//        }
//
//        public void set(String key, String value) {
//            editor.putString(key, value);
//            editor.apply();
//        }
//
//        @Override
//        public int getItemCount() {
//            return countryRecipes.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            AppCompatImageView iv_mainImage;
//            AppCompatTextView tv_mainText, tv_procedure_text, tv_ingredient_text;
//            RatingBar ratingBar;
//
//            MyViewHolder(View view) {
//                super(view);
//                iv_mainImage = view.findViewById(R.id.iv_mainImage);
//                tv_mainText = view.findViewById(R.id.tv_mainText);
//                tv_procedure_text = view.findViewById(R.id.tv_procedure_text);
//                tv_ingredient_text = view.findViewById(R.id.tv_ingredient_text);
//                ratingBar = view.findViewById(R.id.ratingBar);
//            }
//        }
//    }
}

