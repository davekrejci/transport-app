package com.example.krejcdav.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentDaily extends Fragment {
    View view;
    private BarChart chart;
    private DatabaseHelper databaseHelper;
    public FragmentDaily() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily,container,false);
        databaseHelper = new DatabaseHelper(getContext());
        configureChart();
        return view;
    }
    private void configureChart(){
        chart = view.findViewById(R.id.dailyChart);
        Cursor data = databaseHelper.getDayOfWeekSums();

        if (data.getCount() != 0){
            ArrayList<BarEntry> yValues = new ArrayList<>();
            ArrayList<String> xLabels = new ArrayList<>();
            int i = 0;
            while(data.moveToNext()){
                yValues.add(new BarEntry(i,data.getInt(1)));
                xLabels.add(formatDate(data.getString(0)));
                i++;
            }

            BarDataSet set = new BarDataSet(yValues, "Totals");
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData barData = new BarData(set);
            barData.setBarWidth(0.9f);
            barData.setValueFormatter(new MyValueFormatter());
            barData.setValueTextColor(ColorTemplate.rgb("#ffffff"));
            barData.setValueTextSize(20);
            barData.setHighlightEnabled(false);

            // Set data and general style
            chart.setData(barData);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
            Description des = new Description();
            des.setText("");
            chart.setDescription(des);
            chart.setNoDataText("No chart data available");
            chart.setExtraBottomOffset(10);
            chart.setVisibleXRangeMaximum(7f);
            chart.setHorizontalScrollBarEnabled(true);

            // Configure legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);

            // Configure xAxis
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new FragmentDaily.MyXAxisValueFormatter(xLabels));
            xAxis.setAxisLineWidth(0);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(ColorTemplate.rgb("#ffffff"));
            xAxis.setGranularity(1f);
            xAxis.setTextSize(15);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


            // Configure yAxis
            YAxis yAxis = chart.getAxisLeft();
            YAxis yAxis2 = chart.getAxisRight();
            yAxis.setDrawGridLines(false);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawLabels(false);
            yAxis.setAxisMinValue(0f);  // using for proportionate bar heights
            yAxis2.setDrawGridLines(false);
            yAxis2.setDrawAxisLine(false);
            yAxis2.setDrawLabels(false);
        }
    }
    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> mValues;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues.get((int) value);
        }
    }
    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            Cursor data = databaseHelper.getDayOfWeekSums();
            int total = 0;
            while (data.moveToNext()){
                total+=data.getInt(1);
            }
            return mFormat.format(((100.0/total)*value)) + " %";
        }
    }
    private String formatDate(String input){
        switch (input){
            case "0": return "Sun";
            case "1": return "Mon";
            case "2": return "Tue";
            case "3": return "Wed";
            case "4": return "Thu";
            case "5": return "Fri";
            case "6": return "Sat";
            default: return "Unknown format";
        }
    }
}

