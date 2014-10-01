package com.android.gif;


import java.io.InputStream;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.graphics.*;

public class GIFDemo extends GraphicsActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GIFView(this));
    }
    private static class GIFView extends View{
    	
    	Movie movie,movie1;
    	InputStream is=null,is1=null;
    	long moviestart;
    	long moviestart1;
		public GIFView(Context context) {
			super(context);
			is=context.getResources().openRawResource(R.drawable.cartoon);
			is1=context.getResources().openRawResource(R.drawable.animated_gif);
			movie=Movie.decodeStream(is);
			movie1=Movie.decodeStream(is1);
			//BitmapFactory.Options opts = new BitmapFactory.Options();
			//opts.inJustDecodeBounds = true;    // this will request the bm
	       // opts.inSampleSize = 10;   
			//movie=Movie.decodeFile("C:\\cartoon.gif");
		}
		
    	@Override
    	protected void onDraw(Canvas canvas) {
    		canvas.drawColor(0xFFCCCCCC);
    		super.onDraw(canvas);
    		long now=android.os.SystemClock.uptimeMillis();
    		System.out.println("now="+now);
    		 if (moviestart == 0) {   // first time
                 moviestart = now;
                 
             }
    		 if(moviestart1==0)
    		 {
    			 moviestart1=now;
    		 }
    		 System.out.println("\tmoviestart="+moviestart);
    		 int relTime = (int)((now - moviestart) % movie.duration()) ;
    		 int relTime1=(int)((now - moviestart1)% movie1.duration());
    		 System.out.println("time="+relTime+"\treltime="+movie.duration());
             movie.setTime(relTime);
             movie1.setTime(relTime1);
             movie.draw(canvas,10,10);
             movie1.draw(canvas,10,100);
             this.invalidate();
    	}
    }
}