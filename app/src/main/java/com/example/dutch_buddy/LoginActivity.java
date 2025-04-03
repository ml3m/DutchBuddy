package com.example.dutch_buddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dutch_buddy.adapters.UserAdapter;
import com.example.dutch_buddy.data.DatabaseHelper;
import com.example.dutch_buddy.data.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements UserAdapter.OnUserClickListener {

    private static final String PREFS_NAME = "DutchBuddyPrefs";
    private static final String KEY_USER_ID = "current_user_id";

    private RecyclerView usersRecyclerView;
    private FloatingActionButton addUserButton;
    private UserAdapter userAdapter;
    private DatabaseHelper databaseHelper;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        addUserButton = findViewById(R.id.addUserButton);

        // Initialize database helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // Set up add user button
        addUserButton.setOnClickListener(v -> showCreateUserDialog());

        // Check if a user is already logged in
        int currentUserId = getSavedUserId();
        if (currentUserId != -1) {
            // User already logged in, go to main activity
            User currentUser = databaseHelper.getUser(currentUserId);
            if (currentUser != null) {
                launchMainActivity(currentUser);
                return;
            }
        }

        // Load user list
        loadUsers();
    }

    private void loadUsers() {
        // Get users from database
        userList = databaseHelper.getAllUsers();

        // Set up RecyclerView
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList, this);
        usersRecyclerView.setAdapter(userAdapter);

        // Show create user dialog if no users exist
        if (userList.isEmpty()) {
            showCreateUserDialog();
        }
    }

    private void showCreateUserDialog() {
        // Create dialog for adding a new user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_user, null);
        EditText usernameInput = dialogView.findViewById(R.id.usernameInput);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(userList.size() > 0); // Only allow cancel if users exist

        confirmButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                usernameInput.setError("Please enter a username");
                return;
            }

            // Check if username already exists
            User existingUser = databaseHelper.getUserByUsername(username);
            if (existingUser != null) {
                usernameInput.setError("Username already exists");
                return;
            }

            // Create new user
            long userId = databaseHelper.createUser(username);
            if (userId != -1) {
                // User created successfully
                User newUser = databaseHelper.getUser((int) userId);
                saveUserId((int) userId);
                
                // Reload users or launch main activity
                dialog.dismiss();
                launchMainActivity(newUser);
            } else {
                Toast.makeText(LoginActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onUserClick(User user) {
        // Save selected user ID and launch main activity
        saveUserId(user.getId());
        launchMainActivity(user);
    }

    private void launchMainActivity(User user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("USER_ID", user.getId());
        intent.putExtra("USERNAME", user.getUsername());
        startActivity(intent);
        finish();
    }

    private void saveUserId(int userId) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    private int getSavedUserId() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getInt(KEY_USER_ID, -1);
    }
} 