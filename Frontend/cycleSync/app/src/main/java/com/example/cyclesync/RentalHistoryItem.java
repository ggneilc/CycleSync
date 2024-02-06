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

public class RentalHistoryItem extends RelativeLayout {
    private ExpandableLayout expandableLayout;
    private ImageView rentalIcon;
    private ImageView arrow;
    private TextView rentalHistoryText1;
    private TextView renter_value;
    private TextView locationValue;
    private TextView ratingValue;

    private TextView reviewValue;

    public RentalHistoryItem(Context context) {
        super(context);
        init(null);
    }

    public RentalHistoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.rental_history_item, this, true);

        rentalIcon = findViewById(R.id.rentalIcon);
        rentalHistoryText1 = findViewById(R.id.rentalHistoryText1);
        arrow = findViewById(R.id.arrow_extra_unique);
        expandableLayout = findViewById(R.id.expandableLayout);
        renter_value = findViewById(R.id.renter_value);
        locationValue = findViewById(R.id.location_value);
        ratingValue = findViewById(R.id.rating_value);
        reviewValue = findViewById(R.id.review_value);

        RelativeLayout rentalHistoryItemLayout = findViewById(R.id.rentalHistoryItemLayout);
        rentalHistoryItemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("RentalHistoryItemClick", "Layout Clicked!");
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                    arrow.animate().rotation(0).setDuration(300).start();
                } else {
                    expandableLayout.expand();
                    arrow.animate().rotation(180).setDuration(300).start();
                }
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InventoryItem, 0, 0);
            Drawable icon = a.getDrawable(R.styleable.InventoryItem_inventoryIcon);
            String text1 = a.getString(R.styleable.InventoryItem_inventoryText1);

            if (icon != null) {
                rentalIcon.setImageDrawable(icon);
            }
            if (text1 != null) {
                rentalHistoryText1.setText(text1);
            }
            a.recycle();
        }
    }

    public void setIcon(Drawable icon) {
        rentalIcon.setImageDrawable(icon);
    }

    public void setText1(String text) {
        rentalHistoryText1.setText(text);
    }

    public void updateRentalItem(RentalPreview rental) {
        if (rental.isElectric()) {
            rentalIcon.setImageResource(R.drawable.electric_icon);
        } else {
            rentalIcon.setImageResource(R.drawable.become_renter_setting);
        }
        rentalHistoryText1.setText(rental.getBike_name());
//        rentalHistoryText1.setText(new SimpleDateFormat("MM-dd-yy").format(rental.getStart_date()) + "-" + new SimpleDateFormat("MM-dd-yy").format(rental.getStop_date()));
        renter_value.setText(rental.getRenter_name());
        locationValue.setText(rental.getLocation());
        ratingValue.setText(rental.getRating());
        reviewValue.setText(rental.getReview());
    }
}
