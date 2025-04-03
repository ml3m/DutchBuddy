package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView usernameLabel;
    private TextView levelText;
    private TextView levelProgressText;
    private ProgressBar levelProgressBar;
    private TextView streakText;
    private TextView quizTakenText;
    private TextView accuracyText;
    private Button logoutButton;
    
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        // Get user ID from intent
        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            finish();
            return;
        }
        
        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        usernameLabel = findViewById(R.id.usernameLabel);
        levelText = findViewById(R.id.levelText);
        levelProgressText = findViewById(R.id.levelProgressText);
        levelProgressBar = findViewById(R.id.levelProgressBar);
        streakText = findViewById(R.id.streakText);
        quizTakenText = findViewById(R.id.quizTakenText);
        accuracyText = findViewById(R.id.accuracyText);
        logoutButton = findViewById(R.id.logoutButton);
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);
        
        // Load user data
        loadUserData();
        
        // Set up click listeners
        backButton.setOnClickListener(v -> onBackPressed());
        
        logoutButton.setOnClickListener(v -> {
            // Clear saved user ID and go back to login screen
            getSharedPreferences("DutchBuddyPrefs", MODE_PRIVATE)
                .edit()
                .remove("current_user_id")
                .apply();
                
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user data when coming back to this screen
        loadUserData();
    }
    
    private void loadUserData() {
        currentUser = databaseHelper.getUser(userId);
        if (currentUser == null) {
            finish();
            return;
        }
        
        // Set user info
        usernameLabel.setText(currentUser.getUsername());
        levelText.setText("Level " + currentUser.getLevel());
        
        // Calculate progress to next level (100 XP per level)
        int currentXP = currentUser.getExperiencePoints();
        int nextLevelXP = currentUser.getLevel() * 100;
        int xpForCurrentLevel = (currentUser.getLevel() - 1) * 100;
        int progressToNextLevel = currentXP - xpForCurrentLevel;
        int xpNeededForNextLevel = nextLevelXP - xpForCurrentLevel;
        
        levelProgressText.setText(progressToNextLevel + " / " + xpNeededForNextLevel + " XP");
        levelProgressBar.setMax(xpNeededForNextLevel);
        levelProgressBar.setProgress(progressToNextLevel);
        
        // Set statistics
        streakText.setText(currentUser.getDailyStreak() + " days");
        quizTakenText.setText(String.valueOf(currentUser.getTotalQuizzesTaken()));
        
        // Format accuracy to two decimal places
        double accuracy = currentUser.getAccuracy();
        String formattedAccuracy = String.format("%.2f%%", accuracy);
        accuracyText.setText(formattedAccuracy);
    }
} 