package com.example.dutch_buddy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dutch_buddy.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dutch_buddy.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    public static final String TABLE_VOCABULARY = "vocabulary";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_UNITS = "units";
    public static final String TABLE_LESSONS = "lessons";

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
    
    // Units Column names
    public static final String COLUMN_UNIT_ID = "id";
    public static final String COLUMN_UNIT_NAME = "name";
    public static final String COLUMN_UNIT_DESCRIPTION = "description";
    public static final String COLUMN_UNIT_CATEGORY = "category";
    public static final String COLUMN_UNIT_ICON = "icon_resource_id";
    public static final String COLUMN_UNIT_UNLOCKED = "unlocked";
    public static final String COLUMN_UNIT_COMPLETED = "completed";
    
    // Lessons Column names
    public static final String COLUMN_LESSON_ID = "id";
    public static final String COLUMN_LESSON_NAME = "name";
    public static final String COLUMN_LESSON_DESCRIPTION = "description";
    public static final String COLUMN_LESSON_UNIT_ID = "unit_id";
    public static final String COLUMN_LESSON_ICON = "icon_resource_id";
    public static final String COLUMN_LESSON_UNLOCKED = "unlocked";
    public static final String COLUMN_LESSON_COMPLETED = "completed";
    public static final String COLUMN_LESSON_TYPE = "lesson_type";

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
            
    // Create units table query
    private static final String CREATE_TABLE_UNITS = "CREATE TABLE " + TABLE_UNITS + " ("
            + COLUMN_UNIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_UNIT_NAME + " TEXT NOT NULL, "
            + COLUMN_UNIT_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_UNIT_CATEGORY + " TEXT NOT NULL, "
            + COLUMN_UNIT_ICON + " INTEGER NOT NULL, "
            + COLUMN_UNIT_UNLOCKED + " INTEGER DEFAULT 0, "
            + COLUMN_UNIT_COMPLETED + " INTEGER DEFAULT 0);";
            
    // Create lessons table query
    private static final String CREATE_TABLE_LESSONS = "CREATE TABLE " + TABLE_LESSONS + " ("
            + COLUMN_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LESSON_NAME + " TEXT NOT NULL, "
            + COLUMN_LESSON_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_LESSON_UNIT_ID + " INTEGER NOT NULL, "
            + COLUMN_LESSON_ICON + " INTEGER NOT NULL, "
            + COLUMN_LESSON_UNLOCKED + " INTEGER DEFAULT 0, "
            + COLUMN_LESSON_COMPLETED + " INTEGER DEFAULT 0, "
            + COLUMN_LESSON_TYPE + " TEXT NOT NULL);";

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
        db.execSQL(CREATE_TABLE_UNITS);
        db.execSQL(CREATE_TABLE_LESSONS);
        populateInitialData(db);
        populateUnitsAndLessons(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add users table in version 2
            db.execSQL(CREATE_TABLE_USERS);
        }
        if (oldVersion < 3) {
            // Add units and lessons tables in version 3
            db.execSQL(CREATE_TABLE_UNITS);
            db.execSQL(CREATE_TABLE_LESSONS);
            populateUnitsAndLessons(db);
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

    private void populateUnitsAndLessons(SQLiteDatabase db) {
        // Travel category units
        addUnit(db, "Places", "Learn vocabulary for common places", "Travel", 
                R.drawable.ic_travel, 1, 0);
        addUnit(db, "Transportation", "Learn vocabulary for different transportation methods", "Travel", 
                R.drawable.ic_travel, 0, 0);
        addUnit(db, "Directions", "Learn vocabulary for asking and giving directions", "Travel", 
                R.drawable.ic_travel, 0, 0);
        addUnit(db, "Accommodations", "Learn vocabulary for hotels and accommodations", "Travel", 
                R.drawable.ic_travel, 0, 0);
        
        // Food category units
        addUnit(db, "Basic Food", "Learn vocabulary for common foods", "Food", 
                R.drawable.ic_food, 1, 0);
        addUnit(db, "Drinks", "Learn vocabulary for beverages", "Food", 
                R.drawable.ic_food, 0, 0);
        addUnit(db, "Restaurant", "Learn vocabulary for ordering at restaurants", "Food", 
                R.drawable.ic_food, 0, 0);
        addUnit(db, "Cooking", "Learn vocabulary for cooking and recipes", "Food", 
                R.drawable.ic_food, 0, 0);
        
        // Greetings category units
        addUnit(db, "Basic Greetings", "Learn basic greetings and introductions", "Greetings", 
                R.drawable.ic_greetings, 1, 0);
        addUnit(db, "Formal Greetings", "Learn formal greetings for professional settings", "Greetings", 
                R.drawable.ic_greetings, 0, 0);
        
        // Add lessons for Travel - Places unit
        addLesson(db, "Common Places", "Learn vocabulary for common places", 1, 
                R.drawable.ic_travel, 1, 0, "VOCAB");
        addLesson(db, "Places Quiz", "Test your knowledge of places", 1, 
                R.drawable.ic_travel, 0, 0, "QUIZ");
        addLesson(db, "Places Sentences", "Practice sentences about places", 1, 
                R.drawable.ic_travel, 0, 0, "SENTENCE");
        
        // Add lessons for Food - Basic Food unit
        addLesson(db, "Common Foods", "Learn vocabulary for common foods", 5, 
                R.drawable.ic_food, 1, 0, "VOCAB");
        addLesson(db, "Foods Quiz", "Test your knowledge of foods", 5, 
                R.drawable.ic_food, 0, 0, "QUIZ");
        addLesson(db, "Food Sentences", "Practice sentences about food", 5, 
                R.drawable.ic_food, 0, 0, "SENTENCE");
        
        // Add lessons for Greetings - Basic Greetings unit
        addLesson(db, "Basic Greetings", "Learn basic greeting vocabulary", 9, 
                R.drawable.ic_greetings, 1, 0, "VOCAB");
        addLesson(db, "Greetings Quiz", "Test your knowledge of greetings", 9, 
                R.drawable.ic_greetings, 0, 0, "QUIZ");
        addLesson(db, "Greeting Sentences", "Practice sentences with greetings", 9, 
                R.drawable.ic_greetings, 0, 0, "SENTENCE");
    }
    
    private void addUnit(SQLiteDatabase db, String name, String description, String category, 
                        int iconResId, int unlocked, int completed) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_UNIT_NAME, name);
        values.put(COLUMN_UNIT_DESCRIPTION, description);
        values.put(COLUMN_UNIT_CATEGORY, category);
        values.put(COLUMN_UNIT_ICON, iconResId);
        values.put(COLUMN_UNIT_UNLOCKED, unlocked);
        values.put(COLUMN_UNIT_COMPLETED, completed);
        db.insert(TABLE_UNITS, null, values);
    }
    
    private void addLesson(SQLiteDatabase db, String name, String description, int unitId, 
                          int iconResId, int unlocked, int completed, String lessonType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_NAME, name);
        values.put(COLUMN_LESSON_DESCRIPTION, description);
        values.put(COLUMN_LESSON_UNIT_ID, unitId);
        values.put(COLUMN_LESSON_ICON, iconResId);
        values.put(COLUMN_LESSON_UNLOCKED, unlocked);
        values.put(COLUMN_LESSON_COMPLETED, completed);
        values.put(COLUMN_LESSON_TYPE, lessonType);
        db.insert(TABLE_LESSONS, null, values);
    }
    
    public List<Unit> getUnitsByCategory(String category) {
        List<Unit> unitList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
                COLUMN_UNIT_ID, 
                COLUMN_UNIT_NAME, 
                COLUMN_UNIT_DESCRIPTION, 
                COLUMN_UNIT_CATEGORY, 
                COLUMN_UNIT_ICON, 
                COLUMN_UNIT_UNLOCKED, 
                COLUMN_UNIT_COMPLETED
        };
        
        String selection = COLUMN_UNIT_CATEGORY + " = ?";
        String[] selectionArgs = {category};
        
        Cursor cursor = db.query(TABLE_UNITS, columns, selection, selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_DESCRIPTION));
                int iconResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_ICON));
                boolean unlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_UNLOCKED)) == 1;
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_COMPLETED)) == 1;
                
                Unit unit = new Unit(id, name, description, category, iconResId, unlocked, completed);
                
                // Get lessons for this unit
                List<Lesson> lessons = getLessonsByUnitId(id);
                unit.setLessons(lessons);
                
                unitList.add(unit);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return unitList;
    }
    
    public List<Lesson> getLessonsByUnitId(int unitId) {
        List<Lesson> lessonList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
                COLUMN_LESSON_ID, 
                COLUMN_LESSON_NAME, 
                COLUMN_LESSON_DESCRIPTION, 
                COLUMN_LESSON_UNIT_ID, 
                COLUMN_LESSON_ICON, 
                COLUMN_LESSON_UNLOCKED, 
                COLUMN_LESSON_COMPLETED,
                COLUMN_LESSON_TYPE
        };
        
        String selection = COLUMN_LESSON_UNIT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(unitId)};
        
        Cursor cursor = db.query(TABLE_LESSONS, columns, selection, selectionArgs, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LESSON_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LESSON_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LESSON_DESCRIPTION));
                int iconResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LESSON_ICON));
                boolean unlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LESSON_UNLOCKED)) == 1;
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LESSON_COMPLETED)) == 1;
                String lessonType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LESSON_TYPE));
                
                Lesson lesson = new Lesson(id, name, description, unitId, iconResId, unlocked, completed, lessonType);
                lessonList.add(lesson);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return lessonList;
    }
    
    public void updateUnitProgress(int unitId, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UNIT_COMPLETED, completed ? 1 : 0);
        
        String whereClause = COLUMN_UNIT_ID + " = ?";
        String[] whereArgs = {String.valueOf(unitId)};
        
        db.update(TABLE_UNITS, values, whereClause, whereArgs);
        
        // If unit is completed, get its category and unlock the next unit in sequence
        if (completed) {
            String categoryQuery = "SELECT " + COLUMN_UNIT_CATEGORY + 
                                " FROM " + TABLE_UNITS + 
                                " WHERE " + COLUMN_UNIT_ID + " = ?";
            
            Cursor categoryCursor = db.rawQuery(categoryQuery, new String[]{String.valueOf(unitId)});
            if (categoryCursor.moveToFirst()) {
                String category = categoryCursor.getString(0);
                
                // Unlock the next unit in this category
                System.out.println("DEBUG: Unlocking next unit in category: " + category);
                unlockNextUnit(category, unitId);
            }
            categoryCursor.close();
        }
        
        db.close();
    }
    
    public void updateLessonProgress(int lessonId, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_COMPLETED, completed ? 1 : 0);
        
        String whereClause = COLUMN_LESSON_ID + " = ?";
        String[] whereArgs = {String.valueOf(lessonId)};
        
        int rows = db.update(TABLE_LESSONS, values, whereClause, whereArgs);
        System.out.println("DEBUG: updateLessonProgress - Updated " + rows + " rows for lessonId: " + lessonId + ", completed: " + completed);
        
        // If the lesson is marked as completed, check if we need to unlock the next lesson
        if (completed) {
            // Get the unit ID for this lesson
            String unitQuery = "SELECT " + COLUMN_LESSON_UNIT_ID + " FROM " + TABLE_LESSONS + 
                             " WHERE " + COLUMN_LESSON_ID + " = ?";
            
            Cursor unitCursor = db.rawQuery(unitQuery, new String[]{String.valueOf(lessonId)});
            if (unitCursor.moveToFirst()) {
                int unitId = unitCursor.getInt(0);
                
                // Find the next lesson in the unit
                int nextLessonId = getNextLessonId(lessonId);
                if (nextLessonId != -1) {
                    // Unlock the next lesson
                    updateLessonUnlockStatus(nextLessonId, true);
                    System.out.println("DEBUG: Auto-unlocked next lesson: " + nextLessonId + " after completing: " + lessonId);
                }
                
                // Check if this was the last lesson in the unit
                String lessonsQuery = "SELECT COUNT(*) AS total, " +
                                   "SUM(CASE WHEN " + COLUMN_LESSON_COMPLETED + " = 1 THEN 1 ELSE 0 END) AS completed " +
                                   "FROM " + TABLE_LESSONS + 
                                   " WHERE " + COLUMN_LESSON_UNIT_ID + " = ?";
                
                Cursor lessonsCursor = db.rawQuery(lessonsQuery, new String[]{String.valueOf(unitId)});
                if (lessonsCursor.moveToFirst()) {
                    int totalLessons = lessonsCursor.getInt(0);
                    int completedLessons = lessonsCursor.getInt(1);
                    
                    // If all lessons are completed, mark the unit as completed
                    if (totalLessons > 0 && totalLessons == completedLessons) {
                        System.out.println("DEBUG: All lessons completed in unit: " + unitId + ", marking as completed");
                        updateUnitProgress(unitId, true);
                    }
                }
                lessonsCursor.close();
            }
            unitCursor.close();
        }
        
        db.close();
    }
    
    public void unlockNextUnit(String category, int currentUnitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Find the next unit in sequence
        String selectNextUnitQuery = "SELECT " + COLUMN_UNIT_ID + ", " + COLUMN_UNIT_UNLOCKED + 
                " FROM " + TABLE_UNITS +
                " WHERE " + COLUMN_UNIT_CATEGORY + " = ? AND " + COLUMN_UNIT_ID + " > ? " +
                " ORDER BY " + COLUMN_UNIT_ID + " ASC LIMIT 1";
        
        String[] selectionArgs = {category, String.valueOf(currentUnitId)};
        Cursor cursor = db.rawQuery(selectNextUnitQuery, selectionArgs);
        
        if (cursor.moveToFirst()) {
            int nextUnitId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_ID));
            boolean isAlreadyUnlocked = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNIT_UNLOCKED)) == 1;
            
            if (!isAlreadyUnlocked) {
                // Only unlock if not already unlocked
                ContentValues values = new ContentValues();
                values.put(COLUMN_UNIT_UNLOCKED, 1);
                
                String whereClause = COLUMN_UNIT_ID + " = ?";
                String[] whereArgs = {String.valueOf(nextUnitId)};
                
                // Unlock the next unit
                int updatedRows = db.update(TABLE_UNITS, values, whereClause, whereArgs);
                System.out.println("DEBUG: Unlocked next unit ID: " + nextUnitId + " (updated " + updatedRows + " rows)");
                
                // Also unlock the first lesson in the newly unlocked unit
                String firstLessonQuery = "SELECT " + COLUMN_LESSON_ID + " FROM " + TABLE_LESSONS +
                        " WHERE " + COLUMN_LESSON_UNIT_ID + " = ? " +
                        " ORDER BY " + COLUMN_LESSON_ID + " ASC LIMIT 1";
                
                Cursor lessonCursor = db.rawQuery(firstLessonQuery, new String[]{String.valueOf(nextUnitId)});
                if (lessonCursor.moveToFirst()) {
                    int firstLessonId = lessonCursor.getInt(lessonCursor.getColumnIndexOrThrow(COLUMN_LESSON_ID));
                    
                    // Unlock the first lesson
                    ContentValues lessonValues = new ContentValues();
                    lessonValues.put(COLUMN_LESSON_UNLOCKED, 1);
                    
                    String lessonWhereClause = COLUMN_LESSON_ID + " = ?";
                    String[] lessonWhereArgs = {String.valueOf(firstLessonId)};
                    
                    int lessonUpdatedRows = db.update(TABLE_LESSONS, lessonValues, lessonWhereClause, lessonWhereArgs);
                    System.out.println("DEBUG: Unlocked first lesson ID: " + firstLessonId + " of unit: " + nextUnitId + 
                                     " (updated " + lessonUpdatedRows + " rows)");
                }
                lessonCursor.close();
            } else {
                System.out.println("DEBUG: Next unit ID: " + nextUnitId + " is already unlocked");
            }
        } else {
            System.out.println("DEBUG: No next unit found for category: " + category + " after unit: " + currentUnitId);
        }
        
        cursor.close();
        db.close();
    }
    
    public void unlockNextLesson(int unitId, int currentLessonId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_UNLOCKED, 1);
        
        // Find the next lesson in sequence
        String selectNextLessonQuery = "SELECT " + COLUMN_LESSON_ID + " FROM " + TABLE_LESSONS +
                " WHERE " + COLUMN_LESSON_UNIT_ID + " = ? AND " + COLUMN_LESSON_ID + " > ? " +
                " ORDER BY " + COLUMN_LESSON_ID + " ASC LIMIT 1";
        
        String[] selectionArgs = {String.valueOf(unitId), String.valueOf(currentLessonId)};
        Cursor cursor = db.rawQuery(selectNextLessonQuery, selectionArgs);
        
        if (cursor.moveToFirst()) {
            int nextLessonId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LESSON_ID));
            String whereClause = COLUMN_LESSON_ID + " = ?";
            String[] whereArgs = {String.valueOf(nextLessonId)};
            
            db.update(TABLE_LESSONS, values, whereClause, whereArgs);
        }
        
        cursor.close();
        db.close();
    }

    public int getLessonIdByNameAndCategory(String lessonName, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        int lessonId = -1;
        
        String query = "SELECT l." + COLUMN_LESSON_ID + " FROM " + TABLE_LESSONS + " l " +
                       "JOIN " + TABLE_UNITS + " u ON l." + COLUMN_LESSON_UNIT_ID + " = u." + COLUMN_UNIT_ID + " " +
                       "WHERE l." + COLUMN_LESSON_NAME + " = ? AND u." + COLUMN_UNIT_CATEGORY + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{lessonName, category});
        
        if (cursor.moveToFirst()) {
            lessonId = cursor.getInt(0);
        }
        
        cursor.close();
        db.close();
        return lessonId;
    }
    
    public int getNextLessonId(int currentLessonId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int nextLessonId = -1;
        
        // Get the unit ID for the current lesson
        String unitQuery = "SELECT " + COLUMN_LESSON_UNIT_ID + " FROM " + TABLE_LESSONS + 
                          " WHERE " + COLUMN_LESSON_ID + " = ?";
        Cursor unitCursor = db.rawQuery(unitQuery, new String[]{String.valueOf(currentLessonId)});
        
        if (unitCursor.moveToFirst()) {
            int unitId = unitCursor.getInt(0);
            System.out.println("DEBUG: getNextLessonId - Found unitId: " + unitId + " for lessonId: " + currentLessonId);
            unitCursor.close();
            
            // Get the next lesson in the same unit
            String nextLessonQuery = "SELECT " + COLUMN_LESSON_ID + " FROM " + TABLE_LESSONS + 
                                    " WHERE " + COLUMN_LESSON_UNIT_ID + " = ? AND " + 
                                    COLUMN_LESSON_ID + " > ? ORDER BY " + COLUMN_LESSON_ID + 
                                    " ASC LIMIT 1";
            Cursor nextLessonCursor = db.rawQuery(nextLessonQuery, 
                                                 new String[]{String.valueOf(unitId), 
                                                              String.valueOf(currentLessonId)});
            
            if (nextLessonCursor.moveToFirst()) {
                nextLessonId = nextLessonCursor.getInt(0);
                System.out.println("DEBUG: getNextLessonId - Found next lessonId: " + nextLessonId);
            } else {
                System.out.println("DEBUG: getNextLessonId - No next lesson found in unitId: " + unitId);
            }
            nextLessonCursor.close();
        } else {
            System.out.println("DEBUG: getNextLessonId - No unit found for lessonId: " + currentLessonId);
            unitCursor.close();
        }
        
        db.close();
        return nextLessonId;
    }
    
    public void updateLessonUnlockStatus(int lessonId, boolean unlocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LESSON_UNLOCKED, unlocked ? 1 : 0);
        
        String whereClause = COLUMN_LESSON_ID + " = ?";
        String[] whereArgs = {String.valueOf(lessonId)};
        
        int rows = db.update(TABLE_LESSONS, values, whereClause, whereArgs);
        System.out.println("DEBUG: updateLessonUnlockStatus - Updated " + rows + " rows for lessonId: " + lessonId + ", unlocked: " + unlocked);
        db.close();
    }
    
    /**
     * Checks if all lessons in a unit are completed, marks the unit as completed if they are,
     * and unlocks the next unit. Use this method to ensure proper progression through units.
     * @param unitId The ID of the unit to check
     * @return true if the unit was completed and the next unit was unlocked, false otherwise
     */
    public boolean checkAndUnlockNextUnit(int unitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean unitCompleted = false;
        
        // Check if all lessons are completed in this unit
        String lessonsQuery = "SELECT COUNT(*) AS total, " +
                           "SUM(CASE WHEN " + COLUMN_LESSON_COMPLETED + " = 1 THEN 1 ELSE 0 END) AS completed " +
                           "FROM " + TABLE_LESSONS + 
                           " WHERE " + COLUMN_LESSON_UNIT_ID + " = ?";
        
        Cursor lessonsCursor = db.rawQuery(lessonsQuery, new String[]{String.valueOf(unitId)});
        if (lessonsCursor.moveToFirst()) {
            int totalLessons = lessonsCursor.getInt(0);
            int completedLessons = lessonsCursor.getInt(1);
            
            System.out.println("DEBUG: CheckAndUnlockNextUnit - Unit: " + unitId + " has " + 
                              completedLessons + " completed lessons out of " + totalLessons);
            
            // If all lessons are completed, mark the unit as completed and unlock next unit
            if (totalLessons > 0 && totalLessons == completedLessons) {
                // Get the current completion status of the unit
                String unitQuery = "SELECT " + COLUMN_UNIT_COMPLETED + ", " + COLUMN_UNIT_CATEGORY +
                                  " FROM " + TABLE_UNITS + 
                                  " WHERE " + COLUMN_UNIT_ID + " = ?";
                
                Cursor unitCursor = db.rawQuery(unitQuery, new String[]{String.valueOf(unitId)});
                if (unitCursor.moveToFirst()) {
                    boolean isAlreadyCompleted = unitCursor.getInt(0) == 1;
                    String category = unitCursor.getString(1);
                    
                    // Mark unit as completed if not already completed
                    if (!isAlreadyCompleted) {
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_UNIT_COMPLETED, 1);
                        
                        String whereClause = COLUMN_UNIT_ID + " = ?";
                        String[] whereArgs = {String.valueOf(unitId)};
                        
                        db.update(TABLE_UNITS, values, whereClause, whereArgs);
                        System.out.println("DEBUG: Marked unit " + unitId + " as completed");
                    }
                    
                    // Unlock the next unit in this category
                    System.out.println("DEBUG: Unlocking next unit in category: " + category + " after unit: " + unitId);
                    unlockNextUnit(category, unitId);
                    unitCompleted = true;
                }
                unitCursor.close();
            }
        }
        lessonsCursor.close();
        db.close();
        
        return unitCompleted;
    }
    
    /**
     * Updates a unit's unlock status
     * @param unitId The ID of the unit to update
     * @param unlocked The new unlock status
     * @return Number of rows affected (should be 1 if successful)
     */
    public int updateUnitUnlockStatus(int unitId, boolean unlocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UNIT_UNLOCKED, unlocked ? 1 : 0);
        
        String whereClause = COLUMN_UNIT_ID + " = ?";
        String[] whereArgs = {String.valueOf(unitId)};
        
        int updatedRows = db.update(TABLE_UNITS, values, whereClause, whereArgs);
        System.out.println("DEBUG: updateUnitUnlockStatus - Updated " + updatedRows + 
                           " rows for unitId: " + unitId + ", unlocked: " + unlocked);
        
        // If we're unlocking a unit, also unlock the first lesson
        if (unlocked && updatedRows > 0) {
            // Get the first lesson in this unit
            String firstLessonQuery = "SELECT " + COLUMN_LESSON_ID + " FROM " + TABLE_LESSONS +
                    " WHERE " + COLUMN_LESSON_UNIT_ID + " = ? " +
                    " ORDER BY " + COLUMN_LESSON_ID + " ASC LIMIT 1";
            
            Cursor lessonCursor = db.rawQuery(firstLessonQuery, new String[]{String.valueOf(unitId)});
            if (lessonCursor.moveToFirst()) {
                int firstLessonId = lessonCursor.getInt(lessonCursor.getColumnIndexOrThrow(COLUMN_LESSON_ID));
                updateLessonUnlockStatus(firstLessonId, true);
            }
            lessonCursor.close();
        }
        
        db.close();
        return updatedRows;
    }
    
    /**
     * Adds vocabulary items for the next units in each category (Transportation, Drinks, Formal Greetings)
     * Call this method to ensure content is available for newly unlocked units
     */
    public void addNextUnitsVocabulary() {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Check if we already added the next units vocabulary (to avoid duplicates)
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VOCABULARY + 
                                   " WHERE " + COLUMN_DUTCH_WORD + " = 'Afspraak'", null);
        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
            cursor.close();
            db.close();
            return; // Already added
        }
        cursor.close();
        
        // Add vocabulary for Travel - Transportation unit
        addVocabularyItem(db, "Travel", "Snelweg", "Highway");
        addVocabularyItem(db, "Travel", "Afslag", "Exit (road)");
        addVocabularyItem(db, "Travel", "Rotonde", "Roundabout");
        addVocabularyItem(db, "Travel", "Stoplicht", "Traffic light");
        addVocabularyItem(db, "Travel", "Zebrapad", "Pedestrian crossing");
        addVocabularyItem(db, "Travel", "Rijbewijs", "Driver's license");
        addVocabularyItem(db, "Travel", "Tankstation", "Gas station");
        addVocabularyItem(db, "Travel", "Parkeerplaats", "Parking spot");
        addVocabularyItem(db, "Travel", "Fietsenrek", "Bike rack");
        addVocabularyItem(db, "Travel", "Fietsenmaker", "Bike repair shop");
        addVocabularyItem(db, "Travel", "OV-chipkaart", "Public transport card");
        addVocabularyItem(db, "Travel", "Vertraging", "Delay");
        addVocabularyItem(db, "Travel", "Aankomst", "Arrival");
        addVocabularyItem(db, "Travel", "Vertrek", "Departure");
        addVocabularyItem(db, "Travel", "Perron", "Platform");
        
        // Add vocabulary for Food - Drinks unit
        addVocabularyItem(db, "Food", "Bier", "Beer");
        addVocabularyItem(db, "Food", "Wijn", "Wine");
        addVocabularyItem(db, "Food", "Sap", "Juice");
        addVocabularyItem(db, "Food", "Frisdrank", "Soft drink");
        addVocabularyItem(db, "Food", "Sinaasappelsap", "Orange juice");
        addVocabularyItem(db, "Food", "Appelsap", "Apple juice");
        addVocabularyItem(db, "Food", "Warme chocolademelk", "Hot chocolate");
        addVocabularyItem(db, "Food", "Limonade", "Lemonade");
        addVocabularyItem(db, "Food", "Spa rood", "Sparkling water");
        addVocabularyItem(db, "Food", "Spa blauw", "Still water");
        addVocabularyItem(db, "Food", "Glas", "Glass");
        addVocabularyItem(db, "Food", "Kopje", "Cup");
        addVocabularyItem(db, "Food", "Dorst", "Thirsty");
        addVocabularyItem(db, "Food", "Proost", "Cheers");
        addVocabularyItem(db, "Food", "IJsblokje", "Ice cube");
        
        // Add vocabulary for Greetings - Formal Greetings unit
        addVocabularyItem(db, "Greetings", "Geachte", "Dear (formal)");
        addVocabularyItem(db, "Greetings", "Meneer", "Sir/Mr.");
        addVocabularyItem(db, "Greetings", "Mevrouw", "Madam/Mrs.");
        addVocabularyItem(db, "Greetings", "Met vriendelijke groet", "Kind regards");
        addVocabularyItem(db, "Greetings", "Hoogachtend", "Yours sincerely");
        addVocabularyItem(db, "Greetings", "Afspraak", "Appointment");
        addVocabularyItem(db, "Greetings", "Vergadering", "Meeting");
        addVocabularyItem(db, "Greetings", "Collega", "Colleague");
        addVocabularyItem(db, "Greetings", "Kennismaken", "To get acquainted");
        addVocabularyItem(db, "Greetings", "Visitekaartje", "Business card");
        addVocabularyItem(db, "Greetings", "Arbeidscontract", "Employment contract");
        addVocabularyItem(db, "Greetings", "Sollicitatiegesprek", "Job interview");
        addVocabularyItem(db, "Greetings", "Hoe maakt u het?", "How are you? (formal)");
        addVocabularyItem(db, "Greetings", "Aangenaam kennis te maken", "Pleased to meet you (formal)");
        addVocabularyItem(db, "Greetings", "Tot ziens", "Goodbye (formal)");
        
        // Add the lessons for the next units
        
        // Travel - Transportation unit (unit ID 2)
        // Check if lessons already exist
        Cursor transportationCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LESSONS + 
                                                  " WHERE " + COLUMN_LESSON_UNIT_ID + " = 2", null);
        if (transportationCursor.moveToFirst() && transportationCursor.getInt(0) == 0) {
            addLesson(db, "Transportation Vocabulary", "Learn vocabulary for different transportation methods", 2, 
                      R.drawable.ic_travel, 1, 0, "VOCAB");
            addLesson(db, "Transportation Quiz", "Test your knowledge of transportation terms", 2, 
                      R.drawable.ic_travel, 0, 0, "QUIZ");
            addLesson(db, "Transportation Sentences", "Practice sentences about transportation", 2, 
                      R.drawable.ic_travel, 0, 0, "SENTENCE");
        }
        transportationCursor.close();
        
        // Food - Drinks unit (unit ID 6)
        Cursor drinksCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LESSONS + 
                                          " WHERE " + COLUMN_LESSON_UNIT_ID + " = 6", null);
        if (drinksCursor.moveToFirst() && drinksCursor.getInt(0) == 0) {
            addLesson(db, "Drinks Vocabulary", "Learn vocabulary for different beverages", 6, 
                      R.drawable.ic_food, 1, 0, "VOCAB");
            addLesson(db, "Drinks Quiz", "Test your knowledge of drink terms", 6, 
                      R.drawable.ic_food, 0, 0, "QUIZ");
            addLesson(db, "Drinks Sentences", "Practice sentences about drinks", 6, 
                      R.drawable.ic_food, 0, 0, "SENTENCE");
        }
        drinksCursor.close();
        
        // Greetings - Formal Greetings unit (unit ID 10)
        Cursor formalGreetingsCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_LESSONS + 
                                                    " WHERE " + COLUMN_LESSON_UNIT_ID + " = 10", null);
        if (formalGreetingsCursor.moveToFirst() && formalGreetingsCursor.getInt(0) == 0) {
            addLesson(db, "Formal Greetings Vocabulary", "Learn formal greeting vocabulary", 10, 
                      R.drawable.ic_greetings, 1, 0, "VOCAB");
            addLesson(db, "Formal Greetings Quiz", "Test your knowledge of formal greetings", 10, 
                      R.drawable.ic_greetings, 0, 0, "QUIZ");
            addLesson(db, "Formal Greetings Sentences", "Practice sentences with formal greetings", 10, 
                      R.drawable.ic_greetings, 0, 0, "SENTENCE");
        }
        formalGreetingsCursor.close();
        
        db.close();
    }
} 