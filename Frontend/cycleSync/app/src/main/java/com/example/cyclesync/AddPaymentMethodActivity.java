package com.example.cyclesync;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class AddPaymentMethodActivity extends AppCompatActivity {

    private EditText paymentMethodName;
    private EditText cardHolderName;
    private EditText cardNumber;
    private EditText expiryDate;
    private EditText cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_method_activity);

        paymentMethodName = findViewById(R.id.payment_method_name);
        cardHolderName = findViewById(R.id.card_holder_name);
        cardNumber = findViewById(R.id.card_number);
        expiryDate = findViewById(R.id.expiry_date);
        cvv = findViewById(R.id.cvv);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button savePaymentMethodButton = findViewById(R.id.save_payment_method_button);
        savePaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate input fields
                if (isInputValid()) {
                    // Construct a payment method object or map from the inputs
                    // Depending on how your PaymentMethod class is structured, you may need to create a new instance and set values, or build a map
                    PaymentMethod newPaymentMethod = new PaymentMethod(
                            PaymentMethod.PaymentType.CREDIT_CARD,
                            cardHolderName.getText().toString(),
                            paymentMethodName.getText().toString(),
                            cardNumber.getText().toString(),
                            expiryDate.getText().toString(),
                            cvv.getText().toString()
                    );

                    sendAddPaymentMethodRequest(newPaymentMethod);

                } else {
                    Toast.makeText(AddPaymentMethodActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isInputValid() {
        // Basic validation to check if the input fields are not empty
        // More complex validation logic such as card number format, expiry date format, CVV number length, etc., can be added here
        return !paymentMethodName.getText().toString().trim().isEmpty() &&
                !cardHolderName.getText().toString().trim().isEmpty() &&
                !cardNumber.getText().toString().trim().isEmpty() &&
                !expiryDate.getText().toString().trim().isEmpty() &&
                !cvv.getText().toString().trim().isEmpty();
    }

    private void sendAddPaymentMethodRequest(PaymentMethod newPaymentMethod) {
        String URL = "http://10.0.2.2:8080/payment_methods/" + UserManager.getInstance().getUser().getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            URL,
            new JSONObject(newPaymentMethod.toMap()), // Assuming your PaymentMethod object has a toMap() method that returns its properties as a Map
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Volley Response", response.toString());

                    try {
                        String message = response.getString("message");

                        if (message.equals("success")) {
                            finish();
                            Toast.makeText(AddPaymentMethodActivity.this, "Payment Method Added", Toast.LENGTH_SHORT).show();
                        } else {
                            // Something went wrong while creating the account. Handle the situation accordingly, maybe show an error message
                            Toast.makeText(AddPaymentMethodActivity.this, "Failed to add payment method, please try again", Toast.LENGTH_SHORT).show();
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

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}