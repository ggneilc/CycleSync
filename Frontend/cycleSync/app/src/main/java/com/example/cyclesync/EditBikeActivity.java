package com.example.cyclesync;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import androidx.core.app.ActivityCompat;


public class EditBikeActivity extends AppCompatActivity {

    EditText nameText, priceText, locationText, ratingText;
    Button buttonEditBike, buttonUseLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_bike_activity);

        nameText = findViewById(R.id.bikeName);
        priceText = findViewById(R.id.bikePrice);
        locationText = findViewById(R.id.bikeLocation);
        ratingText = findViewById(R.id.bikeRating);
        buttonEditBike = findViewById(R.id.buttonEditBike);

        // Retrieve data sent from the previous activity
        String name = getIntent().getStringExtra("name");
        int price = getIntent().getIntExtra("price", 0);
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        int bikeId = getIntent().getIntExtra("id", 0);

        // Set retrieved data to the EditText fields
        nameText.setText(name);
        priceText.setText(String.valueOf(price));
        locationText.setText(location);
        ratingText.setText(rating);

        buttonEditBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBike(
                        nameText.getText().toString(),
                        priceText.getText().toString(),
                        locationText.getText().toString(),
                        ratingText.getText().toString(),
                        bikeId,
                        currentLatitude,
                        currentLongitude
                );
            }
        });


        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize Location Manager and Listener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                // Use Geocoder to get street address and city from latitude and longitude
                Geocoder geocoder = new Geocoder(EditBikeActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);

                        // Get the street address and the city
                        String streetAddress = address.getAddressLine(0);
                        String city = address.getLocality();

                        // Format it as "Street Address, City"
                        String formattedAddress = streetAddress.split(",")[0] + ", " + city;
                        locationText.setText(formattedAddress);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Button useCurrentLocationBtn = findViewById(R.id.useCurrentLocation);
        useCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditBikeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request location permissions if not granted
                    ActivityCompat.requestPermissions(EditBikeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    // If permission is already granted, fetch the location
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                }
            }
        });
    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(EditBikeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request location permissions if not granted
                    ActivityCompat.requestPermissions(EditBikeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }
        }
    }

    private void updateBike(String name, String price, String location, String rating, int id, double latitude, double longitude) {
        String URL = "http://10.0.2.2:8080/bikes/" + id;

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        params.put("location", location);
        params.put("rating", rating);
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, URL, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        try {
                            String message = response.getString("message");

                            if (message.equals("success")) {
                                Intent intent = new Intent(EditBikeActivity.this, InventoryActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditBikeActivity.this, "Bike Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditBikeActivity.this, "Failed to update Bike, please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }
}
