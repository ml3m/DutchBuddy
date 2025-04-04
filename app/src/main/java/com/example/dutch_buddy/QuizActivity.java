package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.VocabularyItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView quizCategoryTitle;
    private TextView questionNumber;
    private TextView questionText;
    private TextView answerOption1;
    private TextView answerOption2;
    private TextView answerOption3;
    private TextView answerOption4;
    private Button submitButton;
    private ImageButton backButton;
    private MaterialCardView cardOption1;
    private MaterialCardView cardOption2;
    private MaterialCardView cardOption3;
    private MaterialCardView cardOption4;
    
    private String category;
    private List<VocabularyItem> quizItems;
    private List<VocabularyItem> allVocabularyItems;
    private DatabaseHelper databaseHelper;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 10; // Max number of questions
    private MaterialCardView selectedCardOption = null;
    private int userId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        // Get category from intent
        category = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);
        
        // Validate user ID
        if (userId == -1) {
            Toast.makeText(this, "User information missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize views
        quizCategoryTitle = findViewById(R.id.quizCategoryTitle);
        questionNumber = findViewById(R.id.questionNumber);
        questionText = findViewById(R.id.questionText);
        answerOption1 = findViewById(R.id.answerOption1);
        answerOption2 = findViewById(R.id.answerOption2);
        answerOption3 = findViewById(R.id.answerOption3);
        answerOption4 = findViewById(R.id.answerOption4);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        cardOption1 = findViewById(R.id.cardOption1);
        cardOption2 = findViewById(R.id.cardOption2);
        cardOption3 = findViewById(R.id.cardOption3);
        cardOption4 = findViewById(R.id.cardOption4);
        
        // Set up card clicks to select the answer
        setupCardClickListeners();
        
        // Set the category title
        quizCategoryTitle.setText(category + " Quiz");
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);
        
        // Get vocabulary items for the quiz
        prepareQuizItems();
        
        // Show the first question
        if (!quizItems.isEmpty()) {
            showQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "No vocabulary items available for this category", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // Set up submit button click listener
        submitButton.setOnClickListener(v -> {
            // Check if an answer is selected
            if (selectedCardOption == null) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check if the answer is correct
            String selectedAnswer = getSelectedAnswerText();
            
            if (selectedAnswer.equals(quizItems.get(currentQuestionIndex).getEnglishTranslation())) {
                correctAnswers++;
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect. The correct answer is: " + 
                        quizItems.get(currentQuestionIndex).getEnglishTranslation(), Toast.LENGTH_SHORT).show();
            }
            
            // Move to the next question or finish the quiz
            currentQuestionIndex++;
            
            if (currentQuestionIndex < quizItems.size()) {
                // Show the next question
                showQuestion(currentQuestionIndex);
            } else {
                // Update user stats
                updateUserStatistics();
                
                // All questions answered, show results
                Intent resultsIntent = new Intent(QuizActivity.this, QuizResultsActivity.class);
                resultsIntent.putExtra("CATEGORY_NAME", category);
                resultsIntent.putExtra("correctAnswers", correctAnswers);
                resultsIntent.putExtra("totalQuestions", quizItems.size());
                resultsIntent.putExtra("USER_ID", userId);
                startActivity(resultsIntent);
                finish();
            }
        });
        
        // Set up back button
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
    
    private void updateUserStatistics() {
        // Update user stats with results from this quiz
        databaseHelper.updateUserStats(userId, correctAnswers, quizItems.size());
    }
    
    private void setupCardClickListeners() {
        // Set up card clicks
        cardOption1.setOnClickListener(v -> selectAnswer(cardOption1));
        cardOption2.setOnClickListener(v -> selectAnswer(cardOption2));
        cardOption3.setOnClickListener(v -> selectAnswer(cardOption3));
        cardOption4.setOnClickListener(v -> selectAnswer(cardOption4));
    }
    
    private void selectAnswer(MaterialCardView selectedCard) {
        // Clear previous selection
        clearSelections();
        
        // Set new selection
        selectedCardOption = selectedCard;
        updateCardAppearance(selectedCard, true);
    }
    
    private void clearSelections() {
        // Reset all cards to default appearance
        updateCardAppearance(cardOption1, false);
        updateCardAppearance(cardOption2, false);
        updateCardAppearance(cardOption3, false);
        updateCardAppearance(cardOption4, false);
        selectedCardOption = null;
    }
    
    // Helper method to update card appearance
    private void updateCardAppearance(MaterialCardView card, boolean isSelected) {
        if (isSelected) {
            card.setStrokeWidth(4);
            card.setStrokeColor(getResources().getColor(R.color.matcha_green, getTheme()));
            card.setCardElevation(8);
            // Add ripple effect
            card.setClickable(true);
        } else {
            card.setStrokeWidth(0);
            card.setCardElevation(2);
        }
    }
    
    private String getSelectedAnswerText() {
        if (selectedCardOption == null) {
            return "";
        }
        
        if (selectedCardOption == cardOption1) {
            return answerOption1.getText().toString();
        } else if (selectedCardOption == cardOption2) {
            return answerOption2.getText().toString();
        } else if (selectedCardOption == cardOption3) {
            return answerOption3.getText().toString();
        } else if (selectedCardOption == cardOption4) {
            return answerOption4.getText().toString();
        }
        
        return "";
    }
    
    private void prepareQuizItems() {
        // Get all vocabulary items for the category
        allVocabularyItems = databaseHelper.getVocabularyByCategory(category);
        
        // If there are less than or equal to totalQuestions vocabulary items, use all of them
        if (allVocabularyItems.size() <= totalQuestions) {
            quizItems = new ArrayList<>(allVocabularyItems);
        } else {
            // Randomly select totalQuestions items
            quizItems = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();
            
            // Create a list of indices
            for (int i = 0; i < allVocabularyItems.size(); i++) {
                indices.add(i);
            }
            
            // Shuffle the indices
            Collections.shuffle(indices);
            
            // Add the first totalQuestions items to the quiz items
            for (int i = 0; i < totalQuestions; i++) {
                quizItems.add(allVocabularyItems.get(indices.get(i)));
            }
        }
        
        // Shuffle the quiz items
        Collections.shuffle(quizItems);
    }
    
    private void showQuestion(int index) {
        // Clear previous selection
        clearSelections();
        
        // Update question number
        questionNumber.setText("Question " + (index + 1) + "/" + quizItems.size());
        
        // Set the question (Dutch word)
        VocabularyItem currentItem = quizItems.get(index);
        questionText.setText(currentItem.getDutchWord());
        
        // Generate answer options (1 correct, 3 incorrect)
        List<String> answerOptions = generateAnswerOptions(currentItem);
        
        // Set the answer options
        answerOption1.setText(answerOptions.get(0));
        answerOption2.setText(answerOptions.get(1));
        answerOption3.setText(answerOptions.get(2));
        answerOption4.setText(answerOptions.get(3));
        
        // Update button text for the last question
        if (index == quizItems.size() - 1) {
            submitButton.setText(R.string.finish_quiz);
        } else {
            submitButton.setText(R.string.check_answer);
        }
    }
    
    private List<String> generateAnswerOptions(VocabularyItem currentItem) {
        List<String> options = new ArrayList<>();
        
        // Add the correct answer
        options.add(currentItem.getEnglishTranslation());
        
        // Create a list of all other translations
        List<String> otherTranslations = new ArrayList<>();
        for (VocabularyItem item : allVocabularyItems) {
            if (item.getId() != currentItem.getId()) {
                otherTranslations.add(item.getEnglishTranslation());
            }
        }
        
        // If there are less than 3 other items, we need to handle this
        if (otherTranslations.size() < 3) {
            // Just use what we have
            options.addAll(otherTranslations);
            
            // Add dummy options if needed
            String[] dummyOptions = {"Option A", "Option B", "Option C"};
            int dummyIndex = 0;
            while (options.size() < 4) {
                options.add(dummyOptions[dummyIndex++]);
            }
        } else {
            // Shuffle and take the first 3
            Collections.shuffle(otherTranslations);
            for (int i = 0; i < 3; i++) {
                options.add(otherTranslations.get(i));
            }
        }
        
        // Shuffle the options
        Collections.shuffle(options);
        
        return options;
    }
} 