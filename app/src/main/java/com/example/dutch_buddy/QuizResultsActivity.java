package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class QuizResultsActivity extends AppCompatActivity {

    private TextView categoryNameResult;
    private TextView correctAnswersText;
    private TextView scorePercentText;
    private Button newQuizButton;
    private Button returnToCategoriesButton;
    
    private String category;
    private int correctAnswers;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        
        // Get data from intent
        if (getIntent() != null) {
            category = getIntent().getStringExtra("CATEGORY_NAME");
            correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
            totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        } else {
            finish(); // If required data is missing, close the activity
            return;
        }
        
        // Initialize views
        categoryNameResult = findViewById(R.id.categoryNameResult);
        correctAnswersText = findViewById(R.id.correctAnswersText);
        scorePercentText = findViewById(R.id.scorePercentText);
        newQuizButton = findViewById(R.id.newQuizButton);
        returnToCategoriesButton = findViewById(R.id.returnToCategoriesButton);
        
        // Set up the UI with quiz results
        displayResults();
        
        // Set up button click listeners
        newQuizButton.setOnClickListener(v -> {
            Intent quizIntent = new Intent(QuizResultsActivity.this, QuizActivity.class);
            quizIntent.putExtra("CATEGORY_NAME", category);
            startActivity(quizIntent);
            finish();
        });
        
        returnToCategoriesButton.setOnClickListener(v -> {
            // Go back to the categories screen (MainActivity)
            Intent mainIntent = new Intent(QuizResultsActivity.this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    }
} 