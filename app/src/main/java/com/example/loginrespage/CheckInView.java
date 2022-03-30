package com.example.loginrespage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CheckInView extends RelativeLayout {

    private float scale = 1.0f;

    private OnBtnClickListener onBtnClickListener = null;
    public interface OnBtnClickListener{
        void onClick();
    }

    public CheckInView(@NonNull final Context context, @NonNull final ViewGroup.LayoutParams params, final float scale, @Nullable final OnBtnClickListener listener) {
        super(context);
        this.scale = scale;
        onBtnClickListener = listener;
        initialize(context, params);
    }

    private void initialize(final Context context, final ViewGroup.LayoutParams params){
        LayoutParams rParam;
        params.width = Math.round(640f * scale);
        params.height = Math.round(1136f * scale);
        setLayoutParams(params);
        setBackgroundResource(R.drawable.bgcheckin);

        View checkInButton = new View(context);
        checkInButton.setBackgroundResource(R.drawable.btn_checkin);
        rParam = new LayoutParams(Math.round(580f * scale), Math.round(88f * scale));
        rParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParam.bottomMargin = Math.round(44f * scale);
        addView(checkInButton, rParam);

        setTranslationY(Math.round(-1200f * scale));


        checkInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                close();
                if (onBtnClickListener != null) {
                    onBtnClickListener.onClick();
                }
            }
        });
    }

    public void open(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", Math.round(-1136f * scale), 0f);
        objectAnimator.setStartDelay(0);
        objectAnimator.setDuration(2500);
        objectAnimator.start();
    }

    private void close(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationY", 0f, Math.round(-1136f * scale));
        objectAnimator.setStartDelay(0);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
}
