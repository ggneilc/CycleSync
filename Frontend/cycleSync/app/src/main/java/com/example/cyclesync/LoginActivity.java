package com.example.cyclesync;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogin;
    Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextUsername = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                makeLoginreq(username, password);
            }
        });

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void makeLoginreq(String username, String password) {
        String URL = "http://10.0.2.2:8080/users/login";

        Map<String, String> params = new HashMap();
        params.put("emailId", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);
        Log.d("LoginRequest", "Sending request with parameters: " + parameters.toString());


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            if (response.has("message") && response.getString("message").equals("failure")) {
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            } else {
                                Gson gson = new Gson();

                                // Wrap the Gson parsing code in a try-catch block
                                try {
                                    User user = gson.fromJson(response.toString(), User.class);

                                    // Set the user in UserManager for this session
                                    UserManager.getInstance().setUser(user);
                                    Log.d("user on login", user.toString());

                                    // Save the user to SharedPreferences for persistence across app restarts
//                                    saveUserToSharedPreferences(user);

                                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                    finish();

                                } catch (JsonSyntaxException e) {
                                    // This is the exception thrown by Gson if parsing fails
                                    Log.e("GSON Parsing Error", e.toString());
                                    Toast.makeText(LoginActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                            Toast.makeText(LoginActivity.this, "Error processing server response", Toast.LENGTH_SHORT).show();
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

