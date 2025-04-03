package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.dutch_buddy.adapters.FlashcardAdapter;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.VocabularyItem;

import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {

    private TextView categoryTitle;
    private ViewPager2 flashcardsViewPager;
    private Button previousButton;
    private Button nextButton;
    private TextView tapToFlipText;
    
    private String category;
    private List<VocabularyItem> vocabularyItems;
    private FlashcardAdapter flashcardAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        // Get the category from intent
        if (getIntent().hasExtra("category")) {
            category = getIntent().getStringExtra("category");
        } else {
            finish(); // If no category provided, close the activity
            return;
        }

        // Initialize views
        categoryTitle = findViewById(R.id.categoryTitle);
        flashcardsViewPager = findViewById(R.id.flashcardsViewPager);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        tapToFlipText = findViewById(R.id.tapToFlipText);

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
            }
        });

        // Update button states when page changes
        flashcardsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonStates(position);
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