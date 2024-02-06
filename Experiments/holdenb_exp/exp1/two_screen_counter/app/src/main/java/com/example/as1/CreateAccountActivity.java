package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class CreateAccountActivity extends AppCompatActivity {
    EditText editTextNewEmail;
    EditText editTextNewUsername;
    EditText editTextNewPassword;
    Button buttonCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);

        editTextNewEmail = findViewById(R.id.editTextNewEmail);
        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateNewAccount);

        buttonCreateNewAccount.setOnClickListener(v -> {
            String email = editTextNewEmail.getText().toString();
            String username = editTextNewUsername.getText().toString();
            String password = editTextNewPassword.getText().toString();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(CreateAccountActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateAccountActivity.this, "Account Created\nUsername: " + username + "\nPassword: " + password, Toast.LENGTH_SHORT).show();
                // Here you can add the code to send the create account request to the backend

                // Create an Intent to start the MapActivity
                Intent intent = new Intent(CreateAccountActivity.this, MapActivity.class);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
