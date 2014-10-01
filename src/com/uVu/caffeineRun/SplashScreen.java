package com.uVu.caffeineRun;

import com.uVu.caffeineRun.R;
import com.uVu.caffeineRun.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	private static final String TAG = "splashscreen";
//	Changed from 3000 to 100 for development
	private static final int SPLASH_TIME_OUT = 100;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splashscreen);
//	    ImageView iv = (ImageView) findViewById(R.id.splashview);
//	    Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//	    int height = bMap.getHeight();
//	    int width = bMap.getWidth();
//	    int scale = 5;
//	    bMap = Bitmap.createScaledBitmap(bMap, width*scale, height*scale, true);
//	    iv.setImageBitmap(bMap);
	    
	    new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
	    // TODO Auto-generated method stub
	}
}
