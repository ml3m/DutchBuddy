package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dutch_buddy.adapters.FlashcardAdapter;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.VocabularyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {

    private TextView categoryTitle;
    private ViewPager2 flashcardsViewPager;
    private Button previousButton;
    private Button nextButton;
    private TextView tapToFlipText;
    private FloatingActionButton completeButton;
    
    private String category;
    private int userId;
    private int lessonId;
    private List<VocabularyItem> vocabularyItems;
    private FlashcardAdapter flashcardAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        // Get parameters from intent
        category = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);
        lessonId = getIntent().getIntExtra("LESSON_ID", -1);
        
        // Initialize views
        categoryTitle = findViewById(R.id.categoryTitle);
        flashcardsViewPager = findViewById(R.id.flashcardsViewPager);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        tapToFlipText = findViewById(R.id.tapToFlipText);
        completeButton = findViewById(R.id.completeButton);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Get vocabulary items based on lesson
        if (lessonId != -1) {
            setLessonTitle(lessonId);
            vocabularyItems = getVocabularyForLesson(lessonId);
        } else {
            // Legacy approach - use category
            categoryTitle.setText(category);
            vocabularyItems = databaseHelper.getVocabularyByCategory(category);
        }

        // Set up ViewPager with adapter
        flashcardAdapter = new FlashcardAdapter(this, vocabularyItems);
        flashcardsViewPager.setAdapter(flashcardAdapter);

        // Set up navigation buttons
        setupNavigationButtons();
        
        // Set up complete button
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessonId != -1) {
                    markSpecificLessonCompleted(lessonId);
                } else {
                    markFlashcardsCompleted();
                }
            }
        });
    }
    
    /**
     * Sets the title based on the unit of the lesson
     */
    private void setLessonTitle(int lessonId) {
        // Query for the unit name for this lesson
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT u." + DatabaseHelper.COLUMN_UNIT_NAME + 
                      " FROM " + DatabaseHelper.TABLE_UNITS + " u " +
                      " JOIN " + DatabaseHelper.TABLE_LESSONS + " l " +
                      " ON u." + DatabaseHelper.COLUMN_UNIT_ID + " = l." + DatabaseHelper.COLUMN_LESSON_UNIT_ID +
                      " WHERE l." + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonId)});
        if (cursor.moveToFirst()) {
            String unitName = cursor.getString(0);
            categoryTitle.setText(unitName);
        } else {
            categoryTitle.setText(category);
        }
        cursor.close();
        db.close();
    }
    
    /**
     * Gets vocabulary items specific to the lesson's unit
     */
    private List<VocabularyItem> getVocabularyForLesson(int lessonId) {
        // Get the unit ID for this lesson
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COLUMN_LESSON_UNIT_ID + 
                      " FROM " + DatabaseHelper.TABLE_LESSONS + 
                      " WHERE " + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonId)});
        int unitId = -1;
        
        if (cursor.moveToFirst()) {
            unitId = cursor.getInt(0);
            System.out.println("DEBUG: Flashcards for lesson: " + lessonId + ", unit: " + unitId);
        }
        cursor.close();
        
        // Get vocabulary for the unit
        List<VocabularyItem> unitVocabulary = databaseHelper.getVocabularyByCategory(category);
        
        // If we have a valid unit ID, filter the vocabulary
        if (unitId != -1) {
            unitVocabulary = filterVocabularyByUnit(unitVocabulary, unitId);
        }
        
        db.close();
        return unitVocabulary;
    }
    
    /**
     * Filters vocabulary items based on the unit ID
     */
    private List<VocabularyItem> filterVocabularyByUnit(List<VocabularyItem> allItems, int unitId) {
        // Use the same filtering logic as in QuizActivity
        switch (unitId) {
            case 1: // Travel - Places
                return filterWordsByType(allItems, new String[]{"hotel", "station", "luchthaven", "centrum", "museum"});
            case 2: // Travel - Transportation
                return filterWordsByType(allItems, new String[]{"trein", "bus", "metro", "tram", "fiets", "auto", "taxi", 
                                                              "boot", "vliegtuig", "ov-chipkaart", "perron", "snelweg", 
                                                              "afslag", "stoplicht"});
            case 5: // Food - Basic Food
                return filterWordsByType(allItems, new String[]{"brood", "kaas", "appel", "groente", "fruit", "vlees", 
                                                              "vis", "aardappel", "pasta", "rijst", "boterham", "soep", 
                                                              "salade"});
            case 6: // Food - Drinks
                return filterWordsByType(allItems, new String[]{"koffie", "thee", "water", "melk", "sap", "bier", "wijn", 
                                                              "frisdrank", "sinaasappelsap", "appelsap", "chocolademelk", 
                                                              "limonade", "spa"});
            case 9: // Greetings - Basic Greetings
                return filterWordsByType(allItems, new String[]{"hallo", "goedemorgen", "goedemiddag", "goedenavond", 
                                                               "tot ziens", "dank je", "alsjeblieft", "tot morgen", 
                                                               "welkom", "sorry"});
            case 10: // Greetings - Formal Greetings
                return filterWordsByType(allItems, new String[]{"geachte", "meneer", "mevrouw", "vriendelijke groet", 
                                                              "hoogachtend", "afspraak", "vergadering", "kennismaken", 
                                                              "visitekaartje"});
            default:
                return allItems;
        }
    }
    
    /**
     * Filters vocabulary items based on word types
     */
    private List<VocabularyItem> filterWordsByType(List<VocabularyItem> allItems, String[] wordTypes) {
        java.util.List<VocabularyItem> filteredItems = new java.util.ArrayList<>();
        
        for (VocabularyItem item : allItems) {
            String word = item.getDutchWord().toLowerCase();
            for (String wordType : wordTypes) {
                if (word.contains(wordType.toLowerCase())) {
                    filteredItems.add(item);
                    break;
                }
            }
        }
        
        // If we don't have enough filtered items, return all items
        if (filteredItems.size() < 5) {
            return allItems;
        }
        
        return filteredItems;
    }
    
    /**
     * Marks a specific lesson as completed
     */
    private void markSpecificLessonCompleted(int lessonId) {
        System.out.println("DEBUG: Marking specific lesson as completed: " + lessonId);
        databaseHelper.updateLessonProgress(lessonId, true);
        
        Toast.makeText(this, "Great job! You've completed this lesson.", Toast.LENGTH_SHORT).show();
        
        // Return to learning path
        Intent intent = new Intent(this, LearningPathActivity.class);
        intent.putExtra("CATEGORY_NAME", category);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }

    private void markFlashcardsCompleted() {
        // Find the first flashcard lesson for this category
        int lessonId = -1;
        
        // Get the first flashcard lesson for this category
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT l." + DatabaseHelper.COLUMN_LESSON_ID + 
                      " FROM " + DatabaseHelper.TABLE_LESSONS + " l " +
                      " JOIN " + DatabaseHelper.TABLE_UNITS + " u " +
                      " ON l." + DatabaseHelper.COLUMN_LESSON_UNIT_ID + " = u." + DatabaseHelper.COLUMN_UNIT_ID +
                      " WHERE u." + DatabaseHelper.COLUMN_UNIT_CATEGORY + " = ? " +
                      " AND l." + DatabaseHelper.COLUMN_LESSON_TYPE + " = 'VOCAB' " +
                      " ORDER BY l." + DatabaseHelper.COLUMN_LESSON_ID + " ASC LIMIT 1";
        
        System.out.println("DEBUG: Complete button clicked for category: " + category);
        
        Cursor cursor = db.rawQuery(query, new String[]{category});
        if (cursor.moveToFirst()) {
            lessonId = cursor.getInt(0);
            System.out.println("DEBUG: Found lesson ID: " + lessonId);
        } else {
            System.out.println("DEBUG: No lesson found for category: " + category + " with type VOCAB");
        }
        cursor.close();
        db.close();
        
        if (lessonId != -1) {
            System.out.println("DEBUG: Marking lesson as completed: " + lessonId);
            // This will automatically handle unit completion and unlocking the next lesson
            databaseHelper.updateLessonProgress(lessonId, true);
            
            Toast.makeText(this, "Great job! You've completed this lesson.", Toast.LENGTH_SHORT).show();
            
            // Return to category screen to see progression
            System.out.println("DEBUG: Navigating back to learning path for: " + category);
            Intent intent = new Intent(this, LearningPathActivity.class);
            intent.putExtra("CATEGORY_NAME", category);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            System.out.println("DEBUG: Failed to find the lesson ID for completion");
            // Still return to category screen even if lesson ID not found
            Toast.makeText(this, "Unable to update progress, but you can continue.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LearningPathActivity.class);
            intent.putExtra("CATEGORY_NAME", category);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        }
    }

    private void setupNavigationButtons() {
        previousButton.setOnClickListener(v -> {
            if (flashcardsViewPager.getCurrentItem() > 0) {
                flashcardsViewPager.setCurrentItem(flashcardsViewPager.getCurrentItem() - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            System.out.println("DEBUG: Next button clicked, current position: " + flashcardsViewPager.getCurrentItem() + " of " + (flashcardAdapter.getItemCount() - 1));
            if (flashcardsViewPager.getCurrentItem() < flashcardAdapter.getItemCount() - 1) {
                flashcardsViewPager.setCurrentItem(flashcardsViewPager.getCurrentItem() + 1);
            } else {
                // On last card, show complete button
                System.out.println("DEBUG: On last card, showing complete button");
                completeButton.setVisibility(View.VISIBLE);
            }
        });

        // Update button states when page changes
        flashcardsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonStates(position);
                // Show complete button if on last card
                if (position == flashcardAdapter.getItemCount() - 1) {
                    System.out.println("DEBUG: Page changed to last card, showing complete button");
                    completeButton.setVisibility(View.VISIBLE);
                } else {
                    completeButton.setVisibility(View.GONE);
                }
            }
        });

        // Initialize button states
        updateButtonStates(0);
    }

    private void updateButtonStates(int position) {
        previousButton.setEnabled(position > 0);
        nextButton.setEnabled(position < flashcardAdapter.getItemCount() - 1);
    }
} 