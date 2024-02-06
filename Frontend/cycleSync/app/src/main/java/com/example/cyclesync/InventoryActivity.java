package com.example.cyclesync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import android.content.Intent;
import android.widget.Button;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_activity);

        User user = UserManager.getInstance().getUser();
        int id = user.getId();

        fetchInventory(id);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hgv) {
                finish();
            }
        });

        Button addBikeButton = findViewById(R.id.addBike);
        addBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryActivity.this, AddBikeActivity.class);
                startActivity(intent);
            }
        });
    }



    private void fetchInventory(int id) {
        String URL = "http://10.0.2.2:8080/users/" + id;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        String resp = response.toString();
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        UserManager.getInstance().setUser(user);
                        List<Bike> bikes = user.getBikes();

                        LinearLayout parentLayout = findViewById(R.id.items_container);
                        if (bikes.isEmpty()) {
                            Toast.makeText(InventoryActivity.this, "No bikes available", Toast.LENGTH_SHORT).show();
                        }

                        for(Bike bike : bikes) {
                            // Inflate the InventoryItem layout
                            InventoryItem itemView = new InventoryItem(InventoryActivity.this);
                            itemView.setText1(bike.getName());
                            itemView.updateInventoryItem(bike);
                            parentLayout.addView(itemView);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any errors that occur during the request
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
