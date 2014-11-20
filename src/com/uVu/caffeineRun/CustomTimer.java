package com.uVu.caffeineRun;

public class CustomTimer {
	long start;
	long pauseStart;
	long pauseDuration = 0;
	
	public CustomTimer(long s){
		start = s;
	}
	
	public long getElapsedTime(){
		return System.currentTimeMillis() - start - pauseDuration;
	}
	
	public void pause(){
		pauseStart = System.currentTimeMillis();
	}
	
	public void resume(){
		pauseDuration += System.currentTimeMillis() -pauseStart;
	}
}
