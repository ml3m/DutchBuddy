package com.example.dutch_buddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private int lessonId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        // Get category from intent
        category = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);
        lessonId = getIntent().getIntExtra("LESSON_ID", -1);
        
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
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);
        
        // Get unit title for quiz if lesson ID is provided
        if (lessonId != -1) {
            setQuizTitle(lessonId);
        } else {
            quizCategoryTitle.setText(category + " Quiz");
        }
        
        // Get vocabulary items for the quiz
        prepareQuizItems(lessonId);
        
        // Show the first question
        if (!quizItems.isEmpty()) {
            showQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "No vocabulary items available for this quiz", Toast.LENGTH_SHORT).show();
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
    
    private void prepareQuizItems(int lessonId) {
        // If a specific lesson ID is provided, get vocabulary for that unit
        if (lessonId != -1) {
            prepareQuizItemsForLesson(lessonId);
        } else {
            // Legacy approach - get all items for the category
            prepareAllCategoryQuizItems();
        }
    }
    
    private void prepareQuizItemsForLesson(int lessonId) {
        // Get the unit ID for this lesson
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COLUMN_LESSON_UNIT_ID + 
                      " FROM " + DatabaseHelper.TABLE_LESSONS + 
                      " WHERE " + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonId)});
        int unitId = -1;
        
        if (cursor.moveToFirst()) {
            unitId = cursor.getInt(0);
            System.out.println("DEBUG: Quiz for lesson: " + lessonId + ", unit: " + unitId);
        }
        cursor.close();
        
        // Based on unit ID, select appropriate vocabulary
        if (unitId != -1) {
            // Get vocabulary specific to certain units
            allVocabularyItems = getVocabularyForUnit(unitId);
            System.out.println("DEBUG: Found " + allVocabularyItems.size() + " vocabulary items for unit: " + unitId);
        } else {
            // Fallback to category
            allVocabularyItems = databaseHelper.getVocabularyByCategory(category);
        }
        
        db.close();
        
        // Process quiz items
        processFinalQuizItems();
    }
    
    /**
     * Gets vocabulary specifically for a unit based on its ID
     */
    private List<VocabularyItem> getVocabularyForUnit(int unitId) {
        List<VocabularyItem> unitVocabulary = new ArrayList<>();
        
        // Get category vocabulary
        List<VocabularyItem> categoryVocabulary = databaseHelper.getVocabularyByCategory(category);
        
        // Filter based on unit ID
        switch (unitId) {
            case 1: // Travel - Places
                // Filter travel vocabulary about places
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isPlaceWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                break;
                
            case 2: // Travel - Transportation
                // Filter travel vocabulary about transportation
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isTransportationWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                
                // If we found fewer than 5 items, include all travel vocabulary
                if (unitVocabulary.size() < 5) {
                    return categoryVocabulary;
                }
                break;
                
            case 5: // Food - Basic Food
                // Filter food vocabulary about basic foods
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isBasicFoodWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                break;
                
            case 6: // Food - Drinks
                // Filter food vocabulary about drinks
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isDrinkWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                
                // If we found fewer than 5 items, include all food vocabulary
                if (unitVocabulary.size() < 5) {
                    return categoryVocabulary;
                }
                break;
                
            case 9: // Greetings - Basic Greetings
                // Filter greetings vocabulary for basic greetings
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isBasicGreetingWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                break;
                
            case 10: // Greetings - Formal Greetings
                // Filter greetings vocabulary for formal greetings
                for (VocabularyItem item : categoryVocabulary) {
                    String word = item.getDutchWord().toLowerCase();
                    if (isFormalGreetingWord(word)) {
                        unitVocabulary.add(item);
                    }
                }
                
                // If we found fewer than 5 items, include all greetings vocabulary
                if (unitVocabulary.size() < 5) {
                    return categoryVocabulary;
                }
                break;
                
            default:
                // Default to all vocabulary for the category
                return categoryVocabulary;
        }
        
        // If we couldn't find enough vocabulary specific to the unit, use all category vocabulary
        if (unitVocabulary.size() < 5) {
            return categoryVocabulary;
        }
        
        return unitVocabulary;
    }
    
    // Helper methods to identify words for specific units
    
    private boolean isPlaceWord(String word) {
        // Words related to places in the Travel category
        String[] placeWords = {"hotel", "station", "luchthaven", "centrum", "museum"};
        for (String placeWord : placeWords) {
            if (word.contains(placeWord)) return true;
        }
        return false;
    }
    
    private boolean isTransportationWord(String word) {
        // Words related to transportation in the Travel category
        String[] transportWords = {"trein", "bus", "metro", "tram", "fiets", "auto", "taxi", "boot", 
                                  "vliegtuig", "ov-chipkaart", "perron", "snelweg", "afslag", "stoplicht"};
        for (String transportWord : transportWords) {
            if (word.contains(transportWord)) return true;
        }
        return false;
    }
    
    private boolean isBasicFoodWord(String word) {
        // Words related to basic food in the Food category
        String[] basicFoodWords = {"brood", "kaas", "appel", "groente", "fruit", "vlees", "vis", 
                                  "aardappel", "pasta", "rijst", "boterham", "soep", "salade"};
        for (String foodWord : basicFoodWords) {
            if (word.contains(foodWord)) return true;
        }
        return false;
    }
    
    private boolean isDrinkWord(String word) {
        // Words related to drinks in the Food category
        String[] drinkWords = {"koffie", "thee", "water", "melk", "sap", "bier", "wijn", "frisdrank", 
                              "sinaasappelsap", "appelsap", "chocolademelk", "limonade", "spa"};
        for (String drinkWord : drinkWords) {
            if (word.contains(drinkWord)) return true;
        }
        return false;
    }
    
    private boolean isBasicGreetingWord(String word) {
        // Words related to basic greetings in the Greetings category
        String[] basicGreetingWords = {"hallo", "goedemorgen", "goedemiddag", "goedenavond", "tot ziens", 
                                      "dank je", "alsjeblieft", "tot morgen", "welkom", "sorry"};
        for (String greetingWord : basicGreetingWords) {
            if (word.contains(greetingWord.toLowerCase())) return true;
        }
        return false;
    }
    
    private boolean isFormalGreetingWord(String word) {
        // Words related to formal greetings in the Greetings category
        String[] formalGreetingWords = {"geachte", "meneer", "mevrouw", "vriendelijke groet", "hoogachtend", 
                                       "afspraak", "vergadering", "kennismaken", "visitekaartje"};
        for (String greetingWord : formalGreetingWords) {
            if (word.contains(greetingWord.toLowerCase())) return true;
        }
        return false;
    }
    
    private void prepareAllCategoryQuizItems() {
        // Legacy method - get all vocabulary items for the category
        allVocabularyItems = databaseHelper.getVocabularyByCategory(category);
        processFinalQuizItems();
    }
    
    private void processFinalQuizItems() {
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
            for (int i = 0; i < totalQuestions && i < indices.size(); i++) {
                quizItems.add(allVocabularyItems.get(indices.get(i)));
            }
        }
        
        // Shuffle the quiz items
        Collections.shuffle(quizItems);
    }
    
    /**
     * Sets the appropriate quiz title based on the lesson
     */
    private void setQuizTitle(int lessonId) {
        // Get the unit name for this lesson to make a more specific quiz title
        String unitName = "";
        
        // Query for the unit name for this lesson
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT u." + DatabaseHelper.COLUMN_UNIT_NAME + 
                      " FROM " + DatabaseHelper.TABLE_UNITS + " u " +
                      " JOIN " + DatabaseHelper.TABLE_LESSONS + " l " +
                      " ON u." + DatabaseHelper.COLUMN_UNIT_ID + " = l." + DatabaseHelper.COLUMN_LESSON_UNIT_ID +
                      " WHERE l." + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonId)});
        if (cursor.moveToFirst()) {
            unitName = cursor.getString(0);
        }
        cursor.close();
        db.close();
        
        // Set a more specific title
        if (!unitName.isEmpty()) {
            quizCategoryTitle.setText(unitName + " Quiz");
        } else {
            quizCategoryTitle.setText(category + " Quiz");
        }
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