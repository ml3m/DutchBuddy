package com.example.dutch_buddy;

import android.content.Intent;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sentence);
        
        // Get the category name from the intent
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        
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
        
        // Add exercises based on the category
        switch (categoryName) {
            case "Greetings":
                exerciseList.add(new SentenceExercise("Goedemorgen, hoe gaat het met ___?", "jou", "It's a personal pronoun (you)"));
                exerciseList.add(new SentenceExercise("___ ben ik blij om je te zien!", "Wat", "This word expresses intensity"));
                exerciseList.add(new SentenceExercise("Tot ___, zie je morgen!", "ziens", "It's part of a common goodbye phrase"));
                break;
            case "Food":
                exerciseList.add(new SentenceExercise("Ik wil graag een ___ koffie bestellen.", "kopje", "It's a container for drinks"));
                exerciseList.add(new SentenceExercise("Dit restaurant is ___ voor vegetariërs.", "perfect", "It means 'ideal' or 'without flaws'"));
                exerciseList.add(new SentenceExercise("Kan ik de ___ zien, alstublieft?", "menukaart", "You need this to order food"));
                break;
            case "Travel":
                exerciseList.add(new SentenceExercise("De ___ vertrekt over tien minuten.", "trein", "It runs on tracks"));
                exerciseList.add(new SentenceExercise("Ik moet mijn ___ inchecken bij de balie.", "bagage", "You pack your clothes in this"));
                exerciseList.add(new SentenceExercise("Kunt u mij de ___ naar het museum wijzen?", "weg", "You follow this to reach a destination"));
                break;
            default:
                // Default exercises if category is not recognized
                exerciseList.add(new SentenceExercise("Nederlands is een ___ taal om te leren.", "leuke", "It's a positive adjective"));
                exerciseList.add(new SentenceExercise("Ik woon in ___ sinds vorig jaar.", "Nederland", "It's a country name"));
                break;
        }
        
        // Shuffle the exercises
        Collections.shuffle(exerciseList);
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
                        Toast.makeText(CompleteSentenceActivity.this, 
                                "All exercises completed! Well done!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }, 1500);
        } else {
            Toast.makeText(this, "Incorrect, try again!", Toast.LENGTH_SHORT).show();
        }
    }
} 