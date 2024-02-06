package com.example.as1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.cachapa.expandablelayout.ExpandableLayout;




public class InventoryItem extends LinearLayout {
    private ExpandableLayout expandableLayout;

    private ImageView inventoryIcon;
    private TextView inventoryText1;
    private TextView inventoryText2;

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
        inventoryText2 = findViewById(R.id.inventoryText2);

        // Initialize the ExpandableLayout and set its click listener
        expandableLayout = findViewById(R.id.expandableLayout);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                } else {
                    expandableLayout.expand();
                }
            }
        });

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InventoryItem, 0, 0);

            Drawable icon = a.getDrawable(R.styleable.InventoryItem_inventoryIcon);
            String text1 = a.getString(R.styleable.InventoryItem_inventoryText1);
            String text2 = a.getString(R.styleable.InventoryItem_inventoryText2);

            if (icon != null) {
                inventoryIcon.setImageDrawable(icon);
            }
            if (text1 != null) {
                inventoryText1.setText(text1);
            }
            if (text2 != null) {
                inventoryText2.setText(text2);
            }

            a.recycle();
        }
    }

    public void setIcon(Drawable icon) {
        inventoryIcon.setImageDrawable(icon);
    }

    public void setText1(String text) {
        inventoryText1.setText(text);
    }

    public void setText2(String text) {
        inventoryText2.setText(text);
    }
}
