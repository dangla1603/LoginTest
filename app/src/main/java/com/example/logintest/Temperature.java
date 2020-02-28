package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.widget.TextView;
import android.app.ProgressDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Temperature extends AppCompatActivity {

    LineChart mpLineChart;
    Activity activity_ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_ctx = this;

        setContentView(R.layout.activity_temperature);
        mpLineChart = findViewById(R.id.line_chart);


        // Back button
        getSupportActionBar().setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*
        // Example mpLineChart code from github to display the Line Chart to test the local data.
        LineDataSet lineDataSet1 = new LineDataSet(parse_data(),"Data Set");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);

        mpLineChart.setData(data);                      //add data to line chart
        mpLineChart.invalidate();
        */


        // Exact temperature data from pi and display with current date & hour
        String QUERY_TEMPERATURE_DATA = "2";                    // later in res.value
        new server_connection().execute(QUERY_TEMPERATURE_DATA);


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
                                TextView tdate = findViewById(R.id.date);        // get textview from layout
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy\nhh:mm:ss a");
                                String dateString = sdf.format(date);
                                tdate.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    System.out.println(""+e.getMessage());
                }
            }


        };
        t.start();


    }

    // Parse received string of data into an Arraylist of Entries for the line chart.
    private ArrayList<Entry> parse_data(String data){
        ArrayList<Entry> dataVals = new ArrayList<>();
        /*
        // Fill the chart with dummy data.
        dataVals.add(new Entry(0,23));
        dataVals.add(new Entry(1,23));
        dataVals.add(new Entry(2,24));
        dataVals.add(new Entry(3,25));
        dataVals.add(new Entry(4,23));
        dataVals.add(new Entry(5,23));
        */

        ArrayList<String> lines = new ArrayList<>( Arrays.asList(data.split("\n")) );

        System.out.println("lines has "+ lines.size()+ "entries.");
        int count = 0;
        for (String l : lines){
            //String timestamp = l.split(";")[0];
            float value = Float.parseFloat(l.split(";")[2]);

            //dataVals.add(new Entry(timestamp,value); // NOTE: timestamp -> float ?
            dataVals.add(new Entry(count,value));
            count++;

        }

        return dataVals;

    }

    // Async network procedure for Temperature.java
    private class server_connection extends AsyncTask<Object, Void, Object> {

        ProgressDialog progress = new ProgressDialog(activity_ctx);

        @Override
        protected Object doInBackground(Object... voids){
            TCPClient pi = new TCPClient("192.168.1.3", 8000, activity_ctx);
            return pi.send_command((String)voids[0]);
        }

        protected void onProgressUpdate(Integer... progress) {
            // Display loading animation (Spinner progress bar)
        }

        @Override
        protected void onPostExecute(Object result) {

            LineDataSet lineDataSet1 = new LineDataSet(parse_data((String)result),"Temperature");
            lineDataSet1.setDrawCircles(false);
            lineDataSet1.setLineWidth(3);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet1);

            LineData data = new LineData(dataSets);

            mpLineChart.setData(data);                      //add data to line chart
            mpLineChart.animateX(500);
            mpLineChart.invalidate();
            mpLineChart.setDescription(null);

        }
    }
}
