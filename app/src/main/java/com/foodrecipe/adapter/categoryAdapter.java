package com.foodrecipe.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrecipe.R;
import com.foodrecipe.activity.countryRecipe;
import com.foodrecipe.model.categoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.MyViewHolder> {
    Context context;
    private List<categoryModel> categoryList;
    class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView text;
        private AppCompatImageView image;
        private CardView cv_category;

        MyViewHolder(View view) {
            super(view);
            cv_category = view.findViewById(R.id.cv_category);
            text = view.findViewById(R.id.tv_categoryText);
            image = view.findViewById(R.id.iv_categoryImage);
        }
    }


    public categoryAdapter(List<categoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.text.setText(categoryList.get(position).getCountryName());
        Picasso.get().load("http://foodrecipeapp.com/FoodRecipes/images/continental/"+categoryList.get(position).getCountryName().toLowerCase()+".jpg").placeholder(R.drawable.placeholder).into(holder.image);

        holder.cv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, countryRecipe.class);
                intent.putExtra("countryRecipe",categoryList.get(position).getCountryName());
                context.startActivity(intent);
//                ((AppCompatActivity) context).overridePendingTransition( R.anim.slidein_up, R.anim.slideout_up );
//                serverData(categoryList.get(position).getCountryName(),context);
            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}