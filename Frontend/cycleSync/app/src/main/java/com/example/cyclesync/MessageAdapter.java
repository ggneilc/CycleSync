package com.example.cyclesync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private static final int MESSAGE_SENT = 0;
    private static final int MESSAGE_RECEIVED = 1;

    public MessageAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getItem(position);

        if (message.isMyMessage()) {
            return MESSAGE_SENT;
        } else {
            return MESSAGE_RECEIVED;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            switch (viewType) {
                case MESSAGE_SENT:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_sent, parent, false);
                    break;
                case MESSAGE_RECEIVED:
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_received, parent, false);
                    break;
            }
        }

        Message message = getItem(position);
        TextView messageBody = convertView.findViewById(R.id.message_body);
        messageBody.setText(message.getBodyText());

        return convertView;
    }
}