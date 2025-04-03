package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
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
    private List<VocabularyItem> vocabularyItems;
    private FlashcardAdapter flashcardAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        // Get category name from intent
        category = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);
        
        // Initialize views
        categoryTitle = findViewById(R.id.categoryTitle);
        flashcardsViewPager = findViewById(R.id.flashcardsViewPager);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        tapToFlipText = findViewById(R.id.tapToFlipText);
        completeButton = findViewById(R.id.completeButton);

        // Set the category title
        categoryTitle.setText(category);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Get vocabulary items for the selected category
        vocabularyItems = databaseHelper.getVocabularyByCategory(category);

        // Set up ViewPager with adapter
        flashcardAdapter = new FlashcardAdapter(this, vocabularyItems);
        flashcardsViewPager.setAdapter(flashcardAdapter);

        // Set up navigation buttons
        setupNavigationButtons();
        
        // Set up complete button
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markFlashcardsCompleted();
            }
        });
    }
    
    private void markFlashcardsCompleted() {
        // Find the Common Places lesson and mark it complete
        // This is a simplification - in a real app, you'd look up the actual lesson ID
        int lessonId = databaseHelper.getLessonIdByNameAndCategory("Common Places", category);
        if (lessonId != -1) {
            databaseHelper.updateLessonProgress(lessonId, true);
            
            // Unlock the next lesson (Places Quiz)
            int nextLessonId = databaseHelper.getNextLessonId(lessonId);
            if (nextLessonId != -1) {
                // Make next lesson available
                databaseHelper.updateLessonUnlockStatus(nextLessonId, true);
                
                Toast.makeText(this, "Great job! You've completed this lesson.", Toast.LENGTH_SHORT).show();
                
                // Navigate to Quiz
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("CATEGORY_NAME", category);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        }
    }

    private void setupNavigationButtons() {
        previousButton.setOnClickListener(v -> {
            if (flashcardsViewPager.getCurrentItem() > 0) {
                flashcardsViewPager.setCurrentItem(flashcardsViewPager.getCurrentItem() - 1);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (flashcardsViewPager.getCurrentItem() < flashcardAdapter.getItemCount() - 1) {
                flashcardsViewPager.setCurrentItem(flashcardsViewPager.getCurrentItem() + 1);
            } else {
                // On last card, show complete button
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