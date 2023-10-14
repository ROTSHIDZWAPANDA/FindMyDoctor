package com.example.findmydoctor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.findmydoctor.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = (binding.loginEmail).getText().toString();
                String password = (binding.loginPassword).getText().toString();

                // Check if email and password are empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Show a message to the user to input values
                    Toast.makeText(SignIn.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                    return; // Return without attempting to sign in
                }



                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    handleUserLogin(user);
                                } else {
                                    // Login failed
                                    Toast.makeText(SignIn.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        TextView forgotPasswordTextView = findViewById(R.id.forgotPassword);

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the ForgotPassword activity
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);

                // Start the ForgotPassword activity
                startActivity(intent);
            }
        });
    }

    private void handleUserLogin(FirebaseUser user) {
        DatabaseReference userRef = mDatabase.child("users").child(user.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if (role != null) {
                        navigateToRoleHomeScreen(role);
                    } else {
                        Toast.makeText(SignIn.this, "Signup failed: " , Toast.LENGTH_SHORT).show();
                        // Handle missing role
                    }
                } else {
                    Toast.makeText(SignIn.this, "Signup failed2: " , Toast.LENGTH_SHORT).show();
                    // Handle missing user data
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    private void navigateToRoleHomeScreen(String role) {
        switch (role) {
            case "Patient":
                Intent intent = new Intent(SignIn.this, HomePageStructure.class);
                startActivity(intent);
                break;
            case "Doctor":
                Intent intent1 = new Intent(SignIn.this, DoctorHomepageStructure.class);
                startActivity(intent1);
                break;
            // Add cases for other roles if needed
            default:
                // Handle other roles or cases
                break;
        }
        finish(); // Close the login activity
        
    }
}