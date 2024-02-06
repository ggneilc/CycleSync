package com.example.cyclesync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {
    EditText editTextNewName;
    EditText editTextNewEmail;
    EditText editTextNewPassword;
    Button buttonCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);

        editTextNewName = findViewById(R.id.editTextNewName);
        editTextNewEmail = findViewById(R.id.editTextNewEmail);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonCreateNewAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextNewName.getText().toString();
                String email = editTextNewEmail.getText().toString();
                String password = editTextNewPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    makeSignupReq(name, email, password);
                }
            }
        });
    }

    private void makeSignupReq(String name, String email, String password){
        //String URL = "http://coms-309-069.class.las.iastate.edu:8080/users";
        String URL = "http://10.0.2.2:8080/users";

        String[] nameParts = name.split(" ", 2);
        String firstname = nameParts[0];
        String lastname = (nameParts.length > 1) ? nameParts[1] : "";

        Map<String, String> params = new HashMap();
        params.put("firstName", firstname);
        params.put("lastName", lastname);
        params.put("emailId", email);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        try {
                            String message = response.getString("message");

                            if (message.equals("success")) {
                                // Successfully created account. You should now navigate to another activity or update the UI accordingly
                                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(CreateAccountActivity.this, "Account created! Please sign in.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Something went wrong while creating the account. Handle the situation accordingly, maybe show an error message
                                Toast.makeText(CreateAccountActivity.this, "Failed to create account, please try again", Toast.LENGTH_SHORT).show();
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
