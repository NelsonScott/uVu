package com.uVu.caffeineRun;

import java.util.Arrays;

import com.uVu.caffeineRun.R;
import com.uVu.caffeineRun.R.layout;
import com.uVu.caffeineRun.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScoreBoard extends Activity {
	ScoreDB scoreData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_board);
		
		//get the scores
		CursorFactory cf = null;
        scoreData = new ScoreDB(this, "scoreDB", cf);
        
        //format and list them
		int[] allScores = scoreData.getAllScores();
		int[] firstHalf = Arrays.copyOfRange(allScores, 0, allScores.length/2);
		int [] secondHalf = Arrays.copyOfRange(allScores, allScores.length/2 + 1, allScores.length);
		
		String result1 = makeString(firstHalf, 0);
		String result2 = makeString(secondHalf, allScores.length/2);
		
		TextView score1 = (TextView)findViewById(R.id.scoreBoard1);
		TextView score2 = (TextView)findViewById(R.id.scoreBoard2);
		
		score1.setText(result2);
		score1.setTextColor(Color.WHITE);
		score2.setText(result1);
		score2.setTextColor(Color.WHITE);
		
		final ImageButton back = (ImageButton)findViewById(R.id.scoreBackButton);
		back.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				finish();
			}
		});
	}
	
	public String makeString(int[] s, int start){
		String str = "";
		for (int i=0; i<s.length; i++){
			str+="Score "+(i+1+start)+": "+s[i]+"\n";
		}
		return str;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score_board, menu);
		return true;
	}

}
