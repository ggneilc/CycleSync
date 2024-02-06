package com.example.cyclesync;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomComponent extends RelativeLayout {

    private ImageView speakerIcon;
    private TextView notificationsText;
    private OnComponentClickListener onComponentClickListener;

    public CustomComponent(Context context) {
        super(context);
        init(context, null);
    }

    public CustomComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.setting_item, this, true);
        speakerIcon = findViewById(R.id.speaker_icon);
        notificationsText = findViewById(R.id.notifications_text);

        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomComponent);
            Drawable icon = attributes.getDrawable(R.styleable.CustomComponent_speakerIcon);
            String text = attributes.getString(R.styleable.CustomComponent_notificationsText);

            if (icon != null) {
                speakerIcon.setImageDrawable(icon);
            }

            if (text != null) {
                notificationsText.setText(text);
            }

            attributes.recycle();
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onComponentClickListener != null) {
                    onComponentClickListener.onComponentClick(view);
                }
            }
        });
    }

    public void setOnComponentClickListener(OnComponentClickListener listener) {
        this.onComponentClickListener = listener;
    }

    public void setSpeakerIcon(Drawable drawable) {
        speakerIcon.setImageDrawable(drawable);
    }

    public void setNotificationsText(String text) {
        notificationsText.setText(text);
    }

    public interface OnComponentClickListener {
        void onComponentClick(View view);
    }
}
