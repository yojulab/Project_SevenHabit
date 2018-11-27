package com.example.ohsanghun.nicehabit;

/**
 * Created by ohsanghun on 10/29/16.
 */

interface Constants {
    static final String dateFormat = "yyyy-MM-dd";

    static final String DB = "sevenhabit.db";
    static final int VERSION = 4;

    static final String T_HABITS = "habits";
    static final String C_ID = "_id";
    static final String C_HABIT = "habit";
    //	static final String C_USEYN = "useyn";
    static final String C_STARTDATE = "startdate";
    static final String C_ENDDATE = "enddate";

    static final String CREATE_T_HABITS = "create table " + T_HABITS +
            " (" + C_ID + " integer primary key autoincrement " +
            ", " + C_HABIT + " text not null " +
            ", " + C_STARTDATE + " text not null " +
            ", " + C_ENDDATE + " text not null " +
            " ); ";

    static final String T_DONEPLANS = "doneplans";
    static final String C_DATEPLAN = "dateplan";
    static final String C_HABITID = "habitid";
    static final String C_DONEFLAG = "doneflag";
    static final String CREATE_T_DONEPLAN = "create table " + T_DONEPLANS +
            " ( " + C_DATEPLAN + " text not null " +
            ", " + C_HABITID + " integer " +
            ", " + C_DONEFLAG + " integer not null " +
            " ); ";

    static final String DROP = "drop table ";

}
