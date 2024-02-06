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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_history_activity);

        int id = UserManager.getInstance().getUser().getId();

        fetchRentalHistory(id);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hgv) {
                finish();
            }
        });
    }



    private void fetchRentalHistory(int id) {
        String URL = "http://10.0.2.2:8080/rentals/user/" + id;

        Log.d("FETCH RENTAL", "fetching: " + URL);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        String resp = response.toString();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
                        Gson gson = gsonBuilder.create();

                        Type listType = new TypeToken<ArrayList<RentalPreview>>(){}.getType();
                        List<RentalPreview> rentals = gson.fromJson(resp, listType);

                        LinearLayout parentLayout = findViewById(R.id.items_container);
                        if (rentals.isEmpty()) {
                            Toast.makeText(RentalHistoryActivity.this, "No history available", Toast.LENGTH_SHORT).show();
                        }

                        for(RentalPreview rental : rentals) {
                            Log.d("RENTAL_PREVIEWS", rental.toString());
                            RentalHistoryItem itemView = new RentalHistoryItem(RentalHistoryActivity.this);
                            itemView.updateRentalItem(rental);
                            parentLayout.addView(itemView);
//                        }
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
