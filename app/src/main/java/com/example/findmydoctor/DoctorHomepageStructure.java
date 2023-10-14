package com.example.findmydoctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmydoctor.databinding.ActivityDoctorHomepageStructureBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.annotations.NonNull;

public class DoctorHomepageStructure extends AppCompatActivity {
    ActivityDoctorHomepageStructureBinding binding;
    private FirebaseAuth mAuth;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorHomepageStructureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        replaceFragment(new DoctorHomeFragment());

        // Initialize the action bar (MaterialToolbar)
        toolbar = binding.appBarLayout.findViewById(R.id.toolbar); // Replace with your MaterialToolbar's ID

        // Set the MaterialToolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Screen Name");

        // Load the initial fragment
        replaceFragment(new DoctorHomeFragment(), "Home");

        mAuth = FirebaseAuth.getInstance();
        Log.d("DebugTag", "Activity created");


        binding.bottomNavigationViewDoctor.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new DoctorHomeFragment(), "Home");
            } else if (itemId == R.id.profile) {
                replaceFragment(new PatientProfileFragment(), "Profile");
            } else if (itemId == R.id.setting) {
                replaceFragment(new DoctorSettingsFragment(), "Settings");
            } else if (itemId == R.id.inbox) {
                replaceFragment(new DoctorInboxFragment(), "Inbox");
            } else if (itemId == R.id.notification) {
                replaceFragment(new DoctorNotificationFragment(), "Notifications");
            }

            return true;
        });



    }

    private void replaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutDoctor, fragment);
        fragmentTransaction.commit();

        // Set the title in the Toolbar
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            // Handle the logout action
            Log.d("DebugTag", "Logout menu item clicked");
            performLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked "Yes," log them out
                    mAuth.signOut();
                    startActivity(new Intent(this, SignIn.class));
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User clicked "No," dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    }
}
