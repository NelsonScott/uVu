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

public class CharSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_char_selection);
		
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/00starmaptruetype.ttf");
		TextView tv = (TextView)findViewById(R.id.charSelection);
		tv.setTypeface(typeFace);
		
		final ImageButton girl = (ImageButton)findViewById(R.id.girl);
		girl.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent toBackground = new Intent(CharSelection.this, BackgroundSelection.class);
				toBackground.putExtra("Gender", true);
				startActivity(toBackground);
				finish();
			}
		});
		
		final ImageButton boy = (ImageButton)findViewById(R.id.boy);
		boy.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent toBackground = new Intent(CharSelection.this, BackgroundSelection.class);
				toBackground.putExtra("Gender", false);
				startActivity(toBackground);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.char_selection, menu);
		return true;
	}

}
