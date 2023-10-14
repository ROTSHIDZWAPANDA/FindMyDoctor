package com.example.findmydoctor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout layoutDisplay,layoutEdit;
    private EditText EDName, EDSurname, EDPhonenumber, EDAddress;

    private TextView btnEditProfile, btnChangePassword, btnSave, btnCancel;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // Define your Firebase references
    private DatabaseReference userRef;
    private FirebaseStorage storage;
    private StorageReference profileImageRef;
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private CircleImageView profileImageEdit;
    private Uri selectedImageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    public static PatientProfileFragment newInstance(String param1, String param2) {
        PatientProfileFragment fragment = new PatientProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Handle the selected image data here
                            selectedImageUri = data.getData();
                            // Update the ImageView with the selected image
                            profileImageEdit.setImageURI(selectedImageUri);
                            // Update your logic to handle the image upload and database update
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final CircleImageView profileImageView = rootView.findViewById(R.id.profile_image);
        final TextView tvFirstName = rootView.findViewById(R.id.tvFirstName);
        final TextView tvLastName = rootView.findViewById(R.id.tvLastName);
        final TextView tvEmail = rootView.findViewById(R.id.tvEmail);
        final TextView tvAddress = rootView.findViewById(R.id.tvAddress);
        final TextView tvPhoneNumber = rootView.findViewById(R.id.tvPhonenumber);
        final TextView tvTop = rootView.findViewById(R.id.tvTop);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);
                        String email = currentUser.getEmail();
                        String address = dataSnapshot.child("userAddress").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                        String surnameName = lastName + " " + firstName;
                        // Populate the TextViews with retrieved data
                        tvFirstName.setText(firstName);
                        tvLastName.setText(lastName);
                        tvEmail.setText(email);
                        tvAddress.setText(address);
                        tvPhoneNumber.setText(phoneNumber);
                        tvTop.setText(surnameName);

                        String profilePictureUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                            // Load profile picture using Glide
                            Glide.with(PatientProfileFragment.this)
                                    .load(profilePictureUrl)
                                    .placeholder(R.drawable.doctor) // Placeholder image
                                    .into(profileImageView);
                        }
                    } catch (Exception e) {
                        // Handle exceptions here (e.g., display an error message)
                        tvFirstName.setText("Error");
                        tvLastName.setText("Error");
                        tvEmail.setText("Error");
                        tvAddress.setText("Error");
                        tvPhoneNumber.setText("Error");
                        tvTop.setText("Error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

        layoutDisplay = rootView.findViewById(R.id.LayoutDisplay);
        layoutEdit = rootView.findViewById(R.id.LayoutEdit);
        profileImageEdit = rootView.findViewById(R.id.profile_image_edit);
        EDName = rootView.findViewById(R.id.EDName);
        EDSurname = rootView.findViewById(R.id.EDSurname);
        EDPhonenumber = rootView.findViewById(R.id.EDPhonenumber);
        EDAddress = rootView.findViewById(R.id.EDAddress);
        profileImageEdit.setOnClickListener(v -> {
            chooseImageFromGalleryOrCamera();
        });
        storage = FirebaseStorage.getInstance();
        profileImageRef = storage.getReference().child("profile_images");

        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);
        btnChangePassword = rootView.findViewById(R.id.btnChangePassword);
        btnSave = rootView.findViewById(R.id.Save);
        btnCancel = rootView.findViewById(R.id.Cancel);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Fetch user data and populate EditText fields
            userRef = FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String userAddress = snapshot.child("userAddress").getValue(String.class);

                        // Populate EditText fields with user data
                        EDName.setText(firstName);
                        EDSurname.setText(lastName);
                        EDPhonenumber.setText(phoneNumber);
                        EDAddress.setText(userAddress);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        // Set click listeners for the buttons
        btnEditProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            // Set visibility for the layouts
                    layoutDisplay.setVisibility(View.GONE);
                    layoutEdit.setVisibility(View.VISIBLE);
                }
            });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set visibility for the layouts
                layoutDisplay.setVisibility(View.VISIBLE);
                layoutEdit.setVisibility(View.GONE);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    String userId = mAuth.getCurrentUser().getUid();

                    // Get values from EditText fields
                    String newFirstName = EDName.getText().toString();
                    String newLastName = EDSurname.getText().toString();
                    String newPhoneNumber = EDPhonenumber.getText().toString();
                    String newUserAddress = EDAddress.getText().toString();

                    // Update user data in Firebase Realtime Database
                    userRef.child("firstName").setValue(newFirstName);
                    userRef.child("lastName").setValue(newLastName);
                    userRef.child("phoneNumber").setValue(newPhoneNumber);
                    userRef.child("userAddress").setValue(newUserAddress);

                    // Upload new profile picture to Firebase Storage
                    if (selectedImageUri != null) {
                        StorageReference imageRef = profileImageRef.child(userId + ".jpg");

                        imageRef.putFile(selectedImageUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        // Update the profilePictureUrl in the Realtime Database
                                        userRef.child("profilePictureUrl").setValue(uri.toString());

                                        // Set visibility for the layouts
                                        layoutDisplay.setVisibility(View.VISIBLE);
                                        layoutEdit.setVisibility(View.GONE);
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Handle image upload failure
                                });
                    } else {
                        userRef.child("profilePictureUrl").setValue("");
                        // No new image selected, only update other fields
                        // Set visibility for the layouts
                        layoutDisplay.setVisibility(View.VISIBLE);
                        layoutEdit.setVisibility(View.GONE);
                    }
                }
            }
        });





        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Update the ImageView with the selected image
            profileImageEdit.setImageURI(selectedImageUri);
            // Handle image upload and database update here
        }
    }


    public void chooseImageFromGalleryOrCamera() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

}
