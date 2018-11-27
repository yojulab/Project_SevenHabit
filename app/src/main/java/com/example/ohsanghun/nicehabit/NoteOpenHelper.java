package com.example.ohsanghun.nicehabit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ohsanghun on 10/29/16.
 */

public class NoteOpenHelper extends SQLiteOpenHelper implements Constants{
    public NoteOpenHelper(Context c){
        super(c, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_T_HABITS);
        db.execSQL(CREATE_T_DONEPLAN);
        addSamples(db);
    }

    public void onDrop(SQLiteDatabase db){
        db.execSQL(DROP + T_HABITS + ";");
        db.execSQL(DROP + T_DONEPLANS + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("debug", "Version mismatch : "+oldVersion + "to "+newVersion);
        onDrop(db);
        onCreate(db);
    }
    public void addSamples(SQLiteDatabase db) {
        addSample(db, "밥먹기 전에 공양게송하기", "20110605", "99999999");
        addSample(db, "적당량을 덜어 남김없이 먹기", "20110610", "99999999");
        addSample(db, "고기 적게 먹고 채식하기", "20110610", "99999999");
        addSample(db, "내 컵 가지고 다니며 쓰기", "20110615", "99999999");
        addSample(db, "휴지대신 손수건 가지고 다니며 쓰기", "20110620", "99999999");
        addSample(db, "쓰지 않는 전기코드뽑기", "20110610", "99999999");
        addSample(db, "대중교통 이용하기", "20110620", "99999999");
        addSample(db, "충동구매 하지 않기", "20110620", "99999999");
        addSample(db, "인스턴트 식품 먹지 않기", "20110610", "99999999");
    }
    public long addSample(SQLiteDatabase db, String habit, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(C_HABIT, habit);
        values.put(C_STARTDATE, startDate);
        values.put(C_ENDDATE, endDate);

        long id = 0;
        id = db.insert(T_HABITS, null, values);

        return id;
    }

}