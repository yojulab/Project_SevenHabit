package com.example.ohsanghun.nicehabit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.BaseColumns;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private Cursor cursor;
    private ListViewAdapter adapter;
    private TextView dateText;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private ListView listView;

    public DBProvider db;

    static final int DATE_DIALOG_ID = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBProvider(this);
        db.open();

        dateText = (TextView)findViewById(R.id.plandate);
        leftArrow = (ImageView)findViewById(R.id.larrow);
        rightArrow = (ImageView)findViewById(R.id.rarrow);
        listView = (ListView) findViewById(R.id.resultList);

        dateText.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);
        listView.setOnItemClickListener(itemListener);

        dueDateDisplay(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.plandate:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.larrow:
                gainDateDisplay(-1);
                break;
            case R.id.rarrow:
                gainDateDisplay(1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sDate = dateText.getText().toString();
        cursor = db.fetchAllPlansPerDate(sDate);

        adapter = new ListViewAdapter(cursor);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return  result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case INSERT_ID :
                createSevenHabits();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSevenHabits(){
        Intent intent = new Intent(this, HabitActivity.class);
        startActivityForResult(intent, ACTIVITY_CREATE);
    }


    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Cursor c = cursor;
            c.moveToPosition(i);

            String datePlan = dateText.getText().toString();
            String planId = c.getString(c.getColumnIndexOrThrow(db.C_ID));
            String sflag = c.getString(c.getColumnIndexOrThrow(db.C_DONEFLAG));
            String doneflag = (sflag == null || sflag.equals("X")) ? "O" : "X";

            int cnt = db.applyDonePlan(datePlan, planId, doneflag);
            onResume();
        }
    };

    private void dueDateDisplay(int iYear, int iMonth, int iDay){
        String sDate = db.getDateFormat(iYear, iMonth, iDay);
        dateText.setText(sDate);
    }

    private void dueDateDisplay(int gainDay){
        String sDate = db.getDateFormat(gainDay);
        dateText.setText(sDate);
    }

    private void gainDateDisplay(int gainDay){
        String currentDate = dateText.getText().toString();
        String sDate = db.getDateFormat(gainDay,currentDate);
        dateText.setText(sDate);
        onResume();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        int iYear, iMonth, iDay;
        iYear = c.get(Calendar.YEAR);
        iMonth = c.get(Calendar.MONTH);
        iDay = c.get(Calendar.DAY_OF_MONTH);

        switch(id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, iYear, iMonth, iDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    dueDateDisplay(year, monthOfYear, dayOfMonth);
                    onResume();
                }
            };

    public void onShowMap(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent = null;
        try {
            intent = builder.build(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, PLACE_PICKER_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, this);

                Toast.makeText(getApplicationContext(),place.getName(), Toast.LENGTH_LONG);
            }
        }
    }

}
