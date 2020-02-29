package com.foodrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.R;
import com.foodrecipe.activity.recipeDetail;
import com.foodrecipe.model.myRecipesModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.MyViewHolder> {
    public static List<myRecipesModel> searchRecipes;
    Context context;

    public searchAdapter(List<myRecipesModel> searchRecipes, Context context) {
        this.searchRecipes = searchRecipes;
        this.context = context;
    }

    @Override
    public searchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_search, parent, false);
        return new searchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final searchAdapter.MyViewHolder holder, final int position) {
        holder.tv_search.setText(searchRecipes.get(position).getRecipeName());
        holder.tv_ingredient.setText(searchRecipes.get(position).getIngredients());
        Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" +searchRecipes.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.iv_imagesearch);
        holder.rl_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, recipeDetail.class);
                intent.putExtra("id", searchRecipes.get(position).getId());
                intent.putExtra("photo",searchRecipes.get(position).getImgUrl());
                intent.putExtra("mainText", searchRecipes.get(position).getRecipeName());
                intent.putExtra("procedure_text", searchRecipes.get(position).getRecipeProcedure());
                intent.putExtra("ingredient_text", searchRecipes.get(position).getIngredients());
                intent.putExtra("ratingBar", String.valueOf(searchRecipes.get(position).getRating()));
                intent.putExtra("editable", false);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return searchRecipes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_search,tv_ingredient;
        AppCompatImageView iv_imagesearch;
        RelativeLayout rl_search;

        MyViewHolder(View view) {
            super(view);
            tv_search = view.findViewById(R.id.tv_search);
            tv_ingredient = view.findViewById(R.id.tv_ingredient);
            rl_search = view.findViewById(R.id.rl_search);
            iv_imagesearch = view.findViewById(R.id.iv_imageSearch);
        }
    }
}