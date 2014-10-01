package com.uVu.caffeineRun;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import com.uVu.caffeineRun.R;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
private static final String TAG = "uVuMain";
public SensorManager smanager;
public Boolean playing = false, paused = false, firstPlay = true;
public TextView score;
public MediaPlayer mediaPlayer;
long startTime, elapsedTime, lastUpdate;
float moveIncrement, middle, moveRightLimit, moveLeftLimit, 
difficulty;
Timer timer;
Boolean test = true;
ScoreDB scoreDB;
LinkedList<Integer> ll = new LinkedList<Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.activity_main);
		score = (TextView)findViewById(R.id.score);
		score.setTextColor(Color.WHITE);
								
		//get the sensor and music ready
		smanager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.start();
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("uVuLoop.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            Log.d(TAG, "could not load music: "+e.getMessage());
            mediaPlayer = null;
        }
        
        //mute option 
      		final ImageButton ib = (ImageButton)findViewById(R.id.mute);
      		ib.setOnClickListener(new OnClickListener(){
      			public void onClick(View view){
      				if (mediaPlayer.isPlaying()){
      				mediaPlayer.pause();
      				ib.setImageResource(R.drawable.mute);
      				}
      				else {
      					mediaPlayer.start();
      					ib.setImageResource(R.drawable.unmute);
      				}
      			}
      		});
      		
      	//pause option
      	//should not work if game hasn't started or if it's gameover
      		final ImageButton pause = (ImageButton)findViewById(R.id.pause);
      		pause.setOnClickListener(new OnClickListener(){
      			public void onClick(View view){
      				if (playing & !paused){
      					playing = false;
      					paused = true;
      					pause.setImageResource(R.drawable.play);
      					
      					//stop the background
      					//added for testing
      					RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
      					AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
      					frameAnimation.stop();
      					
      					//stop the character animation
      					ImageView c = (ImageView)findViewById(R.id.mainChar);
      					c.setImageResource(R.drawable.normal_0);
      					
      					if (timer != null){
      						timer.pause();
      					}
      				}
      				else if (!playing & paused) {
      					playing = true;
      					paused = false;
      					pause.setImageResource(R.drawable.pause);
      					
      					//start the background
      					//added for testing
      					RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
      					AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
      					frameAnimation.start();
      					
      					if (timer != null){
      						timer.resume();
      					}
      				}
      			}
      		});
      		pause.setEnabled(false);
        
        //access to scoreboard for adding scores
        CursorFactory cf = null;
        scoreDB = new ScoreDB(this, "scoreDB", cf);
        
        final ImageButton help = (ImageButton)findViewById(R.id.help);
		help.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent h = new Intent(MainActivity.this, Help.class);
	            startActivity(h);
			}
		});
		
		//start the game
		final ImageButton start = (ImageButton)findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {				
				CountDown timer = new CountDown(3000, 500);
				timer.start();
				pause.setEnabled(true);
				start.setEnabled(false);
				help.setEnabled(false);
			}
		});
		
		
	}
	
	//give the player a moment to get ready
	public class CountDown extends CountDownTimer{
		public CountDown(long s, long i){
			super(s, i);
		}
		ImageButton countDown = (ImageButton)findViewById(R.id.start);

		@Override
		public void onFinish() {
			countDown.setImageResource(R.drawable.go);
			playing = true;
			startTime = System.currentTimeMillis();
			score.setVisibility(View.VISIBLE);
			
			//Start background animation
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
			rl.setBackgroundResource(R.drawable.background);
			AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
			frameAnimation.start();
			
			//Start Character animation
			ImageView mainChar = (ImageView)findViewById(R.id.mainChar);
			mainChar.setImageResource(R.drawable.maincharacter);
			
			//Make sure gameover screen is invisible
			ImageView gameOver = (ImageView)findViewById(R.id.gameover);
			gameOver.setVisibility(View.INVISIBLE);
			
			if (firstPlay){
				//Calculate screen movement 
				int[]posXY= new int[2];
				mainChar.getLocationInWindow(posXY);
				middle = posXY[0];
				//changed from .005 to .01 for faster start 
				moveIncrement = (float)(middle*.01); 
				moveRightLimit = (float) (middle+0.5*middle);
				moveLeftLimit= (float)(middle-0.5*middle);
				firstPlay = false;
			}
			mainChar.setX(middle);
						
			//Start the clock
			timer = new Timer(startTime);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			performTick(millisUntilFinished, countDown);
		}
	}
	
	public void performTick(long millisUntilFinished, ImageButton count){
		int remaining = Math.round(millisUntilFinished * 0.001f);
		if (remaining == 3){
			count.setImageResource(R.drawable.num_3);
		}
		else if (remaining == 2){
			count.setImageResource(R.drawable.num_2);
		}
		else if (remaining == 1){
			count.setImageResource(R.drawable.num_1);
		}
	}
	
	public void onSensorChanged(SensorEvent event){
		if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER && playing){
			getAccelerometer(event);
		}
	}
		
	public void getAccelerometer(SensorEvent event){
		//In intervals of 10 seconds increase the difficulty 
		elapsedTime = timer.getElapsedTime()/1000;
		difficulty = ((elapsedTime/10)+1);
						
		//only need the y axis and want avg for smoother controls
		float[]values = event.values;
		int orientation = Math.round(values[1]);
		orientation = smoothing(orientation);
		
		score.setText("Walked "+elapsedTime+" feet.\nLevel: "+difficulty);
						
		//Move across screen in a timed interval
		//Changed from 15 to 25
		if (System.currentTimeMillis()-lastUpdate>25){
			lastUpdate = System.currentTimeMillis();
			move(orientation, difficulty);		
		}
	}
	
	public void move(int oriented, float difficulty){
		//Get character's current position
		int[]posXY= new int[2];
		ImageView mainChar = (ImageView)findViewById(R.id.mainChar);
		mainChar.getLocationInWindow(posXY);
		int currentPosition = posXY[0];
		if (currentPosition > middle){
			
		}
		
		float delta = (float) (moveIncrement*difficulty*2);
		if (oriented>=0){
			if (oriented == 0){
				oriented = 1;
			}
			delta = delta*((float)oriented/10);
//			Check right boundary
			if ((currentPosition+delta)>moveRightLimit){
				mainChar.setX(moveRightLimit);
				mainChar.setImageResource(R.drawable.fall_right);
				gameOver();
			}
			else{
//			Log.d(TAG, "Oriented value: "+oriented+"\nDelta value: "+delta);
				mainChar.setX(currentPosition+delta);
				tilt(mainChar, currentPosition+delta);
			}
			
		}
		else {
			delta = -delta*((float)oriented/10);
//			Check left boundary
			if ((currentPosition-delta)<moveLeftLimit){
				mainChar.setX(moveLeftLimit);
				mainChar.setImageResource(R.drawable.fall_left);
				gameOver();
			}
			else {
//			Log.d(TAG, "Oriented value: "+oriented+"\nDelta value: "+delta);
				mainChar.setX(currentPosition-delta);
				tilt(mainChar, currentPosition-delta);
			}
		}
	}
	
	public void tilt(ImageView mainChar, float position){
		
			if (position< (middle-middle*.10)){
				mainChar.setImageResource(R.drawable.maincharacterleft); 
			}
			else if(position>(middle+middle*.10)){
				mainChar.setImageResource(R.drawable.maincharacterright);
			}
			else {
				mainChar.setImageResource(R.drawable.maincharacter);
			}
	}
	
	public void gameOver(){
		//stop the background
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
		AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
		frameAnimation.stop();
		
		//fade in the gameover screen
		Animation animation = new AlphaAnimation(0, 1);
		animation.setInterpolator(new AccelerateInterpolator());
		animation.setDuration(1000);
		ImageView gameOver = (ImageView)findViewById(R.id.gameover);
		gameOver.setVisibility(View.VISIBLE);
		gameOver.startAnimation(animation);
		
		//Reset buttons
		ImageButton startbtn = (ImageButton)findViewById(R.id.start);
		startbtn.setImageResource(R.drawable.play_again);
		playing = false;
		ImageButton hlp = (ImageButton)findViewById(R.id.help);
		hlp.setEnabled(true);
		startbtn.setEnabled(true);
		
		//update scoreboard
		scoreDB.addScore((int) elapsedTime);
        
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(MainActivity.this, ScoreBoard.class);
		        startActivity(i);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	//take average of last 4 values to smooth controls
	private int smoothing(int value){
		ll.add(value);
		
		if (ll.size()>4){
			ll.removeFirst();
		}
		
		int sum = 0;
		for (Integer i: ll){
			sum+=i;
		}
		
		int avg = Math.round(sum/((float)ll.size()));
		return avg;
	}
	
	@SuppressWarnings("static-access")
	protected void onResume(){
		super.onResume();
		
		if (mediaPlayer != null) {
            mediaPlayer.start();
        }
		
		smanager.registerListener(this, 
		smanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		smanager.SENSOR_DELAY_NORMAL);
	}
	
	protected void onPause() {
		super.onPause();
		smanager.unregisterListener(this);
		
		if (mediaPlayer != null) {
            mediaPlayer.pause();
            if (isFinishing()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}