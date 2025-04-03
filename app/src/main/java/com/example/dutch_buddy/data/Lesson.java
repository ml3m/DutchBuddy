package com.example.dutch_buddy.data;

public class Lesson {
    private int id;
    private String name;
    private String description;
    private int unitId;
    private int iconResId;
    private boolean unlocked;
    private boolean completed;
    private String lessonType; // "VOCAB", "QUIZ", "SENTENCE"

    public Lesson(int id, String name, String description, int unitId, int iconResId, boolean unlocked, boolean completed, String lessonType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitId = unitId;
        this.iconResId = iconResId;
        this.unlocked = unlocked;
        this.completed = completed;
        this.lessonType = lessonType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }
} 