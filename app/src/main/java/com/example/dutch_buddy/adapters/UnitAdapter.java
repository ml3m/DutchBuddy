package com.example.dutch_buddy.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutch_buddy.CompleteSentenceActivity;
import com.example.dutch_buddy.FlashcardsActivity;
import com.example.dutch_buddy.QuizActivity;
import com.example.dutch_buddy.R;
import com.example.dutch_buddy.data.Lesson;
import com.example.dutch_buddy.data.Unit;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {

    private Context context;
    private List<Unit> unitList;
    private int userId;
    private String categoryName;

    public UnitAdapter(Context context, List<Unit> unitList, int userId, String categoryName) {
        this.context = context;
        this.unitList = unitList;
        this.userId = userId;
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit unit = unitList.get(position);
        
        holder.unitNameTextView.setText(unit.getName());
        holder.unitDescriptionTextView.setText(unit.getDescription());
        holder.unitIconImageView.setImageResource(unit.getIconResId());
        
        // Set up the appearance based on locked/unlocked status
        if (!unit.isUnlocked()) {
            // Unit is locked
            holder.unitCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.disabled_gray));
            holder.unitLockedImageView.setVisibility(View.VISIBLE);
            holder.lessonsRecyclerView.setVisibility(View.GONE);
        } else {
            // Unit is unlocked
            holder.unitCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface));
            holder.unitLockedImageView.setVisibility(View.GONE);
            holder.lessonsRecyclerView.setVisibility(View.VISIBLE);
            
            // Set up the lessons recycler view
            setupLessonsRecyclerView(holder, unit);
        }
        
        // If it's completed, add a completion marker
        if (unit.isCompleted()) {
            Drawable completedIcon = ContextCompat.getDrawable(context, R.drawable.ic_completed);
            holder.unitNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, completedIcon, null);
        } else {
            holder.unitNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void setupLessonsRecyclerView(UnitViewHolder holder, Unit unit) {
        List<Lesson> lessons = unit.getLessons();
        LessonAdapter lessonAdapter = new LessonAdapter(context, lessons, userId, categoryName);
        holder.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.lessonsRecyclerView.setAdapter(lessonAdapter);
    }

    @Override
    public int getItemCount() {
        return unitList.size();
    }

    public static class UnitViewHolder extends RecyclerView.ViewHolder {
        CardView unitCardView;
        ImageView unitIconImageView;
        TextView unitNameTextView;
        TextView unitDescriptionTextView;
        RecyclerView lessonsRecyclerView;
        ImageView unitLockedImageView;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            unitCardView = itemView.findViewById(R.id.unitCardView);
            unitIconImageView = itemView.findViewById(R.id.unitIconImageView);
            unitNameTextView = itemView.findViewById(R.id.unitNameTextView);
            unitDescriptionTextView = itemView.findViewById(R.id.unitDescriptionTextView);
            lessonsRecyclerView = itemView.findViewById(R.id.lessonsRecyclerView);
            unitLockedImageView = itemView.findViewById(R.id.unitLockedImageView);
        }
    }

    // Inner LessonAdapter class to handle lessons within a unit
    private class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

        private Context context;
        private List<Lesson> lessonList;
        private int userId;
        private String categoryName;

        public LessonAdapter(Context context, List<Lesson> lessonList, int userId, String categoryName) {
            this.context = context;
            this.lessonList = lessonList;
            this.userId = userId;
            this.categoryName = categoryName;
        }

        @NonNull
        @Override
        public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, parent, false);
            return new LessonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
            Lesson lesson = lessonList.get(position);
            
            holder.lessonNameTextView.setText(lesson.getName());
            holder.lessonIconImageView.setImageResource(lesson.getIconResId());
            
            // Set the lesson start button appearance based on lock status
            if (!lesson.isUnlocked()) {
                holder.startLessonButton.setEnabled(false);
                holder.startLessonButton.setAlpha(0.5f);
                holder.lessonLockedImageView.setVisibility(View.VISIBLE);
            } else {
                holder.startLessonButton.setEnabled(true);
                holder.startLessonButton.setAlpha(1.0f);
                holder.lessonLockedImageView.setVisibility(View.GONE);
            }
            
            // If it's completed, add a completion marker
            if (lesson.isCompleted()) {
                holder.lessonCompletedImageView.setVisibility(View.VISIBLE);
            } else {
                holder.lessonCompletedImageView.setVisibility(View.GONE);
            }
            
            // Set click listener for the start button
            holder.startLessonButton.setOnClickListener(v -> startLesson(lesson));
        }

        private void startLesson(Lesson lesson) {
            Intent intent;
            String lessonType = lesson.getLessonType();
            
            switch (lessonType) {
                case "VOCAB":
                    intent = new Intent(context, FlashcardsActivity.class);
                    break;
                case "QUIZ":
                    intent = new Intent(context, QuizActivity.class);
                    break;
                case "SENTENCE":
                    intent = new Intent(context, CompleteSentenceActivity.class);
                    break;
                default:
                    intent = new Intent(context, FlashcardsActivity.class);
                    break;
            }
            
            // Pass category name and user ID
            intent.putExtra("CATEGORY_NAME", categoryName);
            intent.putExtra("USER_ID", userId);
            intent.putExtra("LESSON_ID", lesson.getId());
            context.startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return lessonList.size();
        }

        public class LessonViewHolder extends RecyclerView.ViewHolder {
            ImageView lessonIconImageView;
            TextView lessonNameTextView;
            Button startLessonButton;
            ImageView lessonLockedImageView;
            ImageView lessonCompletedImageView;

            public LessonViewHolder(@NonNull View itemView) {
                super(itemView);
                lessonIconImageView = itemView.findViewById(R.id.lessonIconImageView);
                lessonNameTextView = itemView.findViewById(R.id.lessonNameTextView);
                startLessonButton = itemView.findViewById(R.id.startLessonButton);
                lessonLockedImageView = itemView.findViewById(R.id.lessonLockedImageView);
                lessonCompletedImageView = itemView.findViewById(R.id.lessonCompletedImageView);
            }
        }
    }
} 