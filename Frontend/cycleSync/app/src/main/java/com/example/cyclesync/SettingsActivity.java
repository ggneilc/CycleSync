package com.example.cyclesync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Fetch user
        Log.d("user on settings", UserManager.getInstance().getUser().toString());

        TextView accountName = findViewById(R.id.accountName);
        accountName.setText(UserManager.getInstance().getUser().getName());

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        CustomComponent savedPostsComponent = findViewById(R.id.saved);
//        savedPostsComponent.setOnComponentClickListener(new CustomComponent.OnComponentClickListener() {
//            @Override
//            public void onComponentClick(View view) {
//                Intent intent = new Intent(SettingsActivity.this, SavedPostActivity.class);
//                startActivity(intent);
//            }
//        });


        CustomComponent becomeRenterComponent = findViewById(R.id.become_renter_setting);
        becomeRenterComponent.setOnComponentClickListener(new CustomComponent.OnComponentClickListener() {
            @Override
            public void onComponentClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, RenterSettingsActivity.class);
                startActivity(intent);
            }
        });

        CustomComponent inventory = findViewById(R.id.inventory);
        inventory.setOnComponentClickListener(new CustomComponent.OnComponentClickListener() {
            @Override
            public void onComponentClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });


        CustomComponent logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log the user out
                UserManager.getInstance().logout();

                // Navigate to login/signup screen
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
                startActivity(intent);

                // Finish current activity (optional based on your navigation design)
                finish();
            }
        });

        CustomComponent rentalHistory = findViewById(R.id.rentalHistory);
        rentalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, RentalHistoryActivity.class);
                startActivity(intent);
            }
        });

        CustomComponent paymentMethods = findViewById(R.id.payment_methods);
        paymentMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }
}

