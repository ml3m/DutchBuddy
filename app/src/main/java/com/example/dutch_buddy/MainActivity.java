package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.dutch_buddy.adapters.CategoryAdapter;
import com.example.dutch_buddy.data.Category;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DatabaseHelper databaseHelper;
    private MaterialCardView profileSummaryCard;
    private TextView userWelcomeText;
    private TextView levelIndicator;
    private TextView streakIndicator;
    private ImageButton profileButton;
    private MaterialToolbar toolbar;
    
    private int userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user ID from intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            // No user found, go to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize views
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        profileSummaryCard = findViewById(R.id.profileSummaryCard);
        userWelcomeText = findViewById(R.id.userWelcomeText);
        levelIndicator = findViewById(R.id.levelIndicator);
        streakIndicator = findViewById(R.id.streakIndicator);
        profileButton = findViewById(R.id.profileButton);
        toolbar = findViewById(R.id.toolbar);
        
        // Set up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);
        
        // Load user data
        loadUserData();
        
        // Set up profile navigation
        profileSummaryCard.setOnClickListener(v -> navigateToProfile());
        profileButton.setOnClickListener(v -> navigateToProfile());

        // Set up RecyclerView with animations
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        categoriesRecyclerView.setLayoutManager(layoutManager);
        
        // Add layout animation for cards
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                this, R.anim.layout_animation_slide_from_bottom);
        categoriesRecyclerView.setLayoutAnimation(animation);
        
        // Initialize category list
        initCategoryList();

        // Set up adapter
        categoryAdapter = new CategoryAdapter(this, categoryList, userId);
        categoriesRecyclerView.setAdapter(categoryAdapter);
        
        // Run the animation
        categoriesRecyclerView.scheduleLayoutAnimation();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Reload user data when returning to this screen
        loadUserData();
    }
    
    private void loadUserData() {
        currentUser = databaseHelper.getUser(userId);
        if (currentUser == null) {
            // User not found, go to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        // Update UI with user data
        userWelcomeText.setText("Hello, " + currentUser.getUsername() + "!");
        levelIndicator.setText("Level " + currentUser.getLevel());
        streakIndicator.setText(currentUser.getDailyStreak() + " day streak");
    }
    
    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void initCategoryList() {
        categoryList = new ArrayList<>();
        
        // Get categories from database
        List<String> dbCategories = databaseHelper.getAllCategories();
        
        // Add categories with icons
        for (String categoryName : dbCategories) {
            int iconResId = R.drawable.ic_greetings; // Default icon
            
            // Assign appropriate icon based on category name
            if (categoryName.equals("Greetings")) {
                iconResId = R.drawable.ic_greetings;
            } else if (categoryName.equals("Food")) {
                iconResId = R.drawable.ic_food;
            } else if (categoryName.equals("Travel")) {
                iconResId = R.drawable.ic_travel;
            }
            
            categoryList.add(new Category(categoryName, iconResId));
        }
        
        // If no categories found in database, add default ones
        if (categoryList.isEmpty()) {
            categoryList.add(new Category("Greetings", R.drawable.ic_greetings));
            categoryList.add(new Category("Food", R.drawable.ic_food));
            categoryList.add(new Category("Travel", R.drawable.ic_travel));
        }
    }
} 