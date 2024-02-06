package com.example.cyclesync;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentNowActivity extends AppCompatActivity {
    private RadioGroup paymentMethodsGroup;
    private int selectedPaymentMethodId = -1;

    private final Map<Integer, PaymentMethod> radioButtonIdToPaymentMethodMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.rent_now_activity);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button rentNow = findViewById(R.id.startRental);

        rentNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRentalReq();
            }
        });

        Bike bike = BikeManager.getInstance().getBike();

        if (bike == null) {
            Log.d("RentNow", "no bike!");
        } else {
            Log.d("RentNow", "bike!");
        }


        // Bike Name
        TextView bikeName = findViewById(R.id.inventoryText1);
        bikeName.setText(bike.getName());

        // Bike Type
        ImageView inventoryIcon = findViewById(R.id.inventoryIcon);
        if (bike.isElectric()) {
            inventoryIcon.setImageResource(R.drawable.electric_icon);
        } else {
            inventoryIcon.setImageResource(R.drawable.become_renter_setting);
        }

        // Price Value
        TextView price = findViewById(R.id.price_value);
        price.setText("$" + String.valueOf(bike.getPrice()) + "/hr");

        // Rating Value
        TextView rating = findViewById(R.id.rating_value);
        rating.setText(bike.getRating());

        // Location Value
        TextView location = findViewById(R.id.location_value);
        location.setText(bike.getLocation());

        paymentMethodsGroup = findViewById(R.id.payment_methods_group);
        List<PaymentMethod> paymentMethods = UserManager.getInstance().getUser().getPaymentMethods();
        createRadioButtonsForPaymentMethods(paymentMethods);
    }

    private void createRadioButtonsForPaymentMethods(List<PaymentMethod> paymentMethods) {
        for (PaymentMethod method : paymentMethods) {
            // Inflate the custom layout for the radio button
            View radioButtonView = getLayoutInflater().inflate(R.layout.payment_method_selector, paymentMethodsGroup, false);
            RadioButton radioButton = radioButtonView.findViewById(R.id.payment_method_radio_button);
            TextView methodName = radioButtonView.findViewById(R.id.method_name);
            ImageView methodIcon = radioButtonView.findViewById(R.id.method_icon);

            methodName.setText(method.getName());

            // Set a unique ID for the RadioButton
            int radioButtonId = View.generateViewId();
            radioButton.setId(radioButtonId);

            radioButtonIdToPaymentMethodMap.put(radioButtonId, method);

            // Add the custom radio button view to the group
            paymentMethodsGroup.addView(radioButtonView);

            radioButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear the previous selection
                    if (selectedPaymentMethodId != -1) {
                        RadioButton prevRadioButton = findViewById(selectedPaymentMethodId);
                        prevRadioButton.setChecked(false);
                    }
                    // Mark the current button as checked
                    radioButton.setChecked(true);
                    selectedPaymentMethodId = radioButtonId;

                    // Handle your selected payment method here
                    handlePaymentMethodSelection(method);
                }
            });

            // Listen for clicks on the radio buttons
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear the previous selection
                    if (selectedPaymentMethodId != -1) {
                        RadioButton prevRadioButton = findViewById(selectedPaymentMethodId);
                        prevRadioButton.setChecked(false);
                    }
                    // Mark the current button as checked
                    radioButton.setChecked(true);
                    selectedPaymentMethodId = radioButtonId;

                    // Handle your selected payment method here
                    handlePaymentMethodSelection(method);
                }
            });
        }
    }

    private void handlePaymentMethodSelection(PaymentMethod selectedMethod) {
        // Implement your logic to handle the selected payment method
        // For instance, update the payment method details on the screen or store the selection
        // ...
    }


    private void makeRentalReq() {
        String userId = String.valueOf(UserManager.getInstance().getUser().getId());
        String bikeId = String.valueOf(BikeManager.getInstance().getBike().getId());
        String URL = "http://10.0.2.2:8080/users/" + userId + "/rent/" + bikeId + "/" + radioButtonIdToPaymentMethodMap.get(selectedPaymentMethodId).getId();

        Log.d("RentNowRequest", "Sending request to URL: " + URL);

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
                                Toast.makeText(RentNowActivity.this, "Rental Started!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RentNowActivity.this, "Failed to start rental, please try again", Toast.LENGTH_SHORT).show();
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
}
