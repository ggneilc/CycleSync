package com.example.cyclesync;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class RentalReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_review_activity);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText reviewText = findViewById(R.id.review_text);
        Button submitBtn = findViewById(R.id.submit_review);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get rating value. Round to nearest whole number.
                int rating = Math.round(ratingBar.getRating());

                // Get the review entered by the user.
                String review = reviewText.getText().toString();

                int rentalId = UserManager.getInstance().getUser().getCurrent_rental().getId();
                makeRentalReviewReq(rentalId, rating, review);
            }
        });

    }

    private void makeRentalReviewReq(int id, int rating, String review) {
        String URL = "http://10.0.2.2:8080/rentals/review/" + id;

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("rating", rating);
            postparams.put("review", review);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        try {
                            String message = response.getString("message");

                            if (message.equals("success")) {
                                Toast.makeText(RentalReviewActivity.this,
                                        "Review Submitted Successfully!",
                                        Toast.LENGTH_SHORT).show();
                                UserManager.getInstance().getUser().setCurrent_rental(null);
                                finish();
                            } else {
                                Toast.makeText(RentalReviewActivity.this,
                                        "Failed to submit review, please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}