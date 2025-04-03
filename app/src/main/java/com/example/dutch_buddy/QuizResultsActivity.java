package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.User;
import com.google.android.material.card.MaterialCardView;

public class QuizResultsActivity extends AppCompatActivity {

    private TextView categoryNameResult;
    private TextView correctAnswersText;
    private TextView scorePercentText;
    private MaterialCardView levelUpCard;
    private TextView levelUpText;
    private Button newQuizButton;
    private Button returnToCategoriesButton;
    
    private String category;
    private int correctAnswers;
    private int totalQuestions;
    private int userId;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        
        // Get data from intent
        if (getIntent() != null) {
            category = getIntent().getStringExtra("CATEGORY_NAME");
            correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
            totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
            userId = getIntent().getIntExtra("USER_ID", -1);
        } else {
            finish(); // If required data is missing, close the activity
            return;
        }
        
        // Initialize database helper and load user
        databaseHelper = DatabaseHelper.getInstance(this);
        user = databaseHelper.getUser(userId);
        
        if (user == null) {
            finish();
            return;
        }
        
        // Initialize views
        categoryNameResult = findViewById(R.id.categoryNameResult);
        correctAnswersText = findViewById(R.id.correctAnswersText);
        scorePercentText = findViewById(R.id.scorePercentText);
        levelUpCard = findViewById(R.id.levelUpCard);
        levelUpText = findViewById(R.id.levelUpText);
        newQuizButton = findViewById(R.id.newQuizButton);
        returnToCategoriesButton = findViewById(R.id.returnToCategoriesButton);
        
        // Set up the UI with quiz results
        displayResults();
        
        // Set up button click listeners
        newQuizButton.setOnClickListener(v -> {
            Intent quizIntent = new Intent(QuizResultsActivity.this, QuizActivity.class);
            quizIntent.putExtra("CATEGORY_NAME", category);
            quizIntent.putExtra("USER_ID", userId);
            startActivity(quizIntent);
            finish();
        });
        
        returnToCategoriesButton.setOnClickListener(v -> {
            // Go back to the main activity
            Intent mainIntent = new Intent(QuizResultsActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.putExtra("USER_ID", userId);
            startActivity(mainIntent);
            finish();
        });
    }
    
    private void displayResults() {
        // Calculate percentage score
        int percentScore = (totalQuestions > 0) ? (correctAnswers * 100 / totalQuestions) : 0;
        
        // Display results
        categoryNameResult.setText(category);
        correctAnswersText.setText(getString(R.string.correct_answers, correctAnswers, totalQuestions));
        scorePercentText.setText(getString(R.string.percent_score, percentScore));
        
        // Change score color based on performance
        if (percentScore >= 80) {
            scorePercentText.setTextColor(getResources().getColor(R.color.matcha_green, null));
        } else if (percentScore >= 60) {
            scorePercentText.setTextColor(getResources().getColor(R.color.sage_green, null));
        } else {
            scorePercentText.setTextColor(getResources().getColor(R.color.mocha_brown, null));
        }
        
        // Check if user has leveled up
        displayLevelUpInfo();
    }
    
    private void displayLevelUpInfo() {
        // Get user's current level
        int currentLevel = user.getLevel();
        int xpEarned = correctAnswers * 10; // 10 XP per correct answer
        
        // Simple calculation to check if the user would have leveled up
        int oldLevel = (user.getExperiencePoints() - xpEarned) / 100 + 1;
        
        if (currentLevel > oldLevel) {
            // User leveled up - show level up card
            levelUpCard.setVisibility(View.VISIBLE);
            levelUpText.setText("Congratulations! You reached Level " + currentLevel);
        } else {
            // User didn't level up - hide the card
            levelUpCard.setVisibility(View.GONE);
        }
    }
} 