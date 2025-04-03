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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.CompleteSentenceActivity;
import com.example.dutch_buddy.FlashcardsActivity;
import com.example.dutch_buddy.LearningPathActivity;
import com.example.dutch_buddy.QuizActivity;
import com.example.dutch_buddy.R;
import com.example.dutch_buddy.data.Category;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int userId;
    
    // Constant for intent extra key
    private static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    private static final String EXTRA_USER_ID = "USER_ID";

    public CategoryAdapter(Context context, List<Category> categoryList, int userId) {
        this.context = context;
        this.categoryList = categoryList;
        this.userId = userId;
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
        
        holder.categoryHeaderImage.setImageResource(category.getIconResId());
        holder.categoryTitle.setText(category.getName());
        
        // Make entire card clickable to open learning path
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LearningPathActivity.class);
            intent.putExtra(EXTRA_CATEGORY_NAME, category.getName());
            intent.putExtra(EXTRA_USER_ID, userId);
            context.startActivity(intent);
        });
        
        // Set click listeners for buttons
        holder.exploreButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardsActivity.class);
            intent.putExtra(EXTRA_CATEGORY_NAME, category.getName());
            intent.putExtra(EXTRA_USER_ID, userId);
            context.startActivity(intent);
        });
        
        holder.startLearningButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra(EXTRA_CATEGORY_NAME, category.getName());
            intent.putExtra(EXTRA_USER_ID, userId);
            context.startActivity(intent);
        });
        
        holder.categoryDescription.setText(category.getDescription());
        holder.unitCountText.setText(String.valueOf(category.getUnitCount()));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView categoryHeaderImage;
        TextView categoryTitle;
        Button exploreButton;
        Button startLearningButton;
        TextView categoryDescription;
        TextView unitCountText;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.categoryCard);
            categoryHeaderImage = itemView.findViewById(R.id.categoryHeaderImage);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            categoryDescription = itemView.findViewById(R.id.categoryDescription);
            exploreButton = itemView.findViewById(R.id.exploreButton);
            startLearningButton = itemView.findViewById(R.id.startLearningButton);
            unitCountText = itemView.findViewById(R.id.unitCountText);
        }
    }
} 