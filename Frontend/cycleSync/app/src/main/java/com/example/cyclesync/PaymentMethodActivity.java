package com.example.cyclesync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.widget.Button;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentMethodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_methods_activity);

        int id = UserManager.getInstance().getUser().getId();

        fetchPaymentMethods(id);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hgv) {
                finish();
            }
        });

        Button addPaymentMethod = findViewById(R.id.add_payment_method);
        addPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View hgv) {
                Intent intent = new Intent(PaymentMethodActivity.this, AddPaymentMethodActivity.class);
                startActivity(intent);
            }
        });
    }



    private void fetchPaymentMethods(int id) {
        String URL = "http://10.0.2.2:8080/payment_methods/" + id;

        Log.d("FETCH PAYMENT METHODS", "fetching: " + URL);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        clearPaymentMethods();
                        String resp = response.toString();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
                        Gson gson = gsonBuilder.create();

                        Type listType = new TypeToken<ArrayList<PaymentMethod>>(){}.getType();
                        List<PaymentMethod> paymentMethods = gson.fromJson(resp, listType);

                        // Update user object
                        UserManager.getInstance().getUser().setPaymentMethods(paymentMethods);

                        LinearLayout parentLayout = findViewById(R.id.items_container);
                        if (paymentMethods.isEmpty()) {
                            Toast.makeText(PaymentMethodActivity.this, "No payment methods available", Toast.LENGTH_SHORT).show();
                        }

                        for(PaymentMethod paymentMethod : paymentMethods) {
                            Log.d("PAYMENT_METHODS", paymentMethod.getName());
                            addPaymentMethodToView(paymentMethod);
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

    private void clearPaymentMethods() {
        LinearLayout itemsContainer = findViewById(R.id.items_container);
        itemsContainer.removeAllViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPaymentMethods(UserManager.getInstance().getUser().getId());
    }

    // Helper method to add a payment method to the LinearLayout container
    private void addPaymentMethodToView(PaymentMethod paymentMethod) {
        LinearLayout parentLayout = findViewById(R.id.items_container);

        // Create a new view for the payment method
        View paymentMethodView = getLayoutInflater().inflate(R.layout.payment_method_item, parentLayout, false);

        // Update the payment method details
        TextView methodName = paymentMethodView.findViewById(R.id.method_name); // Replace with actual ID
        methodName.setText(paymentMethod.getName());

        // Possibly you will have icons for different payment methods (e.g., credit card, PayPal)
        ImageView methodIcon = paymentMethodView.findViewById(R.id.method_icon); // Replace with actual ID
        // Set the correct icon depending on the type of payment method
//        methodIcon.setImageResource(getPaymentMethodIcon(paymentMethod.getType()));

        // Add onClickListener if you want to handle item clicks
        paymentMethodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event to view details
            }
        });

//        // Add divider before adding the payment method view, if not the first item
//        if (parentLayout.getChildCount() > 0) {
//            View divider = new View(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
//            layoutParams.setMargins(0, 16, 0, 16); // Set top and bottom margins for the divider
//            divider.setLayoutParams(layoutParams);
//            divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set the divider color
//            parentLayout.addView(divider);
//        }

        // Finally, add the payment method view to the layout
        parentLayout.addView(paymentMethodView);
    }

    // Dummy method, replace with your logic to determine the correct icon
//    private int getPaymentMethodIcon(String type) {
//        // TODO: Replace with logic to return different icons for different payment types
//        int drawableId;
//        switch (type) {
//            case "credit_card":
//                drawableId = R.drawable.ic_credit_card; // Replace with your credit card icon drawable
//                break;
//            case "paypal":
//                drawableId = R.drawable.ic_paypal; // Replace with your PayPal icon drawable
//                break;
//            default:
//                drawableId = R.drawable.ic_generic_payment; // Replace with your generic payment icon drawable
//        }
//        return drawableId;
//    }
}
