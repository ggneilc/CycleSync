package com.example.cyclesync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RenterAdapter extends RecyclerView.Adapter<RenterAdapter.ViewHolder> {
    private List<RenterPreview> renters;
    Context context;

    public RenterAdapter(Context context, List<RenterPreview> renters) {
        this.renters = renters;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RenterPreview renter = renters.get(position);
        holder.nameTextView.setText(renter.getRenter_name());
        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat(renter);
            }
        });
    }

    private void startChat(RenterPreview renter) {
        Intent intent = new Intent(context, MessageActivity.class);
        Log.d("RenterAdapter", "Starting chat with " + renter.getRenter_id());
        int userId = renter.getRenter_id();
        intent.putExtra("USER_ID", userId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return renters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button chatButton;

        ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.renter_name);
            chatButton = view.findViewById(R.id.chat_button);
        }
    }
}