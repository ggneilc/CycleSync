package com.example.as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class RenterSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_settings_activity);

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        CustomComponent becomerider = findViewById(R.id.become_rider);
        becomerider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        CustomComponent inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
