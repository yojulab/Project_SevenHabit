package com.example.ohsanghun.nicehabit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBProvider implements Constants {

	private final Context context;
	private SQLiteDatabase db ;
	private NoteOpenHelper dbHelper;
	
	public DBProvider(Context ctx){
		context = ctx;
	}

	public DBProvider open() throws SQLException{
		dbHelper = new NoteOpenHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Cursor fetchAllHabit(String endDate){
		String whereClause = C_ENDDATE + " > ? ";
		return db.query(T_HABITS, new String[]{BaseColumns._ID, C_HABIT}, whereClause, new String[]{endDate}, null, null, null, null);
	}
	
	public long addHabit(String habit, String startDate, String endDate){
		ContentValues values = new ContentValues();
		values.put(C_HABIT, habit);
		values.put(C_STARTDATE, startDate);
		values.put(C_ENDDATE, endDate);

		long id = 0;
		id = db.insert(T_HABITS, null, values);
		
		return id;
	}
	
	public int deleteHabit(String id, String endDate){
		ContentValues values = new ContentValues();
		values.put(C_ENDDATE, endDate);
		
		String whereClause = C_ID + " = ? " ;
		int cnt = db.update(T_HABITS, values, whereClause, new String[]{id});
//		db.delete(T_HABITS, "_id = ?", new String[]{id});
		
		return cnt;
	}
	
	public int modifyHabit(String id, String habit){
		ContentValues values = new ContentValues();
		values.put(C_HABIT, habit);
		
		String whereClause = C_ID + " = ? " ;
		int cnt = db.update(T_HABITS, values, whereClause, new String[]{id});
		
		return cnt;
	}

	public Cursor fetchAllPlansPerDate(String dateplan){
		String sDate = dateplan.replaceAll("-", "");
		
		String query = new StringBuilder()
			.append("select h._id, h.habit, d.doneflag, d.dateplan ")
			.append(" from habits h LEFT OUTER JOIN doneplans d ")
			.append(" on h._id = d.habitid and d.dateplan = ").append(sDate)
			.append(" where ").append(sDate).append(" between ").append(C_STARTDATE)
			.append(" and ").append(C_ENDDATE)
			.toString();

		return db.rawQuery(query, null);
	}
	
	public int applyDonePlan(String datePlan, String planId, String doneflag){
		String sDate = datePlan.replaceAll("-", "");
		int cnt = 0;
		long id = 0;
		cnt = modifyDonePlan(sDate, planId, doneflag);
		if(cnt <= 0){
			id = addDonePlan(sDate, planId, doneflag);
			if(id > 0) cnt = 2;
		}
		
		return cnt;
	}

	public long addDonePlan(String datePlan, String planId, String doneflag){
		String sDate = datePlan.replaceAll("-", "");

		ContentValues values = new ContentValues();
		values.put(C_DATEPLAN, sDate);
		values.put(C_HABITID, planId);
		values.put(C_DONEFLAG, doneflag);
		
		long id = db.insert(T_DONEPLANS, null, values);
		
		return id;
	}

	public int modifyDonePlan(String datePlan, String planId, String doneflag){
		String sDate = datePlan.replaceAll("-", "");

		ContentValues values = new ContentValues();
		values.put(C_DONEFLAG, doneflag);
		
		String whereClause = C_DATEPLAN + " = ? " + " and " + C_HABITID + " = ? ";
		int cnt = db.update(T_DONEPLANS, values, whereClause, new String[]{sDate, planId});
		
		return cnt;
	}
	
	public String getCalendar(int gainDay) throws ParseException{
		String sDate = getDateFormat(gainDay).replaceAll("-", "");
        return sDate;
	}

	public String getDateFormat(int year, int monthOfYear,int dayOfMonth) {
		String sDate = "";
        Calendar cal = Calendar.getInstance();
		DateFormat formatter ; 
		formatter = new SimpleDateFormat(dateFormat);
        cal.set(Calendar.YEAR, year); 
        cal.set(Calendar.MONTH, monthOfYear); 
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = cal.getTime();         

        sDate = formatter.format(date); 
		return sDate ;
	}

	public String getDateFormat(int gainDay) {
		String sDate = "";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, gainDay);
        Date date = cal.getTime();         

        sDate = formatter.format(date); 
        
        return sDate ;
	}

	public String getDateFormat(int gainDay, String currentDate) {
		String sDate = "";
		DateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        Date cDate = null;
		try {
			cDate = formatter.parse(currentDate);
	        cal.setTime(cDate);
	        cal.add(Calendar.DAY_OF_MONTH, gainDay);
	        Date date = cal.getTime();         

	        sDate = formatter.format(date); 
		} catch (ParseException e) {
			e.printStackTrace();
		}

        return sDate ;
	}
}
