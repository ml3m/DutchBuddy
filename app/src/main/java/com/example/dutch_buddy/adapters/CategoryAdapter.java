package com.example.dutch_buddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.FlashcardsActivity;
import com.example.dutch_buddy.QuizActivity;
import com.example.dutch_buddy.R;
import com.example.dutch_buddy.data.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        
        holder.categoryIcon.setImageResource(category.getIconResId());
        holder.categoryName.setText(category.getName());
        
        // Set click listeners for buttons
        holder.flashcardButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardsActivity.class);
            intent.putExtra("category", category.getName());
            context.startActivity(intent);
        });
        
        holder.quizButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("category", category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;
        Button flashcardButton;
        Button quizButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryName = itemView.findViewById(R.id.categoryName);
            flashcardButton = itemView.findViewById(R.id.flashcardButton);
            quizButton = itemView.findViewById(R.id.quizButton);
        }
    }
} 