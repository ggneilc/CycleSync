package com.example.cyclesync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageRentersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_renters_activity);

        fetchRenters(UserManager.getInstance().getUser().getId());

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hgv) {
                finish();
            }
        });
    }



    private void fetchRenters(int id) {
        String URL = "http://10.0.2.2:8080/users/" + id + "/getRenters";

        Log.d("FETCH RENTERS", "fetching: " + URL);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        String resp = response.toString();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();

                        Type listType = new TypeToken<ArrayList<RenterPreview>>(){}.getType();
                        List<RenterPreview> renters = gson.fromJson(resp, listType);

                        RecyclerView recyclerView = findViewById(R.id.renters_list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageRentersActivity.this));
                        RenterAdapter renterAdapter = new RenterAdapter(MessageRentersActivity.this, renters);
                        recyclerView.setAdapter(renterAdapter);
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