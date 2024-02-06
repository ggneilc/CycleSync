package com.example.cyclesync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RenterSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_settings_activity);

        // Fetch user
        User user = UserManager.getInstance().getUser();
        Log.d("user on renter settings", user.toString());

        TextView accountName = findViewById(R.id.renterAccountName);
        accountName.setText(UserManager.getInstance().getUser().getName());

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
//
//        CustomComponent inventory = findViewById(R.id.inventoryRenter_id);
//        inventory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RenterSettingsActivity.this, InventoryActivity.class);
//                startActivity(intent);
//            }
//        });

        Button inventory_testing = findViewById(R.id.inventory_testing);
        inventory_testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });



        CustomComponent messages = findViewById(R.id.messages);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, MessageRentersActivity.class);
                startActivity(intent);
            }
        });

        CustomComponent payment_methods = findViewById(R.id.payment_methods);
        payment_methods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RenterSettingsActivity.this, PaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }
}
