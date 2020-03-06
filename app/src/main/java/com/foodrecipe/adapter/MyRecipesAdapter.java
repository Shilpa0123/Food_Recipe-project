package com.foodrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.R;
import com.foodrecipe.Utils;
import com.foodrecipe.activity.AddRecipesActivity;
import com.foodrecipe.activity.LoginActivity;
import com.foodrecipe.activity.recipeDetail;
import com.foodrecipe.model.myRecipesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.MyViewHolder> {

    public static List<myRecipesModel> myrecipyList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView text;
        private AppCompatImageView image;
        CardView cv_recyclerviewMain;


        MyViewHolder(View view) {
            super(view);
            cv_recyclerviewMain = view.findViewById(R.id.cv_recyclerviewMain);
            text = view.findViewById(R.id.tv_mainText);
            image = view.findViewById(R.id.iv_mainImage);
        }
    }


    public MyRecipesAdapter(List<myRecipesModel> myrecipyList, Context context) {
        this.myrecipyList = myrecipyList;
        this.context = context;
        if (this.myrecipyList != null) {
            myRecipesModel myRecipesModel1 = new myRecipesModel();
            myRecipesModel1.setIngredients(null);
            myRecipesModel1.setImgUrl(null);
            myRecipesModel1.setId(null);
            myRecipesModel1.setRecipeName(null);
            myRecipesModel1.setRecipeProcedure(null);
            this.myrecipyList.add(myRecipesModel1);
            new getUserRating().execute();
        } else {
            this.myrecipyList = new ArrayList<myRecipesModel>();
            myRecipesModel myRecipesModel1 = new myRecipesModel();
            myRecipesModel1.setIngredients(null);
            myRecipesModel1.setImgUrl(null);
            myRecipesModel1.setId(null);
            myRecipesModel1.setRecipeName(null);
            myRecipesModel1.setRecipeProcedure(null);
            this.myrecipyList.add(myRecipesModel1);
            new getUserRating().execute();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.text.setText(myrecipyList.get(position).getRecipeName());

        if (position == myrecipyList.size() - 1)
            Picasso.get().load(R.drawable.plus_new).into(holder.image);
        else
            Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/" + myrecipyList.get(position).getImgUrl()).placeholder(R.drawable.placeholder).into(holder.image);

        holder.cv_recyclerviewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == myrecipyList.size() - 1) {
                    if (Utils.guest) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, AddRecipesActivity.class);
                        context.startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(context, recipeDetail.class);
                    intent.putExtra("id", myrecipyList.get(position).getId());
                    intent.putExtra("photo", myrecipyList.get(position).getImgUrl());
                    intent.putExtra("mainText", myrecipyList.get(position).getRecipeName());
                    intent.putExtra("procedure_text", myrecipyList.get(position).getRecipeProcedure());
                    intent.putExtra("ingredient_text", myrecipyList.get(position).getIngredients());
                    intent.putExtra("ratingBar", String.valueOf(myrecipyList.get(position).getRating()));
                    intent.putExtra("editable", true);
                    context.startActivity(intent);
//                    ((AppCompatActivity) context).overridePendingTransition(R.anim.slidein_up, R.anim.slideout_up);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Utils.user_recipes = myrecipyList.size() - 1;
        return myrecipyList.size();
    }

    public class getUserRating extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i < myrecipyList.size(); i++) {
                Utils.user_rating = Utils.user_rating + myrecipyList.get(i).getRating();
            }
            Utils.user_rating /= myrecipyList.size() - 1;
            return null;
        }
    }
}