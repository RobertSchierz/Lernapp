package com.example.rob.lernapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rob.lernapp.R;
import com.example.rob.lernapp.restdataGet.Category;

import java.util.ArrayList;

public class CategorylistRecyclerviewAdapter extends RecyclerView.Adapter<CategorylistRecyclerviewAdapter.CategoryViewHolder> {

    public ArrayList<Category> categories;

    public CategorylistRecyclerviewAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void setCategoryNew(ArrayList<Category> newCategory) {
        this.categories = newCategory;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categorylist_recyclerviewitem, viewGroup, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategorylistRecyclerviewAdapter.CategoryViewHolder categoryViewHolder, int i) {

        categoryViewHolder.categoryname.setText(this.categories.get(i).getName());
        categoryViewHolder.categorycreator.setText(this.categories.get(i).getCreator().getName());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        CardView categorycardview;
        TextView categoryname;
        TextView categorycreator;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categorycardview = (CardView) itemView.findViewById(R.id.categorylist_cardView);
            categoryname = (TextView) itemView.findViewById(R.id.categorylist_name);
            categorycreator = (TextView) itemView.findViewById(R.id.categorylist_creator);

        }
    }

}