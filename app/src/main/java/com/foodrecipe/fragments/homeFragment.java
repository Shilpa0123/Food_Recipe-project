package com.foodrecipe.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodrecipe.EndPointUrl;
import com.foodrecipe.R;
import com.foodrecipe.RetrofitInstance;
import com.foodrecipe.Utils;
import com.foodrecipe.adapter.MyRecipesAdapter;
import com.foodrecipe.adapter.categoryAdapter;
import com.foodrecipe.adapter.searchAdapter;
import com.foodrecipe.model.categoryModel;
import com.foodrecipe.model.myRecipesModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homeFragment extends Fragment {
    private RecyclerView rv_category, rv_main, rv_search;
    private categoryAdapter mAdapter;
    private com.foodrecipe.adapter.searchAdapter searchAdapter;
    private MyRecipesAdapter recipesAdapter;
    private ShimmerFrameLayout shimmer_rv_category, shimmer_rv_main;
    private SearchView searchView;
    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        rv_category = view.findViewById(R.id.rv_category);
        rv_main = view.findViewById(R.id.rv_main);
        rv_search = view.findViewById(R.id.rv_search);
        searchView = view.findViewById(R.id.searchView);
        shimmer_rv_main = view.findViewById(R.id.shimmer_rv_main);
        shimmer_rv_category = view.findViewById(R.id.shimmer_rv_category);
        refresh = view.findViewById(R.id.refresh);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new search().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("") || s.equals(" ") || s.isEmpty()) {
                    rv_search.setVisibility(View.GONE);
                    searchAdapter = null;
                } else {
                    new search().execute();
                }
                return false;
            }
        });
        refresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new getCategories().execute();
                        new getallRecipes().execute();
                        refresh.setRefreshing(false);
                    }
                }
        );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager searchLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        rv_category.setLayoutManager(manager);
        rv_search.setLayoutManager(searchLayoutManager);
        rv_main.setLayoutManager(layoutManager);
        new getCategories().execute();
        new getallRecipes().execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_rv_main.startShimmerAnimation();
        shimmer_rv_category.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        shimmer_rv_main.stopShimmerAnimation();
        shimmer_rv_category.stopShimmerAnimation();
        super.onPause();
    }

    private class getCategories extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            EndPointUrl data = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<List<categoryModel>> callData = data.getRecipieCategory();
            callData.clone().enqueue(new Callback<List<categoryModel>>() {
                @Override
                public void onResponse(Call<List<categoryModel>> call, Response<List<categoryModel>> responseData) {
                    if (responseData != null) {
                        mAdapter = new categoryAdapter(responseData.body(), getContext());
                        rv_category.setAdapter(mAdapter);
                    }
                    shimmer_rv_main.stopShimmerAnimation();
                    shimmer_rv_category.stopShimmerAnimation();
                    shimmer_rv_main.setVisibility(View.GONE);
                    shimmer_rv_category.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<categoryModel>> call, Throwable t) {
                    Log.e("Error", t + "");
                }
            });
            return null;
        }
    }

    private class search extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            EndPointUrl data = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<List<myRecipesModel>> callData = data.search(String.valueOf(searchView.getQuery()));
            callData.clone().enqueue(new Callback<List<myRecipesModel>>() {
                @Override
                public void onResponse(Call<List<myRecipesModel>> call, Response<List<myRecipesModel>> responseData) {
                    if (responseData.body() != null) {
                        searchAdapter = new searchAdapter(responseData.body(), getContext());
                        rv_search.setAdapter(searchAdapter);
                        rv_search.setVisibility(View.VISIBLE);
                    } else {
                        searchAdapter = null;
                        rv_search.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<myRecipesModel>> call, Throwable t) {
                    Log.e("Error", t + "");
                }
            });
            return null;
        }
    }

    private class getallRecipes extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            EndPointUrl data = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
            Call<List<myRecipesModel>> callData = data.myrecipes(Utils.username);
            callData.clone().enqueue(new Callback<List<myRecipesModel>>() {
                @Override
                public void onResponse(Call<List<myRecipesModel>> call, Response<List<myRecipesModel>> responseData) {
                    if (responseData != null) {
                        recipesAdapter = new MyRecipesAdapter(responseData.body(), getContext());
                        rv_main.setAdapter(recipesAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<myRecipesModel>> call, Throwable t) {
                    Log.e("Error", t + "");
                }
            });
            return null;
        }
    }


}