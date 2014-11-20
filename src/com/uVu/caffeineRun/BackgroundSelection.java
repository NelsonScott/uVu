package com.uVu.caffeineRun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BackgroundSelection extends Activity {

	private static final String TAG = "BackgroundAct";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background_selection);
		
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/00starmaptruetype.ttf");
		TextView tv = (TextView)findViewById(R.id.stageSelection);
		tv.setTypeface(typeFace);

		final ImageButton city1 = (ImageButton)findViewById(R.id.city1);
		city1.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent backToMain = new Intent(BackgroundSelection.this, MainActivity.class);
				backToMain.putExtra("Background", 1);
				backToMain.putExtra("Gender", getIntent().getBooleanExtra("Gender", false));
				startActivity(backToMain);
				finish();
			}
		});
		
		final ImageButton city2 = (ImageButton)findViewById(R.id.city2);
		city2.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent backToMain = new Intent(BackgroundSelection.this, MainActivity.class);
				backToMain.putExtra("Background", 2);
				backToMain.putExtra("Gender", getIntent().getBooleanExtra("Gender", false));
				startActivity(backToMain);
				finish();
			}
		});
		
		final ImageButton underwater = (ImageButton)findViewById(R.id.underwater);
		underwater.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent backToMain = new Intent(BackgroundSelection.this, MainActivity.class);
				backToMain.putExtra("Background", 3);
				backToMain.putExtra("Gender", getIntent().getBooleanExtra("Gender", false));
				startActivity(backToMain);
				finish();
			}
		});
		
		final ImageButton space = (ImageButton)findViewById(R.id.space);
		space.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent backToMain = new Intent(BackgroundSelection.this, MainActivity.class);
				backToMain.putExtra("Background", 4);
				backToMain.putExtra("Gender", getIntent().getBooleanExtra("Gender", false));
				startActivity(backToMain);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.background_selection, menu);
		return true;
	}

}
