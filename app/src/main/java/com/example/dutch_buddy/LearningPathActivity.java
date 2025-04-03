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
    
    private void loadUnits() {
        List<Unit> units = databaseHelper.getUnitsByCategory(categoryName);
        
        // Setup adapter
        unitAdapter = new UnitAdapter(this, units, userId, categoryName);
        unitsRecyclerView.setAdapter(unitAdapter);
    }
} 