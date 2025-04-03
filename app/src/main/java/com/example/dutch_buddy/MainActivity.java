package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dutch_buddy.adapters.CategoryAdapter;
import com.example.dutch_buddy.data.Category;
import com.example.dutch_buddy.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView categoriesRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Set up RecyclerView
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize category list
        initCategoryList();

        // Set up adapter
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoriesRecyclerView.setAdapter(categoryAdapter);
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