package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class Temperature extends AppCompatActivity {

    LineChart mpLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);


        // Using mpLineChart from github to display the Line Chart to test the local data .
        mpLineChart = (LineChart) findViewById(R.id.line_chart);
        LineDataSet lineDataSet1 = new LineDataSet(datavalue1(),"Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);                      //add data to line chart
        mpLineChart.invalidate();


        getSupportActionBar().setTitle("Back");                             //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // using send command to get exact temperature and display with current date & hour


        // Using thread to count current date & hours every seconds
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);     // time will count every second
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tdate = (TextView) findViewById(R.id.date);        // get textview from layout
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy\nhh:mm:ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }


        };
        t.start();


    }

    // Using local data to display on the Line Chart ( will replace it with data from the device )
    private ArrayList<Entry> datavalue1(){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0,23));
        dataVals.add(new Entry(1,23));
        dataVals.add(new Entry(2,24));
        dataVals.add(new Entry(3,25));
        dataVals.add(new Entry(4,23));
        dataVals.add(new Entry(5,23));

        return dataVals;


    }
}
