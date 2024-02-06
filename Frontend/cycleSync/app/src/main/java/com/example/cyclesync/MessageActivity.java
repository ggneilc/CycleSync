package com.example.cyclesync;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements WebSocketListener {

    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the content view that renders the map.
        setContentView(R.layout.messaging_activity);

        ImageView backArrow = findViewById(R.id.back_arrow_very_unique);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String serverUrl;
        String BASE_URL = "ws://10.0.2.2:8080/messaging/" + UserManager.getInstance().getUser().getId() + "/";

        // Check if the intent has the extra we are looking for
        if (intent.hasExtra("USER_ID")) {
            // Get the user ID from the intent extras
            int userId = intent.getIntExtra("USER_ID", 0);
            Log.d("MessageActivity", "Detected USER_ID: " + userId);
            serverUrl = BASE_URL + userId;
        } else {
            serverUrl = BASE_URL + UserManager.getInstance().getUser().getCurrent_rental().getRenterId();
        }

        Log.d("MessageActivity", "Initializing WS @ " + serverUrl);

        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MessageActivity.this);

        Button sendBtn = findViewById(R.id.sendButton);
        EditText messageText = findViewById(R.id.messageInput);

        sendBtn.setOnClickListener(v -> {
            try {

                // send message
                WebSocketManager.getInstance().sendMessage(messageText.getText().toString());
                this.addMessage(new Message((messageText.getText().toString()), true));
                messageText.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });

        if (intent.hasExtra("USER_ID")) {
            // Get the user ID from the intent extras
            int userId = intent.getIntExtra("USER_ID", 0);
            fetchMessages(UserManager.getInstance().getUser().getId(), userId);
        } else {
            fetchMessages(UserManager.getInstance().getUser().getId(), UserManager.getInstance().getUser().getCurrent_rental().getRenterId());
        }

//        Message message1 = new Message("Hello!", true); // 'true' indicates this message is from our user
//        Message message2 = new Message("Hi, how can I help you today?", false);
//        Message message3 = new Message("I'm looking for a bike rental.", true);
//        Message message4 = new Message("Sure, I can assist with that.", false);
//
//        messages.add(message1);
//        messages.add(message2);
//        messages.add(message3);
//        messages.add(message4);

    }

    private void fetchMessages(int meId, int otherId) {
        // Fetch other messages first
        String URL = "http://10.0.2.2:8080/messages/" + otherId + "/" + meId;

        Log.d("FETCH MESSAGES 1", "fetching: " + URL);

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

                        Type listType = new TypeToken<ArrayList<MessageDB>>(){}.getType();
                        List<MessageDB> localMessages = gson.fromJson(resp, listType);

                        for (MessageDB message: localMessages) {
                            Log.d("PARSING MESSAGE", message.getContent());
                            Message newMessage = new Message(message.getContent(), message.getSenderId() == meId);
                            addMessage(newMessage);
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

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            Message newMessage = new Message(message, false);
            this.addMessage(newMessage);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onWebSocketError(Exception ex) {

    }

    private void addMessage(Message message) {
        ListView messageList = findViewById(R.id.messageList);

        this.messages.add(message);

        MessageAdapter adapter = new MessageAdapter(this, this.messages);
        messageList.setAdapter(adapter);
        messageList.setSelection(messages.size() - 1); // Scroll to the bottom
    }

}
