package com.example.findmydoctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.findmydoctor.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Spinner signUpRoleSpinner;
    private EditText confirmPasswordEditText;




    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        confirmPasswordEditText = binding.signUpConfirmPassword;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract user input from EditText fields
                String email = (binding.signUpEmail).getText().toString();
                String firstName = (binding.signUpFirstName).getText().toString();
                String lastName = (binding.signUpLastName).getText().toString();
                String userAddress = (binding.signUpAddress).getText().toString();
                String phoneNumber = (binding.signUpPhoneNumber).getText().toString();
                String role = (binding.signUpRole).getSelectedItem().toString();
                String password = (binding.signUpPassword).getText().toString();

                boolean isFormValid = true;

                if (TextUtils.isEmpty(email)) {
                    isFormValid = false;
                    (binding.signUpEmail).setError("Email is required");
                }
                if (TextUtils.isEmpty(firstName)) {
                    isFormValid = false;
                    (binding.signUpFirstName).setError("firstName is required");
                }
                if (TextUtils.isEmpty(lastName)) {
                    isFormValid = false;
                    (binding.signUpLastName).setError("lastName is required");
                }
                if (TextUtils.isEmpty(userAddress)) {
                    isFormValid = false;
                    (binding.signUpAddress).setError("Address is required");
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    isFormValid = false;
                    (binding.signUpPhoneNumber).setError("phone Number is required");
                }



                // Repeat this for other fields

                if (role.equals("Select Role")) {
                    isFormValid = false;
                    // Set an error message for the Spinner (role)
                    ((TextView) binding.signUpRole.getSelectedView()).setError("Role is required");
                }
                if (TextUtils.isEmpty(password)) {
                    isFormValid = false;
                    (binding.signUpPassword).setError("Password is required");
                }


                //validation
                if (isPasswordValid(password)) {
                    // Continue with the signup process
                } else {
                    // Show an error message to the user and display password requirements
                    String errorMessage = "Invalid password. Please ensure that your password:\n" +
                            "- Is between 10 and 20 characters in length.\n" +
                            "- Contains at least two special characters.";


                    // Show the password requirements TextView
                    TextView passwordRequirementsTextView = findViewById(R.id.passwordRequirementsTextView);
                    passwordRequirementsTextView.setVisibility(View.VISIBLE);

                    String confirmPassword = confirmPasswordEditText.getText().toString();


                    if (!password.equals(confirmPassword)) {
                        // Show an error message to the user
                        confirmPasswordEditText.setError("Passwords do not match");
                        return; // Stop the signup process
                    }
                }
                if (isFormValid) {
                // Create user using Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        // Add user details to Realtime Database
                                        UserHelper userData = new UserHelper(firstName, lastName, userAddress,phoneNumber, role,"" );

                                        mDatabase.child("users").child(user.getUid()).setValue(userData);
                                        // TODO: Handle successful signup
                                        Toast.makeText(SignUp.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                                        // Redirect the user to the main activity or another appropriate screen
                                        startActivity(new Intent(SignUp.this, SignIn.class));
                                        finish(); // Close the signup activity
                                    } else {
                                        // TODO: Handle signup failure
                                        Toast.makeText(SignUp.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        binding.signUpAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });

        signUpRoleSpinner = findViewById(R.id.signUpRole);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.role_array, R.layout.spinner_items_layout);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        signUpRoleSpinner.setAdapter(adapter);

        signUpRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = parent.getItemAtPosition(position).toString();
                if (selectedRole.equals("Select Role")) {
                    // Show an alert
                    showRoleNotSelectedAlert();
                } else {
                    // Handle the selected role here
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });



    }

    private void showRoleNotSelectedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please select a role");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    private boolean isPasswordValid(String password) {
        // Check if the password length is more than 8 characters
        if (password.length() <= 8) {
            return false;
        }

        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        // Check if the password contains at least one number and one special character
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }

            // If both conditions are met, we can break out of the loop early
            if (hasNumber && hasSpecialChar) {
                break;
            }
        }

        // Check if both conditions are met
        return hasNumber && hasSpecialChar;
    }


}