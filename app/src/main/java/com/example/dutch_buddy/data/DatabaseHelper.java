package com.example.dutch_buddy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dutch_buddy.db";
    private static final int DATABASE_VERSION = 2;

    // Table names
    public static final String TABLE_VOCABULARY = "vocabulary";
    public static final String TABLE_USERS = "users";

    // Vocabulary Column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DUTCH_WORD = "dutch_word";
    public static final String COLUMN_ENGLISH_TRANSLATION = "english_translation";

    // User Column names
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_EXPERIENCE_POINTS = "experience_points";
    public static final String COLUMN_TOTAL_QUIZZES = "total_quizzes";
    public static final String COLUMN_TOTAL_CORRECT_ANSWERS = "total_correct_answers";
    public static final String COLUMN_TOTAL_QUESTIONS = "total_questions";
    public static final String COLUMN_LAST_LOGIN = "last_login";
    public static final String COLUMN_DAILY_STREAK = "daily_streak";

    // Create vocabulary table query
    private static final String CREATE_TABLE_VOCABULARY = "CREATE TABLE " + TABLE_VOCABULARY + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_DUTCH_WORD + " TEXT NOT NULL, "
            + COLUMN_ENGLISH_TRANSLATION + " TEXT NOT NULL);";

    // Create users table query
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_LEVEL + " INTEGER DEFAULT 1, "
            + COLUMN_EXPERIENCE_POINTS + " INTEGER DEFAULT 0, "
            + COLUMN_TOTAL_QUIZZES + " INTEGER DEFAULT 0, "
            + COLUMN_TOTAL_CORRECT_ANSWERS + " INTEGER DEFAULT 0, "
            + COLUMN_TOTAL_QUESTIONS + " INTEGER DEFAULT 0, "
            + COLUMN_LAST_LOGIN + " INTEGER DEFAULT 0, "
            + COLUMN_DAILY_STREAK + " INTEGER DEFAULT 0);";

    private static DatabaseHelper instance;
    private Context context;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VOCABULARY);
        db.execSQL(CREATE_TABLE_USERS);
        populateInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add users table in version 2
            db.execSQL(CREATE_TABLE_USERS);
        }
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
    
    // USER MANAGEMENT METHODS
    
    public long createUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_LEVEL, 1);
        values.put(COLUMN_EXPERIENCE_POINTS, 0);
        values.put(COLUMN_TOTAL_QUIZZES, 0);
        values.put(COLUMN_TOTAL_CORRECT_ANSWERS, 0);
        values.put(COLUMN_TOTAL_QUESTIONS, 0);
        values.put(COLUMN_LAST_LOGIN, System.currentTimeMillis());
        values.put(COLUMN_DAILY_STREAK, 1);
        
        long userId = db.insert(TABLE_USERS, null, values);
        db.close();
        return userId;
    }
    
    public User getUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
            COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_LEVEL, COLUMN_EXPERIENCE_POINTS,
            COLUMN_TOTAL_QUIZZES, COLUMN_TOTAL_CORRECT_ANSWERS, COLUMN_TOTAL_QUESTIONS,
            COLUMN_LAST_LOGIN, COLUMN_DAILY_STREAK
        };
        
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE_POINTS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUIZZES)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CORRECT_ANSWERS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUESTIONS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_STREAK))
            );
        }
        
        cursor.close();
        db.close();
        return user;
    }
    
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
            COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_LEVEL, COLUMN_EXPERIENCE_POINTS,
            COLUMN_TOTAL_QUIZZES, COLUMN_TOTAL_CORRECT_ANSWERS, COLUMN_TOTAL_QUESTIONS,
            COLUMN_LAST_LOGIN, COLUMN_DAILY_STREAK
        };
        
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE_POINTS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUIZZES)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CORRECT_ANSWERS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUESTIONS)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_STREAK))
            );
        }
        
        cursor.close();
        db.close();
        return user;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
            COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_LEVEL, COLUMN_EXPERIENCE_POINTS,
            COLUMN_TOTAL_QUIZZES, COLUMN_TOTAL_CORRECT_ANSWERS, COLUMN_TOTAL_QUESTIONS,
            COLUMN_LAST_LOGIN, COLUMN_DAILY_STREAK
        };
        
        Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null, COLUMN_LEVEL + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCE_POINTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUIZZES)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CORRECT_ANSWERS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_QUESTIONS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_LOGIN)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DAILY_STREAK))
                );
                users.add(user);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return users;
    }
    
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_LEVEL, user.getLevel());
        values.put(COLUMN_EXPERIENCE_POINTS, user.getExperiencePoints());
        values.put(COLUMN_TOTAL_QUIZZES, user.getTotalQuizzesTaken());
        values.put(COLUMN_TOTAL_CORRECT_ANSWERS, user.getTotalCorrectAnswers());
        values.put(COLUMN_TOTAL_QUESTIONS, user.getTotalQuestions());
        values.put(COLUMN_LAST_LOGIN, user.getLastLoginDate());
        values.put(COLUMN_DAILY_STREAK, user.getDailyStreak());
        
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(user.getId())};
        
        db.update(TABLE_USERS, values, whereClause, whereArgs);
        db.close();
    }
    
    public void updateUserStats(int userId, int correctAnswers, int totalQuestions) {
        User user = getUser(userId);
        if (user != null) {
            // Update quiz stats
            user.setTotalQuizzesTaken(user.getTotalQuizzesTaken() + 1);
            user.setTotalCorrectAnswers(user.getTotalCorrectAnswers() + correctAnswers);
            user.setTotalQuestions(user.getTotalQuestions() + totalQuestions);
            
            // Add experience points (10 XP per correct answer)
            int xpEarned = correctAnswers * 10;
            boolean leveledUp = user.addExperiencePoints(xpEarned);
            
            // Update login streak
            updateLoginStreak(user);
            
            // Save changes
            updateUser(user);
        }
    }
    
    private void updateLoginStreak(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        
        calendar.setTimeInMillis(user.getLastLoginDate());
        int lastLoginDay = calendar.get(Calendar.DAY_OF_YEAR);
        
        if (currentDay == lastLoginDay) {
            // Already logged in today, nothing to update
            return;
        } else if (currentDay == lastLoginDay + 1) {
            // Consecutive day, increase streak
            user.setDailyStreak(user.getDailyStreak() + 1);
        } else {
            // Streak broken
            user.setDailyStreak(1);
        }
        
        // Update last login date
        user.setLastLoginDate(System.currentTimeMillis());
    }
} 