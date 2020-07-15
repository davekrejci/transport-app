package com.example.krejcdav.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentList extends Fragment {
    View view;
    ListView listView;
    DatabaseHelper databaseHelper;

    public FragmentList() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list,container,false);
        databaseHelper = new DatabaseHelper(getActivity());
        listView = view.findViewById(R.id.listView);
        populateListViewMonthly();
        configureSwitchListView();
        return view;
    }

    private void populateListView() {
        Cursor data = databaseHelper.getAllRows();
        ArrayList<String> listData = new ArrayList<>();
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.my_list_item, R.id.list_content,listData);
        if (data.getCount() == 0){
            toastMessage("No data found");
        }
        listData.add("Date  |  Price | Type");
        while(data.moveToNext()){
            listData.add(
                    getDate(data.getLong(1),"dd.MM.yyyy") + "  \t  " +
                    data.getInt(3) + " " + getString(R.string.currency)+ "  \t  " +
                    TransportType.values()[data.getInt(2)]
            );
        }
        listView.setAdapter(adapter);
    }
    private void populateListViewMonthly() {
        Cursor data = databaseHelper.getMonthlySums();
        ArrayList<String> listData = new ArrayList<>();
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), R.layout.my_list_item, R.id.list_content,listData);
        if (data.getCount() == 0){
            toastMessage("No data found");
        }
        listData.add(data.getColumnName(0) + "  |  " + data.getColumnName(1));
        while(data.moveToNext()){
            listData.add(
                    formatDate(data.getString(0),"MM yyyy","MMMM yy") + " :\t " +
                    data.getString(1) + " " + getString(R.string.currency)
            );
        }
        listView.setAdapter(adapter);
    }
    public static String getDate(long timeStamp, String format){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "Exception formatting date";
        }
    }
    private String formatDate(String input,String inputFormat,String outputFormat){
        try{
            Date date = new SimpleDateFormat(inputFormat).parse(input);
            return new SimpleDateFormat(outputFormat).format(date);
        }
        catch(Exception ex){
            return "Exception formatting date";
        }
    }
    private void toastMessage(String message){
        Toast toast = Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 200);
        toast.show();
    }
    private void configureSwitchListView(){
        Switch switchListData = view.findViewById(R.id.switchListData);
        switchListData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    populateListView();
                }
                else
                    populateListViewMonthly();
            }
        });
    }

}
