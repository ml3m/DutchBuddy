package com.example.dutch_buddy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dutch_buddy.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_VOCABULARY = "vocabulary";

    // Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DUTCH_WORD = "dutch_word";
    public static final String COLUMN_ENGLISH_TRANSLATION = "english_translation";

    // Create table query
    private static final String CREATE_TABLE_VOCABULARY = "CREATE TABLE " + TABLE_VOCABULARY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_DUTCH_WORD + " TEXT NOT NULL, "
            + COLUMN_ENGLISH_TRANSLATION + " TEXT NOT NULL);";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VOCABULARY);
        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCABULARY);
        onCreate(db);
    }

    private void populateInitialData(SQLiteDatabase db) {
        // Greetings
        addVocabularyItem(db, "Greetings", "Hallo", "Hello");
        addVocabularyItem(db, "Greetings", "Goedemorgen", "Good morning");
        addVocabularyItem(db, "Greetings", "Goedemiddag", "Good afternoon");
        addVocabularyItem(db, "Greetings", "Goedenavond", "Good evening");
        addVocabularyItem(db, "Greetings", "Tot ziens", "Goodbye");
        addVocabularyItem(db, "Greetings", "Hoe gaat het?", "How are you?");
        addVocabularyItem(db, "Greetings", "Het gaat goed", "I am fine");
        addVocabularyItem(db, "Greetings", "Dank je", "Thank you");
        addVocabularyItem(db, "Greetings", "Alsjeblieft", "Please/You're welcome");
        addVocabularyItem(db, "Greetings", "Sorry", "Sorry");
        addVocabularyItem(db, "Greetings", "Ik heet", "My name is");
        addVocabularyItem(db, "Greetings", "Aangenaam", "Nice to meet you");
        addVocabularyItem(db, "Greetings", "Tot morgen", "See you tomorrow");
        addVocabularyItem(db, "Greetings", "Welkom", "Welcome");
        addVocabularyItem(db, "Greetings", "Prettige dag", "Have a nice day");

        // Food
        addVocabularyItem(db, "Food", "Brood", "Bread");
        addVocabularyItem(db, "Food", "Koffie", "Coffee");
        addVocabularyItem(db, "Food", "Thee", "Tea");
        addVocabularyItem(db, "Food", "Water", "Water");
        addVocabularyItem(db, "Food", "Kaas", "Cheese");
        addVocabularyItem(db, "Food", "Appel", "Apple");
        addVocabularyItem(db, "Food", "Boterham", "Sandwich");
        addVocabularyItem(db, "Food", "Melk", "Milk");
        addVocabularyItem(db, "Food", "Suiker", "Sugar");
        addVocabularyItem(db, "Food", "Zout", "Salt");
        addVocabularyItem(db, "Food", "Peper", "Pepper");
        addVocabularyItem(db, "Food", "Vlees", "Meat");
        addVocabularyItem(db, "Food", "Vis", "Fish");
        addVocabularyItem(db, "Food", "Rijst", "Rice");
        addVocabularyItem(db, "Food", "Pasta", "Pasta");
        addVocabularyItem(db, "Food", "Aardappel", "Potato");
        addVocabularyItem(db, "Food", "Groente", "Vegetable");
        addVocabularyItem(db, "Food", "Fruit", "Fruit");
        addVocabularyItem(db, "Food", "Soep", "Soup");
        addVocabularyItem(db, "Food", "Salade", "Salad");
        addVocabularyItem(db, "Food", "Friet", "French fries");
        addVocabularyItem(db, "Food", "Stroopwafel", "Stroopwafel");

        // Travel
        addVocabularyItem(db, "Travel", "Trein", "Train");
        addVocabularyItem(db, "Travel", "Vliegtuig", "Airplane");
        addVocabularyItem(db, "Travel", "Bus", "Bus");
        addVocabularyItem(db, "Travel", "Metro", "Subway");
        addVocabularyItem(db, "Travel", "Station", "Station");
        addVocabularyItem(db, "Travel", "Kaartje", "Ticket");
        addVocabularyItem(db, "Travel", "Hotel", "Hotel");
        addVocabularyItem(db, "Travel", "Paspoort", "Passport");
        addVocabularyItem(db, "Travel", "Bagage", "Luggage");
        addVocabularyItem(db, "Travel", "Links", "Left");
        addVocabularyItem(db, "Travel", "Rechts", "Right");
        addVocabularyItem(db, "Travel", "Rechtdoor", "Straight ahead");
        addVocabularyItem(db, "Travel", "Waar is", "Where is");
        addVocabularyItem(db, "Travel", "Ver", "Far");
        addVocabularyItem(db, "Travel", "Dichtbij", "Near");
        addVocabularyItem(db, "Travel", "Centrum", "City center");
        addVocabularyItem(db, "Travel", "Luchthaven", "Airport");
        addVocabularyItem(db, "Travel", "Fiets", "Bicycle");
        addVocabularyItem(db, "Travel", "Tram", "Tram");
        addVocabularyItem(db, "Travel", "Auto", "Car");
        addVocabularyItem(db, "Travel", "Taxi", "Taxi");
        addVocabularyItem(db, "Travel", "Boot", "Boat");
    }

    private void addVocabularyItem(SQLiteDatabase db, String category, String dutchWord, String englishTranslation) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DUTCH_WORD, dutchWord);
        values.put(COLUMN_ENGLISH_TRANSLATION, englishTranslation);
        db.insert(TABLE_VOCABULARY, null, values);
    }

    public void addVocabularyItem(String category, String dutchWord, String englishTranslation) {
        SQLiteDatabase db = this.getWritableDatabase();
        addVocabularyItem(db, category, dutchWord, englishTranslation);
        db.close();
    }

    public List<VocabularyItem> getVocabularyByCategory(String category) {
        List<VocabularyItem> vocabularyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID, COLUMN_CATEGORY, COLUMN_DUTCH_WORD, COLUMN_ENGLISH_TRANSLATION};
        String selection = COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(TABLE_VOCABULARY, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String dutchWord = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUTCH_WORD));
                String englishTranslation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ENGLISH_TRANSLATION));

                VocabularyItem item = new VocabularyItem(id, category, dutchWord, englishTranslation);
                vocabularyList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return vocabularyList;
    }

    public List<String> getAllCategories() {
        Set<String> categories = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_CATEGORY};
        Cursor cursor = db.query(true, TABLE_VOCABULARY, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY));
                categories.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return new ArrayList<>(categories);
    }
} 