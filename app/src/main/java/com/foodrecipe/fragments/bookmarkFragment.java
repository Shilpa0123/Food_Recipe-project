package com.foodrecipe.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.R;
import com.foodrecipe.Utils;
import com.foodrecipe.activity.recipeDetail;
import com.foodrecipe.model.myRecipesModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class bookmarkFragment extends Fragment {
    private RecyclerView rv_saved;
    private SharedPreferences sharedPreferences;
    public static List<myRecipesModel> mList = new ArrayList<>();
    AppCompatTextView placeholderText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_fragment, container, false);

        rv_saved = view.findViewById(R.id.rv_saved);
        placeholderText = view.findViewById(R.id.placeholderText);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_saved.setLayoutManager(layoutManager);

        try {
            sharedPreferences = Utils.SharedPref(getContext());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved", null);
        Type type = new TypeToken<ArrayList<myRecipesModel>>() {
        }.getType();
        mList = gson.fromJson(json, type);
        if (mList != null) {
            bookmarkFragment.bookmarkAdapter mAdapter = new bookmarkFragment.bookmarkAdapter(mList);
            rv_saved.setAdapter(mAdapter);
        } else {
            rv_saved.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saved", null);
        Type type = new TypeToken<ArrayList<myRecipesModel>>() {
        }.getType();
        mList = gson.fromJson(json, type);
        if (mList != null && mList.size()!=0) {
            bookmarkFragment.bookmarkAdapter mAdapter = new bookmarkFragment.bookmarkAdapter(mList);
            rv_saved.setAdapter(mAdapter);
            rv_saved.setVisibility(View.VISIBLE);
            placeholderText.setVisibility(View.GONE);
        } else {
            rv_saved.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
        }
    }

    public class bookmarkAdapter extends RecyclerView.Adapter<bookmarkAdapter.MyViewHolder> {

        private List<myRecipesModel> bookmarkList;

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


        public bookmarkAdapter(List<myRecipesModel> myrecipyList) {
            this.bookmarkList = myrecipyList;
        }

        @Override
        public bookmarkAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_history, parent, false);

            return new bookmarkAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final bookmarkAdapter.MyViewHolder holder, final int position) {
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + bookmarkList.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.iv_mainImage);
            holder.tv_mainText.setText(bookmarkList.get(position).getRecipeName());
            holder.ratingBar.setRating(Math.round(bookmarkList.get(position).getRating()));
            holder.tv_ingredient_text.setText(bookmarkList.get(position).getIngredients());
            holder.cv_recyclerviewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), recipeDetail.class);
                    intent.putExtra("id", bookmarkList.get(position).getId());
                    intent.putExtra("photo", bookmarkList.get(position).getImgUrl());
                    intent.putExtra("author", bookmarkList.get(position).getCreated_by());
                    intent.putExtra("mainText", bookmarkList.get(position).getRecipeName());
                    intent.putExtra("procedure_text", bookmarkList.get(position).getRecipeProcedure());
                    intent.putExtra("ingredient_text", bookmarkList.get(position).getIngredients());
                    intent.putExtra("ratingBar", String.valueOf(bookmarkList.get(position).getRating()));
                    intent.putExtra("editable", false);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return bookmarkList.size();
        }
    }
}