package com.example.dutch_buddy.data;

public class Category {
    private String name;
    private int iconResId;

    public Category(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
} 