package com.example.cyclesync;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.cachapa.expandablelayout.ExpandableLayout;
import android.util.Log;
import android.content.res.TypedArray;
import android.widget.Button;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.widget.Toast;
import android.content.Intent;

public class InventoryItem extends RelativeLayout {
    private Button deleteButton;
    private Bike currentBike;
    private ExpandableLayout expandableLayout;
    private ImageView inventoryIcon;
    private ImageView arrow;
    private TextView inventoryText1;
    private TextView priceValue;
    private TextView locationValue;
    private TextView ratingValue;
    private TextView inUseValue;

    public InventoryItem(Context context) {
        super(context);
        init(null);
    }

    public InventoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, this, true);

        inventoryIcon = findViewById(R.id.inventoryIcon);
        inventoryText1 = findViewById(R.id.inventoryText1);
        arrow = findViewById(R.id.arrow_extra_unique);
        expandableLayout = findViewById(R.id.expandableLayout);
        priceValue = findViewById(R.id.price_value);
        locationValue = findViewById(R.id.location_value);
        ratingValue = findViewById(R.id.rating_value);
        inUseValue = findViewById(R.id.inUse_value);

        RelativeLayout inventoryItemLayout = findViewById(R.id.inventoryItemLayout);
        inventoryItemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("InventoryItemClick", "Layout Clicked!");
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                    arrow.animate().rotation(0).setDuration(300).start();
                } else {
                    expandableLayout.expand();
                    arrow.animate().rotation(180).setDuration(300).start();
                }
            }
        });

        Button edit = findViewById(R.id.edit_inventory_item);
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBike != null) {
                    Intent intent = getEditBikeIntent();
                    getContext().startActivity(intent);
                }
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InventoryItem, 0, 0);
            Drawable icon = a.getDrawable(R.styleable.InventoryItem_inventoryIcon);
            String text1 = a.getString(R.styleable.InventoryItem_inventoryText1);

            if (icon != null) {
                inventoryIcon.setImageDrawable(icon);
            }
            if (text1 != null) {
                inventoryText1.setText(text1);
            }
            a.recycle();
        }

        deleteButton = findViewById(R.id.delete_inventory_item);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBikeById(currentBike.getId());
            }
        });
    }

    public void deleteBikeById(int id) {
        String URL = "http://10.0.2.2:8080/bikes/" + id;

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Delete Response", response);
                        Toast.makeText(getContext(), "Bike deleted successfully!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Delete Error", error.toString());
                        Toast.makeText(getContext(), "Failed to delete bike!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void setIcon(Drawable icon) {
        inventoryIcon.setImageDrawable(icon);
    }

    public void setText1(String text) {
        inventoryText1.setText(text);
    }

    public void updateInventoryItem(Bike bike) {
        if (bike.isElectric()) {
            inventoryIcon.setImageResource(R.drawable.electric_icon);
        } else {
            inventoryIcon.setImageResource(R.drawable.become_renter_setting);
        }
        currentBike = bike;
        inventoryText1.setText(bike.getName());
        priceValue.setText(formatPrice(bike.getPrice()));
        locationValue.setText(bike.getLocation());
        ratingValue.setText(bike.getRating()); // directly using the String rating
        inUseValue.setText(bike.isInUse() ? "In Use" : "Available");
    }

    private String formatPrice(int price) {
        return String.format("$%d / hourly", price);
    }

    private Intent getEditBikeIntent() {
        Intent intent = new Intent(getContext(), EditBikeActivity.class);
        intent.putExtra("id", currentBike.getId());
        intent.putExtra("name", currentBike.getName());
        intent.putExtra("price", currentBike.getPrice());
        intent.putExtra("location", currentBike.getLocation());
        intent.putExtra("rating", currentBike.getRating());
        return intent;
    }
}
