package com.example.ohsanghun.nicehabit;

import java.text.ParseException;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HabitActivity extends ListActivity {
	private SimpleCursorAdapter adapter;
	private TextView text;
	private EditText edit;
	public DBProvider db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habits);
        
        db = new DBProvider(this);
        db.open();
        
        text = (TextView)findViewById(R.id.text);
        edit = (EditText)findViewById(R.id.edit);
        
        ((Button)findViewById(R.id.add)).setOnClickListener(listener);
        ((Button)findViewById(R.id.del)).setOnClickListener(listener);
        ((Button)findViewById(R.id.mod)).setOnClickListener(listener);
        
        setEnabled(false);
    }
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView t = (TextView)v.findViewById(R.id.habit);
		edit.setText(t.getText());
		text.setText(Long.toString(id));
		setEnabled(true);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		db.close();
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		String sDay = "";
		try {
			sDay = db.getCalendar(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Cursor c = db.fetchAllHabit(sDay);
		startManagingCursor(c);
		
		String[] from = new String[]{BaseColumns._ID, db.C_HABIT};
		int[] to = new int[]{R.id.id, R.id.habit};
		
		adapter = new SimpleCursorAdapter(this, R.layout.row, c, from, to);
		setListAdapter(adapter);
	}
    private void setEnabled(boolean enabled){
		((Button)findViewById(R.id.del)).setEnabled(enabled);
		((Button)findViewById(R.id.mod)).setEnabled(enabled);
    }
    
    Button.OnClickListener listener = new View.OnClickListener(){
    	public void onClick(View view){
    		String id = text.getText().toString();
    		String str = edit.getText().toString();
    		
    		String startDate = "";
			String endDate = "";
			try {
				startDate = db.getCalendar(0);
				endDate = db.getCalendar(0);
			} catch (ParseException e) {
				e.printStackTrace();
			}
    		switch(view.getId()){
    		
    		case R.id.del:
    			db.deleteHabit(id, endDate);
    			onResume();
    			break;
    		case R.id.mod:
    			if(str.length() != 0)
    				db.modifyHabit(id, str);
    			else
        			db.deleteHabit(id, endDate);
    			onResume();
    			break;
    		case R.id.add:
    			endDate = "99999999";
    			if(str.length() != 0)
    				db.addHabit(str, startDate, endDate);
    			break;
    		default:
    			Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_LONG).show();
    		}
    		
    		text.setText("");
    		edit.setText("");
    		setEnabled(false);
    		
    		endDate = "99999999";
    		Cursor c = db.fetchAllHabit(startDate);
    		startManagingCursor(c);
    		adapter.changeCursor(c);
    	}
    };
}