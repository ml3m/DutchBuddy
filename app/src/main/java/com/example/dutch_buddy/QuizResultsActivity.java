package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
            
            System.out.println("DEBUG: QuizResults onCreate with category: " + category + ", userID: " + userId);
        } else {
            finish(); // If required data is missing, close the activity
            return;
        }
        
        // Initialize database helper and load user
        databaseHelper = DatabaseHelper.getInstance(this);
        user = databaseHelper.getUser(userId);
        
        if (user == null) {
            System.out.println("DEBUG: User not found with ID: " + userId);
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
        
        // Mark the quiz lesson as completed
        markQuizCompleted();
        
        // Set up button click listeners
        newQuizButton.setOnClickListener(v -> {
            System.out.println("DEBUG: New Quiz button clicked");
            Intent quizIntent = new Intent(QuizResultsActivity.this, QuizActivity.class);
            quizIntent.putExtra("CATEGORY_NAME", category);
            quizIntent.putExtra("USER_ID", userId);
            startActivity(quizIntent);
            finish();
        });
        
        returnToCategoriesButton.setOnClickListener(v -> {
            System.out.println("DEBUG: Return to categories button clicked");
            // Go back to the learning path activity instead of main activity
            Intent learningPathIntent = new Intent(QuizResultsActivity.this, LearningPathActivity.class);
            learningPathIntent.putExtra("CATEGORY_NAME", category);
            learningPathIntent.putExtra("USER_ID", userId);
            startActivity(learningPathIntent);
            finish();
        });
    }
    
    private void markQuizCompleted() {
        // Find the quiz lesson for this category
        int lessonId = -1;
        
        // Query for the quiz lesson in this category
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT l." + DatabaseHelper.COLUMN_LESSON_ID + 
                      " FROM " + DatabaseHelper.TABLE_LESSONS + " l " +
                      " JOIN " + DatabaseHelper.TABLE_UNITS + " u " +
                      " ON l." + DatabaseHelper.COLUMN_LESSON_UNIT_ID + " = u." + DatabaseHelper.COLUMN_UNIT_ID +
                      " WHERE u." + DatabaseHelper.COLUMN_UNIT_CATEGORY + " = ? " +
                      " AND l." + DatabaseHelper.COLUMN_LESSON_TYPE + " = 'QUIZ' " +
                      " ORDER BY l." + DatabaseHelper.COLUMN_LESSON_ID + " ASC LIMIT 1";
        
        System.out.println("DEBUG: Quiz results showing for category: " + category);
        System.out.println("DEBUG: Quiz score: " + correctAnswers + " out of " + totalQuestions);
        
        Cursor cursor = db.rawQuery(query, new String[]{category});
        if (cursor.moveToFirst()) {
            lessonId = cursor.getInt(0);
            System.out.println("DEBUG: Found quiz lesson ID: " + lessonId);
        } else {
            System.out.println("DEBUG: No quiz lesson found for category: " + category + " with type QUIZ");
        }
        cursor.close();
        
        if (lessonId != -1) {
            System.out.println("DEBUG: Marking quiz lesson as completed: " + lessonId);
            // Mark the quiz as completed
            databaseHelper.updateLessonProgress(lessonId, true);
            
            // Check if all lessons in this unit are completed
            checkAndUpdateUnitCompletion(lessonId);
            
            // Unlock the next lesson if there is one
            int nextLessonId = databaseHelper.getNextLessonId(lessonId);
            if (nextLessonId != -1) {
                System.out.println("DEBUG: Unlocking next lesson after quiz: " + nextLessonId);
                databaseHelper.updateLessonUnlockStatus(nextLessonId, true);
            } else {
                System.out.println("DEBUG: No next lesson found after quiz: " + lessonId);
            }
        } else {
            System.out.println("DEBUG: Failed to find the quiz lesson ID for completion");
        }
    }
    
    private void checkAndUpdateUnitCompletion(int lessonId) {
        // Get the unit ID for the current lesson
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int unitId = -1;
        
        String unitQuery = "SELECT " + DatabaseHelper.COLUMN_LESSON_UNIT_ID + 
                         " FROM " + DatabaseHelper.TABLE_LESSONS + 
                         " WHERE " + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
        
        Cursor unitCursor = db.rawQuery(unitQuery, new String[]{String.valueOf(lessonId)});
        if (unitCursor.moveToFirst()) {
            unitId = unitCursor.getInt(0);
            System.out.println("DEBUG: Found unit ID: " + unitId + " for lesson: " + lessonId);
        }
        unitCursor.close();
        
        if (unitId != -1) {
            // Check if all lessons in this unit are completed
            String lessonsQuery = "SELECT COUNT(*) AS total, " +
                               "SUM(CASE WHEN " + DatabaseHelper.COLUMN_LESSON_COMPLETED + " = 1 THEN 1 ELSE 0 END) AS completed " +
                               "FROM " + DatabaseHelper.TABLE_LESSONS + 
                               " WHERE " + DatabaseHelper.COLUMN_LESSON_UNIT_ID + " = ?";
            
            Cursor lessonsCursor = db.rawQuery(lessonsQuery, new String[]{String.valueOf(unitId)});
            if (lessonsCursor.moveToFirst()) {
                int totalLessons = lessonsCursor.getInt(0);
                int completedLessons = lessonsCursor.getInt(1);
                
                System.out.println("DEBUG: Unit ID: " + unitId + " has " + completedLessons + 
                                 " completed lessons out of " + totalLessons);
                
                // If all lessons are completed, mark the unit as completed
                if (totalLessons > 0 && totalLessons == completedLessons) {
                    System.out.println("DEBUG: All lessons completed, marking unit as completed: " + unitId);
                    databaseHelper.updateUnitProgress(unitId, true);
                    
                    // Get the category for this unit
                    String categoryQuery = "SELECT " + DatabaseHelper.COLUMN_UNIT_CATEGORY + 
                                        " FROM " + DatabaseHelper.TABLE_UNITS + 
                                        " WHERE " + DatabaseHelper.COLUMN_UNIT_ID + " = ?";
                    
                    Cursor categoryCursor = db.rawQuery(categoryQuery, new String[]{String.valueOf(unitId)});
                    if (categoryCursor.moveToFirst()) {
                        String category = categoryCursor.getString(0);
                        
                        // Unlock the next unit in this category
                        System.out.println("DEBUG: Unlocking next unit in category: " + category);
                        databaseHelper.unlockNextUnit(category, unitId);
                    }
                    categoryCursor.close();
                }
            }
            lessonsCursor.close();
        }
        
        db.close();
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