package com.akuna.Tshirtwholesalers.Tshirtwholesalers.UserScreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.akuna.Tshirtwholesalers.Tshirtwholesalers.R;
import com.squareup.picasso.Picasso;

public class ZoomImageActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;
    String imgSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_zoom_image);


        mImageView = (ImageView) findViewById(R.id.imageView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Intent data = getIntent();
        imgSrc = data.getExtras().getString("imageSrc");


      //  Toast.makeText(getApplicationContext(), "  Image Src " + imgSrc, Toast.LENGTH_SHORT).show();

       // Picasso.with(getApplicationContext()).load(imgSrc).fit().centerCrop().into(mImageView);

        Picasso.with(this).load(imgSrc).into(mImageView);


    }


    public void img_closeOnClick(View view) {
        finish();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));
            mImageView.setScaleY(mScaleFactor);
            mImageView.setScaleX(mScaleFactor);
            return true;
        }
    }
}
