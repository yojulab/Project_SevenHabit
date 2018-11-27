package com.example.ohsanghun.nicehabit;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ohsanghun on 7/1/16.
 */
public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter() {}

    public ListViewAdapter(Cursor cursor) {
        int id = 0;
        String habit = "";
        String doneTF = "";

        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            habit = cursor.getString(1);
            doneTF = cursor.getString(2);
            addItem(id, habit, doneTF);
        }
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ListViewItem listViewItem = listViewItemList.get(position);

            convertView = inflater.inflate(R.layout.rowplan, parent, false);
            TextView habitText = (TextView) convertView.findViewById(R.id.habit);
            TextView doneTFText = (TextView) convertView.findViewById(R.id.doneTF);

            habitText.setText(listViewItem.getHabit());
            doneTFText.setText(listViewItem.getDoneTF());
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(int id, String habit, String doneTF) {
        ListViewItem item = new ListViewItem();

        item.setId(id);
        item.setHabit(habit);
        item.setDoneTF(doneTF);

        listViewItemList.add(item);
    }
}