package com.example.dutch_buddy.data;

public class Category {
    private String name;
    private int iconResId;
    private String description;
    private int unitCount;

    public Category(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
        this.description = "";
        this.unitCount = 0;
    }

    public Category(String name, int iconResId, String description, int unitCount) {
        this.name = name;
        this.iconResId = iconResId;
        this.description = description;
        this.unitCount = unitCount;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getUnitCount() {
        return unitCount;
    }
    
    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }
} 