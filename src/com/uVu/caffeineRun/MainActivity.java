package com.uVu.caffeineRun;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{
	public SensorManager smanager;
	public Boolean playing = false, paused = false, firstPlay = true, isGirl, muted = false, ragTime = false;
	public TextView scoreHolder;
	public ImageButton bonusLeft, bonusRight;
	public MediaPlayer mediaPlayer, endingMediaPlayer;
	long startTime, elapsedTime, lastUpdate;
	float moveIncrement, middle, moveRightLimit, moveLeftLimit, gravity, gravity_value, 
	difficulty;
	Timer bonusTimer, obstacleTimer;
	int backgroundChoice, mainTilt, mainNormal, mainStill, fallLeft, fallRight, bonus;
	CustomTimer timer;
	ScoreDB scoreDB;
	LinkedList<Integer> ll = new LinkedList<Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		scoreHolder = (TextView)findViewById(R.id.scoreandlevel);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Chunkfive Ex.ttf");
		scoreHolder.setTypeface(typeFace);
		scoreHolder.setVisibility(View.INVISIBLE);

		Intent recieve = getIntent();
		isGirl = recieve.getBooleanExtra("Gender", false);
		backgroundChoice = recieve.getIntExtra("Background", 0);
		ImageView main = (ImageView) findViewById(R.id.mainChar);
		ImageView mainHidden = (ImageView)findViewById(R.id.mainCharHidden);
		ImageView obstacleLeft = (ImageView)findViewById(R.id.obstacleLeft);
		ImageView obstacleRight = (ImageView)findViewById(R.id.obstacleRight);
		if (backgroundChoice==1){
			//default
			obstacleLeft.setImageResource(R.drawable.catnormal);
			obstacleRight.setImageResource(R.drawable.catnormal);
			if (isGirl){
				mainNormal = R.drawable.maincharacterfemale;
				mainTilt = R.drawable.maincharacterfemaletilt;
				fallLeft = R.drawable.char20017;
				fallRight = R.drawable.char20018;
				mainStill = R.drawable.char20001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacterfemaletilt);
			}
			else{
				mainNormal = R.drawable.maincharacter;
				mainTilt = R.drawable.maincharactertilt;
				fallLeft = R.drawable.fall_left;
				fallRight = R.drawable.fall_right;
				mainStill = R.drawable.char10001;
				main.setImageResource(mainStill);
			}
		}
		else if (backgroundChoice==2) {
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
			rl.setBackgroundResource(R.drawable.bgcity20001);
			obstacleLeft.setImageResource(R.drawable.catnormal);
			obstacleRight.setImageResource(R.drawable.catnormal);

			if (isGirl){
				mainNormal = R.drawable.maincharacterfemale;
				mainTilt = R.drawable.maincharacterfemaletilt;
				fallLeft = R.drawable.char20017;
				fallRight = R.drawable.char20018;
				mainStill = R.drawable.char20001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacterfemaletilt);
			}
			else{
				mainNormal = R.drawable.maincharacter;
				mainTilt = R.drawable.maincharactertilt;
				fallLeft = R.drawable.fall_left;
				fallRight = R.drawable.fall_right;
				mainStill = R.drawable.char10001;
				main.setImageResource(mainStill);
			}
		}
		else if (backgroundChoice==3){
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
			rl.setBackgroundResource(R.drawable.bg_underwater0001);

			obstacleLeft.setImageResource(R.drawable.catunderwater);
			obstacleRight.setImageResource(R.drawable.catunderwater);


			if (isGirl){
				mainNormal = R.drawable.maincharacterfemale_underwater;
				mainTilt = R.drawable.maincharacterfemale_underwatertilt;
				fallLeft = R.drawable.char2_underwater0017;
				fallRight = R.drawable.char2_underwater0018;
				mainStill = R.drawable.char2_underwater0001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacterfemale_underwatertilt);
			}
			else {
				mainNormal = R.drawable.maincharacter_underwater;
				mainTilt = R.drawable.maincharacter_underwatertilt;
				fallLeft = R.drawable.char1_underwater0017;
				fallRight = R.drawable.char1_underwater0018;
				mainStill = R.drawable.char1_underwater0001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacter_underwatertilt);
			}

		}
		else if (backgroundChoice==4){
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
			rl.setBackgroundResource(R.drawable.bg_space0001);

			obstacleLeft.setImageResource(R.drawable.catspace);
			obstacleRight.setImageResource(R.drawable.catspace);

			if (isGirl){
				mainNormal = R.drawable.maincharacterfemale_space;
				mainTilt = R.drawable.maincharacterfemale_spacetilt;
				fallLeft = R.drawable.char2astronaut0017;
				fallRight = R.drawable.char2astronaut0018;
				mainStill = R.drawable.char2astronaut0001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacterfemale_spacetilt);
			}
			else{
				mainNormal = R.drawable.maincharacter_space;
				mainTilt = R.drawable.maincharacter_spacetilt;
				fallLeft = R.drawable.char1astronaut0017;
				fallRight = R.drawable.char1astronaut0018;
				mainStill = R.drawable.char1astronaut0001;
				main.setImageResource(mainStill);
				mainHidden.setImageResource(R.drawable.maincharacter_spacetilt);
			}
		}

		//get the sensor, music, and bonus ready
		smanager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		bonusLeft = (ImageButton)findViewById(R.id.bonusLeft);
		bonusRight = (ImageButton)findViewById(R.id.bonusRight);
		bonusLeft.setClickable(false);
		bonusRight.setClickable(false);

		mediaPlayer = new MediaPlayer();
		endingMediaPlayer = new MediaPlayer();
		try {
			AssetManager assetManager = getAssets();
			AssetFileDescriptor descriptor = assetManager.openFd("uVuLoop.mp3");
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			mediaPlayer.prepare();
			mediaPlayer.setLooping(true);
			if (!muted){
				mediaPlayer.start();
			}
			AssetFileDescriptor des = assetManager.openFd("gameOverSound.mp3");
			endingMediaPlayer.setDataSource(des.getFileDescriptor(),des.getStartOffset(),des.getLength());
			endingMediaPlayer.setLooping(false);
			endingMediaPlayer.prepare();

		} catch (IOException e) {
			mediaPlayer = null;
		}

		final ImageButton reset = (ImageButton)findViewById(R.id.reset);
		reset.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent reset = new Intent(MainActivity.this, CharSelection.class);
				startActivity(reset);
				finish();
			}
		});

		//mute option 
		final ImageButton ib = (ImageButton)findViewById(R.id.mute);
		ib.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				if (!muted){
					mediaPlayer.pause();
					ib.setImageResource(R.drawable.mute);
					muted = true;
				}
				else if (muted) {
					mediaPlayer.start();
					muted = false;
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
					RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
					AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
					frameAnimation.stop();

					//stop the character animation
					ImageView c = (ImageView)findViewById(R.id.mainChar);
					//need to fix this
					c.setImageResource(mainStill);

					if (timer != null){
						timer.pause();
					}
				}
				else if (!playing & paused) {
					playing = true;
					paused = false;
					pause.setImageResource(R.drawable.pause);

					//start the background
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
		//		CursorFactory cf = null;
		//		scoreDB = new ScoreDB(this, "scoreDB", cf);

		//help screen
		final ImageButton help = (ImageButton)findViewById(R.id.help);
		help.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				Intent h = new Intent(MainActivity.this, Help.class);
				startActivity(h);
			}
		});

		//bonus hits
		bonusLeft.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				bonus+=15;
				bonusLeft.setVisibility(View.INVISIBLE);
			}
		});
		bonusRight.setOnClickListener(new OnClickListener(){
			public void onClick(View view){
				bonus+=15;
				bonusRight.setVisibility(View.INVISIBLE);
			}
		});

		//start the game
		final ImageButton start = (ImageButton)findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				bonus = 0;
				bonusLeft.setClickable(true);
				bonusRight.setClickable(true);
				ImageView obstacleLeft = (ImageView)findViewById(R.id.obstacleLeft);
				ImageView obstacleRight = (ImageView)findViewById(R.id.obstacleRight);
				obstacleLeft.setVisibility(View.VISIBLE);
				obstacleRight.setVisibility(View.VISIBLE);

				CountDown timer = new CountDown(3000, 500);
				timer.start();
				pause.setEnabled(true);
				start.setEnabled(false);
				help.setVisibility(View.INVISIBLE);
				reset.setVisibility(View.INVISIBLE);

				//start the bonus timer
				startBonusTimer();
				startObstacleTimer();
			}
		});
	}

	public boolean onTouchEvent(MotionEvent event){
		if (event.getPointerCount()>2){
			//switch to ragtime music
			if (!ragTime){
				mediaPlayer.pause();
				MediaPlayer ragMediaPlayer = new MediaPlayer();
				try {
					AssetManager assetManager = getAssets();
					AssetFileDescriptor descriptor = assetManager.openFd("pr_mapleleafrag.mp3");
					ragMediaPlayer.setDataSource(descriptor.getFileDescriptor(),
							descriptor.getStartOffset(), descriptor.getLength());
					ragMediaPlayer.prepare();
					ragMediaPlayer.setLooping(true);

				} catch (IOException e) {
					ragMediaPlayer = null;
				}
				mediaPlayer.reset();
				mediaPlayer = null;
				mediaPlayer = ragMediaPlayer;
				mediaPlayer.start();
				ragTime = true;
			}
			//go back to original music
			else {
				mediaPlayer.pause();
				MediaPlayer regularMediaPlayer = new MediaPlayer();
				try {
					AssetManager assetManager = getAssets();
					AssetFileDescriptor descriptor = assetManager.openFd("uVuLoop.mp3");
					regularMediaPlayer.setDataSource(descriptor.getFileDescriptor(),
							descriptor.getStartOffset(), descriptor.getLength());
					regularMediaPlayer.prepare();
					regularMediaPlayer.setLooping(true);

				} catch (IOException e) {
					regularMediaPlayer = null;
				}
				mediaPlayer.reset();
				mediaPlayer = null;
				mediaPlayer = regularMediaPlayer;
				mediaPlayer.start();
				ragTime = false;
			}
		}
		return true;
	}

	public void startObstacleTimer(){
		obstacleTimer = new Timer();
		TimerTask ObTask = new TimerTask(){  
			@Override  
			public void run() {  
				runOnUiThread(new Runnable() {  

					@Override  
					public void run() { 
						//Obstacle appears
						if (Math.random()>.25){
							if (Math.random()>.5){
								final ImageView obstacleRight = (ImageView)findViewById(R.id.obstacleRight);
								obstacleRight.setScaleX(-1);
								//								obstacleRight.setVisibility(View.VISIBLE);
								obstacleRight.animate().setDuration(3000).translationX(-2*obstacleRight.getWidth()).withEndAction(new Runnable(){
									public void run(){
										obstacleRight.animate().setDuration(500).scaleX(1).withEndAction(new Runnable(){
											public void run(){
												obstacleRight.animate().setStartDelay(500).setDuration(2000).translationX(2*obstacleRight.getWidth());
											}
										});
									}
								});
							}
							else {
								final ImageView obstacleLeft = (ImageView)findViewById(R.id.obstacleLeft);
								//changed scale so pointing right way
								obstacleLeft.setScaleX(1);
								//								obstacleLeft.setVisibility(View.VISIBLE);
								obstacleLeft.animate().setDuration(3000).translationX(2*obstacleLeft.getWidth()).withEndAction(new Runnable(){
									public void run(){
										obstacleLeft.animate().setDuration(500).scaleX(-1).withEndAction(new Runnable(){
											public void run(){
												obstacleLeft.animate().setStartDelay(500).setDuration(2000).translationX(-2*obstacleLeft.getWidth());
											}
										});
									}
								});
							}
						}
					}
				});  
			}  
		};

		obstacleTimer.scheduleAtFixedRate(ObTask, 3000, 6000);
	}

	public void startBonusTimer(){
		bonusTimer = new Timer();
		TimerTask task = new TimerTask(){  
			@Override  
			public void run() {  
				runOnUiThread(new Runnable() {  

					@Override  
					public void run() { 
						double x = Math.random();
						if (playing && x>.5){
							if (Math.random()>.5){
								bonusLeft.setVisibility(View.VISIBLE);
								bonusRight.setVisibility(View.INVISIBLE);
							}
							else{
								bonusLeft.setVisibility(View.INVISIBLE);
								bonusRight.setVisibility(View.VISIBLE);
							}
						}
						else{
							bonusLeft.setVisibility(View.INVISIBLE);
							bonusRight.setVisibility(View.INVISIBLE);
						}
					}
				});  
			}  
		};

		bonusTimer.scheduleAtFixedRate(task, 3000, 2000);
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
			scoreHolder.setVisibility(View.VISIBLE);

			if (!muted && !mediaPlayer.isPlaying()){
				mediaPlayer.start();
			}

			//Start background animation
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
			if (backgroundChoice == 1){
				rl.setBackgroundResource(R.drawable.background);
			}
			else if (backgroundChoice == 2){
				rl.setBackgroundResource(R.drawable.background_city2);
			}
			else if (backgroundChoice == 3){
				rl.setBackgroundResource(R.drawable.background_underwater);
			}
			else if (backgroundChoice == 4){
				rl.setBackgroundResource(R.drawable.background_space);
			}
			AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
			frameAnimation.start();

			//Start Character animation
			ImageView mainChar = (ImageView)findViewById(R.id.mainChar);
			if (isGirl){
				mainChar.setImageResource(R.drawable.maincharacterfemale);
			}
			else {
				mainChar.setImageResource(R.drawable.maincharacter);
			}

			//Make sure gameover screen is invisible
			ImageView gameOver = (ImageView)findViewById(R.id.gameover);
			gameOver.setVisibility(View.INVISIBLE);
			ImageView dog = (ImageView)findViewById(R.id.dog);
			dog.setVisibility(View.INVISIBLE);

			if (firstPlay){
				//Calculate screen movement 
				int[]posXY= new int[2];
				mainChar.getLocationInWindow(posXY);
				middle = posXY[0];
				moveIncrement = (float)(middle*.01); 
				//gravity to make it harder to control
				gravity = (float) (.5*moveIncrement);
				//made right limit a little larger
				moveRightLimit = (float) (middle+0.6*middle);
				moveLeftLimit= (float)(middle-0.5*middle);
				firstPlay = false;
			}
			mainChar.setX(middle);

			//Start the clock
			timer = new CustomTimer(startTime);

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

		int joined = (int)elapsedTime+bonus;
		scoreHolder = (TextView)findViewById(R.id.scoreandlevel);
		scoreHolder.setText("Level: "+(int)difficulty+"\nScore: "+joined);

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

		if (currentPosition>middle){
			gravity_value = (Math.abs(gravity)*(difficulty+4))/3;
		}
		else {
			gravity_value = (-gravity*(difficulty+4))/3;
		}

		//Check if hit an obstacle
		int[]obstacleRightposXY = new int[2];
		ImageView obstacleRight = (ImageView)findViewById(R.id.obstacleRight);
		obstacleRight.getLocationInWindow(obstacleRightposXY);
		int obstacleRightCurrentPosition = obstacleRightposXY[0];
		if ((currentPosition+mainChar.getWidth()-110)>= obstacleRightCurrentPosition){
			playing = false;
			mainChar.setImageResource(fallRight);
			gameOver();
			return;
		}

		int[]obstacleLeftposXY = new int[2];
		ImageView obstacleLeft = (ImageView)findViewById(R.id.obstacleLeft);
		obstacleLeft.getLocationInWindow(obstacleLeftposXY);
		int obstacleLeftCurrentPosition = obstacleLeftposXY[0];
		if (currentPosition+110<= (obstacleLeftCurrentPosition)){
			playing = false;
			mainChar.setImageResource(fallLeft);
			gameOver();
			return;
		}

		float delta = (float) (moveIncrement*difficulty*2);
		if (oriented>=0){
			if (oriented == 0){
				oriented = 1;
			}
			delta = delta*((float)oriented/10);
			//Check right boundary
			if ((currentPosition+delta)>moveRightLimit){
				mainChar.setX(moveRightLimit);
				mainChar.setImageResource(fallRight);
				gameOver();
			}
			else{
				mainChar.setX(currentPosition+delta+gravity_value);
				tilt(mainChar, currentPosition+delta);
			}

		}
		else {
			delta = -delta*((float)oriented/10);
			//Check left boundary
			if ((currentPosition-delta)<moveLeftLimit){
				mainChar.setX(moveLeftLimit);
				mainChar.setImageResource(fallLeft);
				gameOver();
			}
			else {
				mainChar.setX(currentPosition-delta+gravity_value);
				tilt(mainChar, currentPosition-delta);
			}
		}
	}

	public boolean collision(int characterX,int obstacleX){
		//should hit a little inside the frame
		if ((characterX)>=obstacleX){
			ImageButton p = (ImageButton)findViewById(R.id.pause);
			p.performClick();
			return true;
		}
		return false;
	}

	public void tilt(ImageView mainChar, float position){
		if (position< (middle-middle*.10)){
			mainChar.setImageResource(mainTilt); 
		}
		else if(position>(middle+middle*.10)){
			mainChar.setImageResource(mainTilt);
		}
		else {
			mainChar.setImageResource(mainNormal);
		}
	}

	public void gameOver(){
		//stop the background
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.main);
		AnimationDrawable frameAnimation = (AnimationDrawable) rl.getBackground();
		frameAnimation.stop();
		bonusTimer.cancel();
		bonusTimer.purge();
		bonusLeft.setVisibility(View.INVISIBLE);
		bonusRight.setVisibility(View.INVISIBLE);
		obstacleTimer.cancel();
		obstacleTimer.purge();
		ImageView obstacleLeft=(ImageView)findViewById(R.id.obstacleLeft);
		ImageView obstacleRight=(ImageView)findViewById(R.id.obstacleRight);
		obstacleLeft.setVisibility(View.INVISIBLE);
		obstacleRight.setVisibility(View.INVISIBLE);

		//stop the music and put the gameover sound in
		mediaPlayer.pause();
		endingMediaPlayer.start();

		//fade in the gameover screen
		Animation gameOverAnimation = new AlphaAnimation(0, 1);
		gameOverAnimation.setInterpolator(new AccelerateInterpolator());
		gameOverAnimation.setDuration(2500);
		ImageView gameOver = (ImageView)findViewById(R.id.gameover);
		gameOver.setVisibility(View.VISIBLE);
		gameOver.startAnimation(gameOverAnimation);

		//Put in the laughing dog
		ImageView dog = (ImageView)findViewById(R.id.dog);
		dog.setVisibility(View.VISIBLE);
		dog.setImageResource(R.drawable.dog_laugh);
		Animation an = new TranslateAnimation(0,0,dog.getY(),0);
		an.setDuration(1000);
		dog.startAnimation(an);

		//Reset buttons
		ImageButton startbtn = (ImageButton)findViewById(R.id.start);
		ImageButton hlp = (ImageButton)findViewById(R.id.help);
		ImageButton reset = (ImageButton)findViewById(R.id.reset);
		startbtn.setImageResource(R.drawable.play_again);
		playing = false;
		hlp.setVisibility(View.VISIBLE);
		startbtn.setVisibility(View.VISIBLE);
		startbtn.setEnabled(true);
		reset.setVisibility(View.VISIBLE);

		//update scoreboard
		//		scoreDB.addScore((int) elapsedTime);

		gameOverAnimation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ScoreBoard.class);
				i.putExtra("newScore", (int)elapsedTime);
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


		if (!muted && (mediaPlayer != null)) {
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