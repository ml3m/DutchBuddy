package com.example.dutch_buddy.data;

public class User {
    private int id;
    private String username;
    private int level;
    private int experiencePoints;
    private int totalQuizzesTaken;
    private int totalCorrectAnswers;
    private int totalQuestions;
    private long lastLoginDate;
    private int dailyStreak;

    public User() {
        // Default constructor
    }

    public User(int id, String username, int level, int experiencePoints, 
                int totalQuizzesTaken, int totalCorrectAnswers, int totalQuestions,
                long lastLoginDate, int dailyStreak) {
        this.id = id;
        this.username = username;
        this.level = level;
        this.experiencePoints = experiencePoints;
        this.totalQuizzesTaken = totalQuizzesTaken;
        this.totalCorrectAnswers = totalCorrectAnswers;
        this.totalQuestions = totalQuestions;
        this.lastLoginDate = lastLoginDate;
        this.dailyStreak = dailyStreak;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public int getTotalQuizzesTaken() {
        return totalQuizzesTaken;
    }

    public void setTotalQuizzesTaken(int totalQuizzesTaken) {
        this.totalQuizzesTaken = totalQuizzesTaken;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(int totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getAccuracy() {
        if (totalQuestions == 0) {
            return 0;
        }
        return (double) totalCorrectAnswers / totalQuestions * 100;
    }

    public long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public int getDailyStreak() {
        return dailyStreak;
    }

    public void setDailyStreak(int dailyStreak) {
        this.dailyStreak = dailyStreak;
    }
    
    // Add XP and update level if needed
    public boolean addExperiencePoints(int points) {
        boolean leveledUp = false;
        this.experiencePoints += points;
        
        // Calculate new level based on XP (100 XP per level)
        int newLevel = this.experiencePoints / 100 + 1;
        if (newLevel > this.level) {
            this.level = newLevel;
            leveledUp = true;
        }
        
        return leveledUp;
    }
} 