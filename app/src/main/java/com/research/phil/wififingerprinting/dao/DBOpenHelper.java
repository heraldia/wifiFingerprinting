package com.research.phil.wififingerprinting.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DBNAME = "wifiFingerPrinting.db";

	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
//		Log.i("DBphil", "DBOpenHelper14");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
//		Log.i("DBphil", "DBOpenHelper--onCreate--21");
		try {
//			db.execSQL("create table IF NOT EXISTS wifirssi "
//					+ "(id integer PRIMARY KEY AutoIncrement NOT NULL,"
//					+ "ORION140 integer,NETGEAR54 integer,Cheese integer,"
//					+ "HPPrint45 integer,Judhajeet integer,NETGEAR17 integer,"
//					+ "DespicableLadies integer,Baking integer, unit204 integer,"
//					+ "location VARCHAR(100),"
//					+ "time DATETIME)");
            db.execSQL("create table IF NOT EXISTS wifiaps "
                    + "(id integer PRIMARY KEY AutoIncrement NOT NULL,"
                    + "wifiaplist VARCHAR(1000),"
                    + "location VARCHAR(100),"
                    + "time DATETIME)");
            db.execSQL("create table IF NOT EXISTS wifissid "
                    + "(id integer PRIMARY KEY AutoIncrement NOT NULL,"
                    + "wifiap VARCHAR(100),"
                    + "ssid VARCHAR(100),"
                    + "location VARCHAR(100),"
                    + "time DATETIME)");
			Log.i("DBphil", "DBOpenHelper--onCreate--28 ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.i("DBphil", "DBOpenHelper32 "+e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	@Override 
	public void onOpen(SQLiteDatabase db) { 
		super.onOpen(db);
		if(!db.isReadOnly()) { 
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;"); 
			} 
		}
	}

