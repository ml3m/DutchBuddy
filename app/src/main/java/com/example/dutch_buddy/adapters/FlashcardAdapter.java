package com.example.dutch_buddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.R;
import com.example.dutch_buddy.data.VocabularyItem;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private Context context;
    private List<VocabularyItem> vocabularyItems;

    public FlashcardAdapter(Context context, List<VocabularyItem> vocabularyItems) {
        this.context = context;
        this.vocabularyItems = vocabularyItems;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        VocabularyItem item = vocabularyItems.get(position);
        
        // Set the Dutch word as the main text
        holder.flashcardWord.setText(item.getDutchWord());
        holder.flashcardTranslation.setText(item.getEnglishTranslation());
        
        // Initially hide the translation
        holder.flashcardTranslation.setVisibility(View.GONE);
        
        // Set background color for the card
        holder.flashcardCardView.setCardBackgroundColor(
                context.getResources().getColor(R.color.surface, null));
        
        // Set up click listener to flip the card
        holder.flashcardCardView.setOnClickListener(v -> {
            if (holder.flashcardTranslation.getVisibility() == View.GONE) {
                // Show translation
                holder.flashcardTranslation.setVisibility(View.VISIBLE);
                // Change background color to indicate flipped state
                holder.flashcardCardView.setCardBackgroundColor(
                        context.getResources().getColor(R.color.sage_green, null));
            } else {
                // Hide translation
                holder.flashcardTranslation.setVisibility(View.GONE);
                // Change background color back to normal
                holder.flashcardCardView.setCardBackgroundColor(
                        context.getResources().getColor(R.color.surface, null));
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocabularyItems.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        CardView flashcardCardView;
        TextView flashcardWord;
        TextView flashcardTranslation;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            flashcardCardView = itemView.findViewById(R.id.flashcardCardView);
            flashcardWord = itemView.findViewById(R.id.flashcardWord);
            flashcardTranslation = itemView.findViewById(R.id.flashcardTranslation);
        }
    }
} 