package com.example.dutch_buddy.data;

public class SentenceExercise {
    private String sentence;
    private String missingWord;
    private String hint;

    public SentenceExercise(String sentence, String missingWord, String hint) {
        this.sentence = sentence;
        this.missingWord = missingWord;
        this.hint = hint;
    }

    public String getSentence() {
        return sentence;
    }

    public String getMissingWord() {
        return missingWord;
    }

    public String getHint() {
        return hint;
    }
} 