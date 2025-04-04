package com.example.dutch_buddy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.adapters.WordBankAdapter;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.SentenceExercise;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CompleteSentenceActivity extends AppCompatActivity implements WordBankAdapter.OnWordClickListener {

    private TextView sentenceTextView;
    private TextView selectedWordTextView;
    private TextView hintTextView;
    private RecyclerView wordBankRecyclerView;
    private MaterialButton submitButton;
    private MaterialButton backButton;
    
    private WordBankAdapter wordBankAdapter;
    private List<String> wordBank;
    private String selectedWord = "";
    private SentenceExercise currentExercise;
    private List<SentenceExercise> exerciseList;
    private int currentExerciseIndex = 0;
    private String categoryName;
    private int userId;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sentence);
        
        // Get the category name from the intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        userId = getIntent().getIntExtra("USER_ID", -1);
        
        System.out.println("DEBUG: CompleteSentenceActivity started for category: " + categoryName + ", userID: " + userId);
        
        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);
        
        // Initialize views
        sentenceTextView = findViewById(R.id.sentenceTextView);
        selectedWordTextView = findViewById(R.id.selectedWordTextView);
        hintTextView = findViewById(R.id.hintTextView);
        wordBankRecyclerView = findViewById(R.id.wordBankRecyclerView);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);
        
        // Set up the word bank RecyclerView
        wordBankRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        
        // Initialize exercise list based on category
        initializeExerciseList();
        
        // Load the first exercise
        if (!exerciseList.isEmpty()) {
            loadExercise(0);
        }
        
        // Set up the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        
        // Set up the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void initializeExerciseList() {
        exerciseList = new ArrayList<>();
        int lessonId = getIntent().getIntExtra("LESSON_ID", -1);
        
        if (lessonId != -1) {
            // Get the unit ID for this lesson
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            String query = "SELECT " + DatabaseHelper.COLUMN_LESSON_UNIT_ID + 
                          " FROM " + DatabaseHelper.TABLE_LESSONS + 
                          " WHERE " + DatabaseHelper.COLUMN_LESSON_ID + " = ?";
            
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(lessonId)});
            int unitId = -1;
            
            if (cursor.moveToFirst()) {
                unitId = cursor.getInt(0);
                System.out.println("DEBUG: Sentence exercise for lesson: " + lessonId + ", unit: " + unitId);
            }
            cursor.close();
            db.close();
            
            // Load exercises based on the unit ID
            if (unitId != -1) {
                switch (unitId) {
                    case 1: // Travel - Places
                        addTravelPlacesExercises();
                        break;
                    case 2: // Travel - Transportation
                        addTravelTransportationExercises();
                        break;
                    case 5: // Food - Basic Food
                        addFoodBasicExercises();
                        break;
                    case 6: // Food - Drinks
                        addFoodDrinksExercises();
                        break;
                    case 9: // Greetings - Basic Greetings
                        addBasicGreetingsExercises();
                        break;
                    case 10: // Greetings - Formal Greetings
                        addFormalGreetingsExercises();
                        break;
                    default:
                        // Fall back to category-based exercises if unit ID is not recognized
                        addExercisesByCategory();
                        break;
                }
            } else {
                // Fall back to category-based exercises if unit ID was not found
                addExercisesByCategory();
            }
        } else {
            // Legacy support for activities that don't pass the lesson ID
            addExercisesByCategory();
        }
        
        // Shuffle the exercises
        Collections.shuffle(exerciseList);
    }
    
    private void addExercisesByCategory() {
        // Add exercises based on the category (legacy approach)
        switch (categoryName) {
            case "Greetings":
                addBasicGreetingsExercises();
                break;
            case "Food":
                addFoodBasicExercises();
                break;
            case "Travel":
                addTravelPlacesExercises();
                break;
            default:
                // Default exercises if category is not recognized
                exerciseList.add(new SentenceExercise("Nederlands is een ___ taal om te leren.", "leuke", "It's a positive adjective"));
                exerciseList.add(new SentenceExercise("Ik woon in ___ sinds vorig jaar.", "Nederland", "It's a country name"));
                break;
        }
    }
    
    private void addTravelPlacesExercises() {
        // Travel - Places unit exercises
        exerciseList.add(new SentenceExercise("De ___ vertrekt over tien minuten.", "trein", "It runs on tracks"));
        exerciseList.add(new SentenceExercise("Ik moet mijn ___ inchecken bij de balie.", "bagage", "You pack your clothes in this"));
        exerciseList.add(new SentenceExercise("Kunt u mij de ___ naar het museum wijzen?", "weg", "You follow this to reach a destination"));
        exerciseList.add(new SentenceExercise("Is er een ___ in de buurt?", "hotel", "A place where travelers stay overnight"));
        exerciseList.add(new SentenceExercise("Waar is het ___ voor dit vliegtuig?", "kaartje", "You need this to board"));
    }
    
    private void addTravelTransportationExercises() {
        // Travel - Transportation unit exercises
        exerciseList.add(new SentenceExercise("We gaan met de ___ naar Amsterdam.", "trein", "A transportation method that runs on rails"));
        exerciseList.add(new SentenceExercise("Ik moet mijn ___ opladen voor de bus.", "OV-chipkaart", "A card used for public transport"));
        exerciseList.add(new SentenceExercise("Let op de ___ bij het oversteken.", "stoplicht", "It tells you when to stop or go"));
        exerciseList.add(new SentenceExercise("Mijn ___ staat geparkeerd bij het station.", "fiets", "A common Dutch transportation method with two wheels"));
        exerciseList.add(new SentenceExercise("Neem de tweede ___ rechts.", "afslag", "Turn off the highway here"));
        exerciseList.add(new SentenceExercise("De trein vertrekt vanaf ___ vijf.", "perron", "Where you wait for the train"));
    }
    
    private void addFoodBasicExercises() {
        // Food - Basic Food unit exercises
        exerciseList.add(new SentenceExercise("Ik wil graag een ___ koffie bestellen.", "kopje", "It's a container for drinks"));
        exerciseList.add(new SentenceExercise("Dit restaurant is ___ voor vegetariërs.", "perfect", "It means 'ideal' or 'without flaws'"));
        exerciseList.add(new SentenceExercise("Kan ik de ___ zien, alstublieft?", "menukaart", "You need this to order food"));
        exerciseList.add(new SentenceExercise("Ik wil graag een ___ brood kopen.", "stuk", "A portion or piece"));
        exerciseList.add(new SentenceExercise("Deze ___ smaakt heerlijk!", "appel", "A common fruit"));
    }
    
    private void addFoodDrinksExercises() {
        // Food - Drinks unit exercises
        exerciseList.add(new SentenceExercise("Mag ik een ___ water bestellen?", "glas", "A container made of glass"));
        exerciseList.add(new SentenceExercise("Ik heb ___, mag ik iets drinken?", "dorst", "The feeling when you need a drink"));
        exerciseList.add(new SentenceExercise("Wil je ___ of spa blauw?", "spa rood", "Dutch term for sparkling water"));
        exerciseList.add(new SentenceExercise("Ik drink 's ochtends graag een ___ koffie.", "kopje", "A small container for hot drinks"));
        exerciseList.add(new SentenceExercise("___ op je verjaardag!", "Proost", "What you say when raising glasses"));
        exerciseList.add(new SentenceExercise("Heb je liever ___ of appelsap?", "sinaasappelsap", "Made from oranges"));
    }
    
    private void addBasicGreetingsExercises() {
        // Greetings - Basic Greetings unit exercises
        exerciseList.add(new SentenceExercise("Goedemorgen, hoe gaat het met ___?", "jou", "It's a personal pronoun (you)"));
        exerciseList.add(new SentenceExercise("___ ben ik blij om je te zien!", "Wat", "This word expresses intensity"));
        exerciseList.add(new SentenceExercise("Tot ___, zie je morgen!", "ziens", "It's part of a common goodbye phrase"));
        exerciseList.add(new SentenceExercise("___ heet ik Jan.", "Hallo", "A common greeting"));
        exerciseList.add(new SentenceExercise("___ je voor het cadeau!", "Dank", "Express gratitude with this word"));
    }
    
    private void addFormalGreetingsExercises() {
        // Greetings - Formal Greetings unit exercises
        exerciseList.add(new SentenceExercise("___ mevrouw De Vries,", "Geachte", "Formal way to address someone in a letter"));
        exerciseList.add(new SentenceExercise("Zou u zich willen ___ aan onze nieuwe collega?", "voorstellen", "To introduce yourself"));
        exerciseList.add(new SentenceExercise("Ik heb een ___ om 15:00 uur.", "afspraak", "A scheduled meeting"));
        exerciseList.add(new SentenceExercise("___ maakt u het vandaag?", "Hoe", "Question word in a formal greeting"));
        exerciseList.add(new SentenceExercise("Mag ik u mijn ___ geven?", "visitekaartje", "A card with your professional details"));
        exerciseList.add(new SentenceExercise("Met ___ groet,", "vriendelijke", "Part of a formal letter closing"));
    }
    
    private void loadExercise(int index) {
        if (index < 0 || index >= exerciseList.size()) {
            return;
        }
        
        currentExerciseIndex = index;
        currentExercise = exerciseList.get(index);
        
        // Display the sentence with a blank for the missing word
        String sentenceWithBlank = currentExercise.getSentence().replace(currentExercise.getMissingWord(), "_____");
        sentenceTextView.setText(sentenceWithBlank);
        
        // Display the hint
        hintTextView.setText("Hint: " + currentExercise.getHint());
        
        // Reset the selected word
        selectedWord = "";
        selectedWordTextView.setText("");
        
        // Create a word bank with the correct answer and some distractors
        generateWordBank();
    }
    
    private void generateWordBank() {
        wordBank = new ArrayList<>();
        
        // Add the correct answer
        wordBank.add(currentExercise.getMissingWord());
        
        // Add distractors based on the category
        switch (categoryName) {
            case "Greetings":
                wordBank.addAll(Arrays.asList("hallo", "dag", "doei", "goed", "slecht"));
                break;
            case "Food":
                wordBank.addAll(Arrays.asList("bord", "mes", "vork", "lekker", "eten"));
                break;
            case "Travel":
                wordBank.addAll(Arrays.asList("auto", "vliegtuig", "kaartje", "hotel", "reis"));
                break;
            default:
                wordBank.addAll(Arrays.asList("mooi", "goed", "slecht", "groot", "klein"));
                break;
        }
        
        // Limit to 8 words maximum and shuffle
        if (wordBank.size() > 8) {
            wordBank = wordBank.subList(0, 8);
        }
        Collections.shuffle(wordBank);
        
        // Set up the adapter
        wordBankAdapter = new WordBankAdapter(wordBank, this);
        wordBankRecyclerView.setAdapter(wordBankAdapter);
    }
    
    @Override
    public void onWordClick(String word) {
        // Update the selected word
        selectedWord = word;
        selectedWordTextView.setText(word);
    }
    
    private void checkAnswer() {
        if (selectedWord.isEmpty()) {
            Toast.makeText(this, "Please select a word first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedWord.equals(currentExercise.getMissingWord())) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            
            // Move to the next exercise after a short delay
            wordBankRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (currentExerciseIndex < exerciseList.size() - 1) {
                        loadExercise(currentExerciseIndex + 1);
                    } else {
                        // All exercises completed
                        System.out.println("DEBUG: All sentence exercises completed for category: " + categoryName);
                        Toast.makeText(CompleteSentenceActivity.this, 
                                "All exercises completed! Well done!", Toast.LENGTH_LONG).show();
                        
                        // Mark the lesson as completed and unlock next unit/lesson
                        markSentenceLessonCompleted();
                    }
                }
            }, 1500);
        } else {
            Toast.makeText(this, "Incorrect, try again!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void markSentenceLessonCompleted() {
        // Find the sentence lesson for this category
        int lessonId = -1;
        
        // Query for the sentence lesson in this category
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT l." + DatabaseHelper.COLUMN_LESSON_ID + 
                      " FROM " + DatabaseHelper.TABLE_LESSONS + " l " +
                      " JOIN " + DatabaseHelper.TABLE_UNITS + " u " +
                      " ON l." + DatabaseHelper.COLUMN_LESSON_UNIT_ID + " = u." + DatabaseHelper.COLUMN_UNIT_ID +
                      " WHERE u." + DatabaseHelper.COLUMN_UNIT_CATEGORY + " = ? " +
                      " AND l." + DatabaseHelper.COLUMN_LESSON_TYPE + " = 'SENTENCE' " +
                      " ORDER BY l." + DatabaseHelper.COLUMN_LESSON_ID + " ASC LIMIT 1";
        
        System.out.println("DEBUG: Finding sentence lesson for category: " + categoryName);
        
        Cursor cursor = db.rawQuery(query, new String[]{categoryName});
        if (cursor.moveToFirst()) {
            lessonId = cursor.getInt(0);
            System.out.println("DEBUG: Found sentence lesson ID: " + lessonId);
        } else {
            System.out.println("DEBUG: No sentence lesson found for category: " + categoryName);
        }
        cursor.close();
        db.close();
        
        if (lessonId != -1) {
            System.out.println("DEBUG: Marking sentence lesson as completed: " + lessonId);
            // This will automatically handle unit completion and unlocking the next lesson
            databaseHelper.updateLessonProgress(lessonId, true);
            
            Toast.makeText(this, "Lesson completed! You're making great progress!", Toast.LENGTH_SHORT).show();
            
            // Return to the learning path to see progression
            navigateToLearningPath();
        } else {
            System.out.println("DEBUG: Failed to find the sentence lesson ID for completion");
            Toast.makeText(this, "Could not update progress, but you can continue.", Toast.LENGTH_SHORT).show();
            navigateToLearningPath();
        }
    }
    
    private void navigateToLearningPath() {
        Intent intent = new Intent(this, LearningPathActivity.class);
        intent.putExtra("CATEGORY_NAME", categoryName);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        finish();
    }
} 