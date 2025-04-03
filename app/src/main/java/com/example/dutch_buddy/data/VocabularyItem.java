package com.example.dutch_buddy.data;

public class VocabularyItem {
    private int id;
    private String category;
    private String dutchWord;
    private String englishTranslation;

    public VocabularyItem(int id, String category, String dutchWord, String englishTranslation) {
        this.id = id;
        this.category = category;
        this.dutchWord = dutchWord;
        this.englishTranslation = englishTranslation;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getDutchWord() {
        return dutchWord;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    @Override
    public String toString() {
        return dutchWord + " - " + englishTranslation;
    }
} 