package com.example.findmydoctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private LinearLayout layoutOne;
    private LinearLayout layoutTwo;
    private Button nextOne,getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        layoutOne = findViewById(R.id.layoutOnee);
        layoutTwo = findViewById(R.id.layoutTwoo);
        nextOne = findViewById(R.id.nextOne);
        getStarted = findViewById(R.id.nextTwo);

        nextOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide layoutOne and show layoutTwo when NEXT is clicked
                layoutOne.setVisibility(View.GONE);
                layoutTwo.setVisibility(View.VISIBLE);
            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, SignIn.class);
                startActivity(intent);


            }
        });




    }
}