package com.example.findmydoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();

        Button resetButton = findViewById(R.id.ResetPass);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email address from the EditText
                EditText emailEditText = findViewById(R.id.ForgotEmail);
                String emailAddress = emailEditText.getText().toString().trim();

                // Check if the email address is empty or invalid
                if (TextUtils.isEmpty(emailAddress) || !isValidEmail(emailAddress) ) {
                    // Show an error message for an empty or invalid email
                    TextView errorTextView = findViewById(R.id.errorTextView);
                    errorTextView.setText("Please enter a valid email address.");
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    // Send a password reset email
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password reset email sent successfully
                                        // Show a success message or handle the UI as needed
                                        TextView hintTextView = findViewById(R.id.hint);
                                        hintTextView.setVisibility(View.VISIBLE);

                                        // Hide the error message if it was previously shown
                                        TextView errorTextView = findViewById(R.id.errorTextView);
                                        errorTextView.setVisibility(View.GONE);
                                    } else {
                                        // Password reset email failed
                                        // Show an error message and handle the UI
                                        TextView errorTextView = findViewById(R.id.errorTextView);
                                        errorTextView.setText("Password reset email failed. Please try again later.");
                                        errorTextView.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
            }
        });

        Button Closee = findViewById(R.id.ForgotClose);

        Closee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the ForgotPassword activity
                Intent intent = new Intent(ForgotPassword.this, SignIn.class);

                // Start the ForgotPassword activity
                startActivity(intent);
            }
        });


    }
    // Function to validate email address
    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}