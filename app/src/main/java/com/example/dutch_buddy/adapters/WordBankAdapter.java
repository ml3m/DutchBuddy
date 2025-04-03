package com.example.dutch_buddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class WordBankAdapter extends RecyclerView.Adapter<WordBankAdapter.WordViewHolder> {

    private List<String> words;
    private OnWordClickListener listener;

    public interface OnWordClickListener {
        void onWordClick(String word);
    }

    public WordBankAdapter(List<String> words, OnWordClickListener listener) {
        this.words = words;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_chip, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String word = words.get(position);
        holder.wordChip.setText(word);
        
        holder.wordChip.setOnClickListener(v -> {
            if (listener != null) {
                listener.onWordClick(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {
        Chip wordChip;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            wordChip = (Chip) itemView;
        }
    }
} 