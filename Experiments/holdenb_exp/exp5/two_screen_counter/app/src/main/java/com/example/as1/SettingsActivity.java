package com.example.as1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        CustomComponent savedPostsComponent = findViewById(R.id.saved);
        savedPostsComponent.setOnComponentClickListener(new CustomComponent.OnComponentClickListener() {
            @Override
            public void onComponentClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, SavedPostActivity.class);
                startActivity(intent);
            }
        });


        CustomComponent becomeRenterComponent = findViewById(R.id.become_renter_setting);
        becomeRenterComponent.setOnComponentClickListener(new CustomComponent.OnComponentClickListener() {
            @Override
            public void onComponentClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, RenterSettingsActivity.class);
                startActivity(intent);
            }
        });


    }
}

