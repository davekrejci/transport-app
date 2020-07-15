package com.example.krejcdav.myapplication;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

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
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentMonthly extends Fragment {
    private View view;
    private BarChart chart;
    private DatabaseHelper databaseHelper;
    public FragmentMonthly() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monthly,container,false);
        databaseHelper = new DatabaseHelper(getContext());
        configureChart();
        return view;
    }

    private void configureChart(){
        chart = view.findViewById(R.id.monthlyChart);
        Cursor data = databaseHelper.getMonthlySums();

        if (data.getCount() != 0){
            ArrayList<BarEntry> yValues = new ArrayList<>();
            ArrayList<String> xLabels = new ArrayList<>();
            int i = 0;
            while(data.moveToNext()){
                yValues.add(new BarEntry(i,data.getInt(1)));
                xLabels.add(formatDate(data.getString(0),"MM yyyy","MMM yy"));
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
            chart.setVisibleXRangeMaximum(5f);
            chart.setHorizontalScrollBarEnabled(true);

            // Configure legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);

            // Configure xAxis
            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new MyXAxisValueFormatter(xLabels));
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
            return mFormat.format(value) + " " + getString(R.string.currency);
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
}


