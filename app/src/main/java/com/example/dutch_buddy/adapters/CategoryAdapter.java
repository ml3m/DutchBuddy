package com.example.dutch_buddy.adapters;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.CompleteSentenceActivity;
import com.example.dutch_buddy.FlashcardsActivity;
import com.example.dutch_buddy.LearningPathActivity;
import com.example.dutch_buddy.QuizActivity;
import com.example.dutch_buddy.R;
import com.example.dutch_buddy.data.Category;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int userId;
    
    // Constant for intent extra key
    private static final String EXTRA_CATEGORY_NAME = "CATEGORY_NAME";
    private static final String EXTRA_USER_ID = "USER_ID";
    
    // Animation properties
    private static final int ANIMATION_DURATION = 300;
    private static final float ICON_SCALE = 1.05f;
    private static final float BUTTON_SCALE = 0.95f;

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
            animateCardClick(holder);
            
            // Start activity after brief animation delay
            holder.cardView.postDelayed(() -> {
                Intent intent = new Intent(context, LearningPathActivity.class);
                intent.putExtra(EXTRA_CATEGORY_NAME, category.getName());
                intent.putExtra(EXTRA_USER_ID, userId);
                context.startActivity(intent);
            }, ANIMATION_DURATION / 2);
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
        
        // Set descriptions
        String description = "";
        if (category.getName().equals("Greetings")) {
            description = "Learn essential Dutch greetings and introduction phrases";
        } else if (category.getName().equals("Food")) {
            description = "Discover Dutch culinary terms and how to order in restaurants";
        } else if (category.getName().equals("Travel")) {
            description = "Master useful Dutch travel phrases and transportation vocabulary";
        }
        category.setDescription(description);
        
        // Set unit counts
        int unitCount = category.getName().equals("Greetings") ? 4 : 
                       (category.getName().equals("Food") ? 3 : 2);
        category.setUnitCount(unitCount);
        
        holder.categoryDescription.setText(category.getDescription());
        holder.unitCountText.setText(unitCount + " Units");
    }

    private void animateCardClick(CategoryViewHolder holder) {
        // Animate category image
        ObjectAnimator scaleXImage = ObjectAnimator.ofFloat(holder.categoryHeaderImage, "scaleX", 1f, ICON_SCALE);
        ObjectAnimator scaleYImage = ObjectAnimator.ofFloat(holder.categoryHeaderImage, "scaleY", 1f, ICON_SCALE);
        
        // Create animation set
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXImage, scaleYImage);
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new BounceInterpolator());
        
        // Play reverse animation after delay
        animatorSet.start();
        holder.cardView.postDelayed(() -> {
            ObjectAnimator scaleXImageReverse = ObjectAnimator.ofFloat(holder.categoryHeaderImage, "scaleX", ICON_SCALE, 1f);
            ObjectAnimator scaleYImageReverse = ObjectAnimator.ofFloat(holder.categoryHeaderImage, "scaleY", ICON_SCALE, 1f);
            
            AnimatorSet reverseSet = new AnimatorSet();
            reverseSet.playTogether(scaleXImageReverse, scaleYImageReverse);
            reverseSet.setDuration(ANIMATION_DURATION);
            reverseSet.setInterpolator(new AccelerateDecelerateInterpolator());
            reverseSet.start();
        }, ANIMATION_DURATION);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView categoryHeaderImage;
        TextView categoryTitle;
        MaterialButton exploreButton;
        MaterialButton startLearningButton;
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