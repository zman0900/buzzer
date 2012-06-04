package com.cse694;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "BuzzerAccounts.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "Accounts";
	private static final String COL_EMAIL = "Email";
	private static final String COL_PNAME = "PartyName";
	private static final String COL_PASS = "Password";

	public LoginDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_EMAIL
				+ " VARCHAR(255) PRIMARY KEY, " + COL_PNAME + " VARCHAR(255), "
				+ COL_PASS + " VARCHAR(255))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public boolean insertUser(String email, String partyName, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COL_EMAIL, email);
		cv.put(COL_PNAME, partyName);
		cv.put(COL_PASS, password);
		boolean result = true;
		if (db.insert(TABLE_NAME, null, cv) < 0) {
			result = false;
		}
		db.close();
		return result;
	}

	public Boolean checkUser(String email, String password) {
		Boolean result = false;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, COL_EMAIL + "=? AND " + COL_PASS
				+ "=?", new String[]{email, password}, null, null, null);
		if (c.moveToFirst()) {
			// Got a result so login is correct
			result = true;
		}
		db.close();
		return result;
	}

	public String getNameFromEmail(String email) {
		SQLiteDatabase db = this.getReadableDatabase();
		String ans = "";
		Cursor c = db.query(TABLE_NAME, null, COL_EMAIL+"=?", new String[] {email}, null, null, null);
		if (c.moveToFirst()) {
			ans = c.getString(1);
		}
		db.close();
		return ans;
	}
}
