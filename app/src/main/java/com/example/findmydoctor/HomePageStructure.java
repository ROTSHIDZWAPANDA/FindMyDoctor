package com.example.findmydoctor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.findmydoctor.databinding.ActivityHomePageStructureBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.annotations.NonNull;

public class HomePageStructure extends AppCompatActivity {

    ActivityHomePageStructureBinding binding;
    private FirebaseAuth mAuth;
    private MaterialToolbar Patienttoolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageStructureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        Patienttoolbar = binding.appBarLayout.findViewById(R.id.patientToolbar); // Replace with your MaterialToolbar's ID

        // Set the MaterialToolbar as the action bar
        setSupportActionBar(Patienttoolbar);
        getSupportActionBar().setTitle("Screen Name");

        replaceFragment(new PatientHomeFragment(), "Home");

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new PatientHomeFragment(), "Home");
            } else if (itemId == R.id.profile) {
                replaceFragment(new PatientProfileFragment(), "Profile");
            } else if (itemId == R.id.setting) {
                replaceFragment(new PatientSettingsFragment(), "Settings");
            }else if (itemId == R.id.inbox) {
                replaceFragment(new PatientInboxFragment(), "Inbox");
            }else if (itemId == R.id.notification) {
                replaceFragment(new PatientNotificationFragment(), "Notifications");
            }


            return true;
        });



    }
    private void replaceFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

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