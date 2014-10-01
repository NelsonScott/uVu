package com.uVu.caffeineRun;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDB extends SQLiteOpenHelper{
	private static final int VERSION = 1;
	private static final String KEY_SCORE = "Score";
	private static final String SCORE_TABLE = "ScoreTable";

	public ScoreDB(Context context, String name, CursorFactory factory) {
		super(context, name, factory, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}
	
	public void createTable(SQLiteDatabase db){
		String createScoreTable = "CREATE TABLE "+SCORE_TABLE+ "(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SCORE+" TEXT" +
				");";
		db.execSQL(createScoreTable);
	}
	
	public void addScore(int score){
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_SCORE, score);
		db.insert(SCORE_TABLE, null, values);
		db.close();
	}
	
	public void deleteAllScores(){
		SQLiteDatabase db = this.getWritableDatabase();
		String delete = "DELETE FROM "+SCORE_TABLE+";";
		db.execSQL(delete);
		db.close();
	}
	
	public int[] getAllScores() {

	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + SCORE_TABLE;

	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    // looping through all rows and adding to list

	    int i = 0;

	    String[] data = new String[cursor.getCount()];

	    if (cursor.moveToFirst()){
	    	do {
	    		data[i] = cursor.getString(1);
	    		i++;
	    		
	    	} while (cursor.moveToNext());
	    }
	    int [] intData = convert(data);
	    Arrays.sort(intData);
	    
	    //reverse the array
        for (int k = 0; k < intData.length / 2; k++) {
        	  int temp = intData[k];
        	  intData[k] = intData[intData.length - 1 - k];
        	  intData[intData.length - 1 - k] = temp;
        	}

	    cursor.close();
	    db.close();
	    
	    // return only top 10 scores
	    return Arrays.copyOfRange(intData, 0, 9);
	}
	
	private int[] convert(String[] string) {
	    int number[] = new int[string.length];

	    for (int i = 0; i < string.length; i++) {
	        number[i] = Integer.parseInt(string[i]);
	    }
	   return number;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String dropSQL = "DROP TABLE IF EXISTS"+SCORE_TABLE+";"; 
		db.execSQL(dropSQL);
		createTable(db);
		
	}

}
