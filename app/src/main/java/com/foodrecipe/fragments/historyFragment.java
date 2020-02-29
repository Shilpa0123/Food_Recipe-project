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

public class historyFragment extends Fragment {
    private RecyclerView rv_history;
    private SharedPreferences sharedPreferences;
    private List<myRecipesModel> mList = new ArrayList<>();
    AppCompatTextView placeholderText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        rv_history = view.findViewById(R.id.rv_history);
        placeholderText = view.findViewById(R.id.placeholderText);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_history.setLayoutManager(layoutManager);


        try {
            sharedPreferences = Utils.SharedPref(getContext());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipeHistory", null);
        Type type = new TypeToken<ArrayList<myRecipesModel>>() {
        }.getType();
        mList = gson.fromJson(json, type);
        if (mList != null) {
            historyAdapter mAdapter = new historyAdapter(mList);
            rv_history.setAdapter(mAdapter);
        } else {
            rv_history.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipeHistory", null);
        Type type = new TypeToken<ArrayList<myRecipesModel>>() {
        }.getType();
        mList = gson.fromJson(json, type);
        if (mList != null) {
            historyAdapter mAdapter = new historyAdapter(mList);
            rv_history.setAdapter(mAdapter);
        } else {
            rv_history.setVisibility(View.GONE);
            placeholderText.setVisibility(View.VISIBLE);
        }
    }

    public class historyAdapter extends RecyclerView.Adapter<historyAdapter.MyViewHolder> {

        private List<myRecipesModel> historyList;

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


        public historyAdapter(List<myRecipesModel> myrecipyList) {
            this.historyList = myrecipyList;
        }

        @Override
        public historyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_history, parent, false);

            return new historyAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final historyAdapter.MyViewHolder holder, final int position) {
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + historyList.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.iv_mainImage);
            holder.tv_mainText.setText(historyList.get(position).getRecipeName());
            holder.ratingBar.setRating(Math.round(historyList.get(position).getRating()));
            holder.tv_ingredient_text.setText(historyList.get(position).getIngredients());
            holder.cv_recyclerviewMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), recipeDetail.class);
                    intent.putExtra("id", historyList.get(position).getImgUrl());
                    intent.putExtra("photo", historyList.get(position).getImgUrl());
                    intent.putExtra("mainText", historyList.get(position).getRecipeName());
                    intent.putExtra("procedure_text", historyList.get(position).getRecipeProcedure());
                    intent.putExtra("ingredient_text", historyList.get(position).getIngredients());
                    intent.putExtra("ratingBar", String.valueOf(historyList.get(position).getRating()));
                    intent.putExtra("editable", false);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return historyList.size();
        }
    }
}