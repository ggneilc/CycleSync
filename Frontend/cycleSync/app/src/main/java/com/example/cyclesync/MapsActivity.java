package com.example.cyclesync;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONException;


import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, WebSocketListener {

    private GoogleMap map;
    private CameraPosition cameraPosition;
    private boolean isLayoutExpanded = false;


    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private static final String TAG = MapsActivity.class.getSimpleName();

    private Marker focusedMarker;

    private List<BikeMarker> bikeMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }



        // Retrieve the content view that renders the map.
        setContentView(R.layout.maps_activity);

        User user = UserManager.getInstance().getUser();
        Log.d("user on maps", user.toString());
        String welcomeText = "Welcome " + UserManager.getInstance().getUser().getName() + "!";
        Toast.makeText(MapsActivity.this, welcomeText, Toast.LENGTH_SHORT).show();

        FrameLayout settingsImageView = findViewById(R.id.settings);
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (user.getIsRenter()) {
                    intent = new Intent(MapsActivity.this, RenterSettingsActivity.class);
                } else {
                    intent = new Intent(MapsActivity.this, SettingsActivity.class);
                }
                startActivity(intent);
            }
        });

        FrameLayout messageRenter = findViewById(R.id.message_renter);
        messageRenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                Rental currentRental = UserManager.getInstance().getUser().getCurrent_rental();
                if (currentRental != null) {
                    intent = new Intent(MapsActivity.this, MessageActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MapsActivity.this, "You don't have an active rental!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonLogin = findViewById(R.id.buttonRentNow);
        buttonLogin.setOnClickListener(v -> {
            Bike currentBike = BikeManager.getInstance().getBike();
            Rental currentRental = UserManager.getInstance().getUser().getCurrent_rental();

            if (currentRental != null) {
                makeRentalStopReq();
            } else if(currentBike == null) {
                Toast.makeText(MapsActivity.this, "Please select a bike", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MapsActivity.this, RentNowActivity.class);
                startActivity(intent);
            }
        });

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String serverUrl = "ws://10.0.2.2:8080/rental-updates/" + UserManager.getInstance().getUser().getId();
        Log.d("MapsActivity", "Initializing WS @ " + serverUrl);

        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MapsActivity.this);


        RelativeLayout filterLayout = findViewById(R.id.filterLayout);
        ImageView arrow = findViewById(R.id.arrow_extra_unique); // Ensure you have this ImageView in your layout
        ExpandableLayout expandableLayout = findViewById(R.id.expandableLayout);

        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpandableLayout(expandableLayout, arrow);
            }
        });

        Button applyButton = findViewById(R.id.filterApply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int priceMin = Integer.parseInt(((EditText) findViewById(R.id.priceMin)).getText().toString());
                    int priceMax = Integer.parseInt(((EditText) findViewById(R.id.priceMax)).getText().toString());
                    boolean filterClassic = ((Switch) findViewById(R.id.classic)).isChecked();
                    boolean filterElectric = ((Switch) findViewById(R.id.electric)).isChecked();

                    if (priceMin > priceMax) {
                        Toast.makeText(MapsActivity.this, "Minimum price cannot be greater than maximum price.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    filterBikes(priceMin, priceMax, filterClassic, filterElectric);
                } catch (NumberFormatException e) {
                    Toast.makeText(MapsActivity.this, "Please enter valid prices.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toggleExpandableLayout(ExpandableLayout expandableLayout, ImageView arrow) {
        if (isLayoutExpanded) {
            expandableLayout.collapse();
            arrow.setRotation(0); // Arrow points down
        } else {
            expandableLayout.expand();
            arrow.setRotation(180); // Arrow points up
        }
        isLayoutExpanded = !isLayoutExpanded; // Toggle the state
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map == null) {
            return;
        }
        map.clear();
        this.bikeMarkers = new ArrayList<>();
        fetchBikeLocationFromBackend();
        Button rentNowButton = findViewById(R.id.buttonRentNow);
        rentNowButton.setText("Rent Now");
        rentNowButton.setBackgroundResource(R.drawable.green_button_rounded);
        fetchCurrentRental();
    }

    private void fetchBikeLocationFromBackend() {
        Log.d("made it into top", "got");
        String URL = "http://10.0.2.2:8080/bikes";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Bike> bikes = parseBikes(response);

                            for (Bike bike : bikes) {
                                if (!bike.getonMarket()) {
                                    continue;
                                }
                                double lat = bike.getLatitude();
                                double lng = bike.getLongitude();
                                int price = bike.getPrice();

                                Marker bikeMarker = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lng))
                                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(price, false))));
                                bikeMarkers.add(new BikeMarker(bikeMarker, bike));
                            }
                        } catch (JSONException e) {
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

    }

    private void filterBikes(int priceMin, int priceMax, boolean filterClassic, boolean filterElectric) {
        map.clear(); // Clear existing markers
        for (BikeMarker bikeMarker : bikeMarkers) {
            Bike bike = bikeMarker.getBike();
            if (bike.getPrice() >= priceMin && bike.getPrice() <= priceMax &&
                    ((filterClassic && !bike.isElectric()) || (filterElectric && bike.isElectric()))) {
                // Directly add a new marker for the bike
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(bike.getLatitude(), bike.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(bike.getPrice(), false))));
            }
        }
    }


    private void fetchCurrentRental() {
        String URL = "http://10.0.2.2:8080/users/" + UserManager.getInstance().getUser().getId() + "/currentRental";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Rental currentRental = parseRental(response);

                            UserManager.getInstance().getUser().setCurrent_rental(currentRental);

                            if (currentRental != null) {
                                // Change button
                                Button rentNowButton = findViewById(R.id.buttonRentNow);
                                rentNowButton.setText("Stop Rental");
                                rentNowButton.setBackgroundResource(R.drawable.red_button_rounded);
                            } else {
                                Button rentNowButton = findViewById(R.id.buttonRentNow);
                                rentNowButton.setText("Rent Now");
                                rentNowButton.setBackgroundResource(R.drawable.green_button_rounded);
                            }
                        } catch (JSONException e) {
                            Button rentNowButton = findViewById(R.id.buttonRentNow);
                            rentNowButton.setText("Rent Now");
                            rentNowButton.setBackgroundResource(R.drawable.green_button_rounded);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }


    public List<Bike> parseBikes(JSONArray bikeArray) throws JSONException {
        List<Bike> bikes = new ArrayList<>();

        for (int i = 0; i < bikeArray.length(); i++) {

            JSONObject bikeJson = bikeArray.getJSONObject(i);

            int id = bikeJson.getInt("id");
            String rating = bikeJson.getString("rating");
            boolean electric = bikeJson.getBoolean("electric");
            boolean onMarket = bikeJson.getBoolean("onMarket");
            String location = bikeJson.getString("location");
            int price = bikeJson.getInt("price");
            double latitude = bikeJson.getDouble("latitude");
            double longitude = bikeJson.getDouble("longitude");
            String name = bikeJson.getString("name");
            boolean inUse = bikeJson.getBoolean("use");

            Bike bike = new Bike(id, name, rating, inUse, electric, location, price, latitude, longitude, onMarket);
            Log.d("bikes 2", bike.toString());
            bikes.add(bike);
        }

        return bikes;
    }

    public Rental parseRental(JSONObject rental) throws JSONException {
        Rental currentRental = new Rental();
        currentRental.setId(rental.getInt("id"));
        currentRental.setPrice(rental.getInt("price"));
        currentRental.setRenter_id(rental.getInt("renter_id"));
        return currentRental;
    }

    private void makeRentalStopReq() {
        String userId = String.valueOf(UserManager.getInstance().getUser().getId());
        String URL = "http://10.0.2.2:8080/rentals/stop/" + userId;

        Log.d("StopRentRequest", "Sending request to URL: " + URL);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.equals("success")) {
                                Intent intent = new Intent(MapsActivity.this, RentalReviewActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MapsActivity.this, "Failed to stop rental, please try again", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        setMapStyle();
        fetchBikeLocationFromBackend();
        fetchCurrentRental();
        Log.d("bikes 1", "got");

        map.setOnMarkerClickListener(marker -> {
            BikeMarker clickedBikeMarker = null;
            for (BikeMarker bikeMarker : bikeMarkers) {
                if (bikeMarker.getMarker().equals(marker)) {
                    clickedBikeMarker = bikeMarker;
                    break;
                }
            }
            if (clickedBikeMarker != null) {
                if (clickedBikeMarker.isHighlighted()) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(clickedBikeMarker.getBike().getPrice(), false)));
                    clickedBikeMarker.setHighlighted(false);
                } else {
                    for (BikeMarker bikeMarker : bikeMarkers) {
                        bikeMarker.getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(bikeMarker.getBike().getPrice(), false)));
                        bikeMarker.setHighlighted(false);
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(createCustomMarker(clickedBikeMarker.getBike().getPrice(), true)));
                    clickedBikeMarker.setHighlighted(true);
                }
            }
            this.focusedMarker = marker;
            BikeManager.getInstance().setBike(clickedBikeMarker.getBike());
            return true;
        });
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }




    private void setMapStyle() {
        boolean result = this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        if (!result) {
            Log.e("Map", "Error setting map style");
        }
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private Bitmap createCustomMarker(int price, boolean tapped) {

        View markerLayout = getLayoutInflater().inflate(R.layout.marker_layout, null);

        TextView txt_price = markerLayout.findViewById(R.id.markerText);
        txt_price.setText("$" + String.valueOf(price) + "/hr");

        if (tapped) {
            markerLayout.setBackgroundResource(R.drawable.marker_tapped);
        } else {
            markerLayout.setBackgroundResource(R.drawable.marker);
        }

        markerLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);

        return bitmap;
    }

    public class BikeMarker {
        private Marker marker;
        private Bike bike;
        private boolean isHighlighted;

        public BikeMarker(Marker marker, Bike bike) {
            this.marker = marker;
            this.bike = bike;
        }

        public Marker getMarker() {
            return marker;
        }

        public Bike getBike() {
            return bike;
        }
        public boolean isHighlighted() {
            return isHighlighted;
        }
        public void setHighlighted(boolean highlighted) {
            isHighlighted = highlighted;
        }

        public void setMarker(Marker newMarker) {
            this.marker = newMarker;
        }
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            try {
                // Convert the string message into a JSONObject
                JSONObject jsonObject = new JSONObject(message);

                // Extract the values for 'alertType' and 'bikeId'
                String alertType = jsonObject.getString("alertType");
                int bikeId = jsonObject.getInt("bikeId");

                if (alertType.equals("bikeRented")) {
                    Iterator<BikeMarker> iterator = this.bikeMarkers.iterator();
                    while (iterator.hasNext()) {
                        BikeMarker bm = iterator.next();
                        if (bm.getBike().getId() == bikeId) {
                            Log.d("MapsActivity", "Removing bike: " + bikeId);
                            iterator.remove(); // Removes the current bike marker
                        }
                    }
                    map.clear();
                    fetchBikeLocationFromBackend();
                } else {
                    fetchBikeLocationFromBackend();
                }

                // Do something with the extracted values. For now, let's just log them
                Log.d("WebSocketListener", "Received Alert Type: " + alertType + ", Bike ID: " + bikeId);

                // Add additional UI related tasks here. Be mindful that you're on UI thread.

            } catch (JSONException e) {
                // The message doesn't seem to be in the expected format. Handle the error.
                Log.e("WebSocketException", "Error parsing JSON: " + e.getMessage());
            }
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }

}