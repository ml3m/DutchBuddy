package com.example.dutch_buddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.adapters.UnitAdapter;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.Unit;
import com.example.dutch_buddy.data.Lesson;

import java.util.List;

public class LearningPathActivity extends AppCompatActivity {

    private RecyclerView unitsRecyclerView;
    private UnitAdapter unitAdapter;
    private DatabaseHelper databaseHelper;
    private String categoryName;
    private int userId;
    private TextView categoryTitleTextView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_path);

        // Get category name from intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);

        if (categoryName == null || userId == -1) {
            finish();
            return;
        }

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Initialize views
        unitsRecyclerView = findViewById(R.id.unitsRecyclerView);
        categoryTitleTextView = findViewById(R.id.categoryTitleTextView);
        toolbar = findViewById(R.id.toolbar);

        // Setup toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        
        // Set category title
        categoryTitleTextView.setText(categoryName);

        // Setup RecyclerView
        unitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load units for this category
        loadUnits();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("DEBUG: LearningPathActivity onResume - Refreshing units for category: " + categoryName);
        // Reload the units to reflect any changes in completion status
        loadUnits();
    }
    
    private void loadUnits() {
        List<Unit> units = databaseHelper.getUnitsByCategory(categoryName);
        System.out.println("DEBUG: Loaded " + units.size() + " units for category: " + categoryName);
        
        // Debug unit information
        for (Unit unit : units) {
            System.out.println("DEBUG: Unit: " + unit.getName() + 
                              ", Unlocked: " + unit.isUnlocked() + 
                              ", Completed: " + unit.isCompleted());
            
            // Debug lessons in this unit
            List<Lesson> lessons = unit.getLessons();
            if (lessons != null) {
                for (Lesson lesson : lessons) {
                    System.out.println("DEBUG:   Lesson: " + lesson.getName() + 
                                      ", Type: " + lesson.getLessonType() + 
                                      ", Unlocked: " + lesson.isUnlocked() + 
                                      ", Completed: " + lesson.isCompleted());
                }
            }
        }
        
        // Setup adapter
        unitAdapter = new UnitAdapter(this, units, userId, categoryName);
        unitsRecyclerView.setAdapter(unitAdapter);
    }
} 