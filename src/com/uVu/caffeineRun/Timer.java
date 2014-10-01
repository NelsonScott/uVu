package com.uVu.caffeineRun;

public class Timer {
	long start;
	long pauseStart;
	long pauseDuration = 0;
	
	public Timer(long s){
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
