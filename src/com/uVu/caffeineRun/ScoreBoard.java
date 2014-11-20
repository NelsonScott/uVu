package com.uVu.caffeineRun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScoreBoard extends Activity {
	private static final String TAG = "ScoreBoard Log";
	ScoreDB scoreData;
	String MyPREFERENCES = "ScorePrefs";
	String initials = "";
	int newScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_board);
		final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);	
		final Editor editor = sharedpreferences.edit();
		newScore = getIntent().getIntExtra("newScore", 0);
		Log.d(TAG, "Value of newScore: "+newScore);
		
		final TextView[] textViewArray = {(TextView)findViewById(R.id.scoreBoard1),
				(TextView)findViewById(R.id.scoreBoard2),
				(TextView)findViewById(R.id.scoreBoard3),
				(TextView)findViewById(R.id.scoreBoard4),
				(TextView)findViewById(R.id.scoreBoard5),
				(TextView)findViewById(R.id.scoreBoard6),
				(TextView)findViewById(R.id.scoreBoard7),
				(TextView)findViewById(R.id.scoreBoard8),
				(TextView)findViewById(R.id.scoreBoard9),
				(TextView)findViewById(R.id.scoreBoard10)};
		
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/00starmaptruetype.ttf");
		TextView title = (TextView)findViewById(R.id.TopBar);
		title.setTypeface(typeFace);
		//for some reason, can't set typeface in xml
		for (TextView tv : textViewArray){
			tv.setTypeface(typeFace);
			tv.setTextSize(20);
		}
		
		//Check they're all there, initialize if not
		for (int i=1; i<=10;i++){
			if (sharedpreferences.getInt("Score"+i, 0)==0){
				editor.putInt("Score"+i, i);
				editor.commit();
			}
			if (sharedpreferences.getString("Initials"+i, "AAA")=="AAA"){
				editor.putString("Initials"+i, "AAA");
				editor.commit();
			}
		}
				
		//get user initials if in top 10
		if (checkTopTen(sharedpreferences, editor, newScore)){
			Log.d(TAG, "Score found to be in top 10");
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Initials");
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
			int maxLength = 3;    
			input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
			builder.setView(input);
			
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	initials = input.getText().toString();
			    	
			    	//Update the scores
			    	for (int i=10; i>0; i--){
			    		if (newScore>sharedpreferences.getInt("Score"+i, Integer.MAX_VALUE)){
			    			Log.d(TAG, "Found "+newScore+" to be larger than "+sharedpreferences.getInt("Score"+i, Integer.MAX_VALUE));
			    			int temp = sharedpreferences.getInt("Score"+i,0);
			    			String tempStr = sharedpreferences.getString("Initials"+i, "AAA");
			    			
			    			editor.putInt("Score"+i, newScore);
			    			editor.putString("Initials"+i, initials);
			    			editor.commit();
			    			newScore = temp;
			    			initials = tempStr;
			    		}
			    	}
			    	Log.d(TAG,"Finished updating scores");

					//Show the scores
					for (int i=1;i<=10; i++){
						textViewArray[i-1].setText(sharedpreferences.getString("Initials"+i, "AAA")+" "
								+sharedpreferences.getInt("Score"+i, 0));
					}
					Log.d(TAG, "Finished displaying them");
			    }
			});
			builder.show();
		}
		else {
			//No update, show scores
			for (int i=1;i<=10; i++){
				textViewArray[i-1].setText(sharedpreferences.getString("Initials"+i, "AAA")+" "
						+sharedpreferences.getInt("Score"+i, 0));
			}
			Log.d(TAG, "Finished displaying, no update");
			//
		}
		
		//get the scores
//		CursorFactory cf = null;
//        scoreData = new ScoreDB(this, "scoreDB", cf);
        
        //format and list them
//		int[] allScores = scoreData.getAllScores();
//		int[] firstHalf = Arrays.copyOfRange(allScores, 0, allScores.length/2);
//		int [] secondHalf = Arrays.copyOfRange(allScores, allScores.length/2 + 1, allScores.length);
//		
//		String result1 = makeString(firstHalf, 0);
//		String result2 = makeString(secondHalf, allScores.length/2);
//		
//		TextView score1 = (TextView)findViewById(R.id.scoreBoard1);
//		TextView score2 = (TextView)findViewById(R.id.scoreBoard2);
//		
//		score1.setText(result2);
//		score1.setTextColor(Color.WHITE);
//		score2.setText(result1);
//		score2.setTextColor(Color.WHITE);
		
		final ImageButton back = (ImageButton)findViewById(R.id.scoreBackButton);
		back.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				finish();
			}
		});
	}
	
	public boolean checkTopTen(SharedPreferences sp, Editor e, int score){
		
		for (int i=1;i<=10;i++){
			if (score>sp.getInt("Score"+i, Integer.MAX_VALUE)){
				return true;
			}
		}
		return false;
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
