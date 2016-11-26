package com.example.shirin.moneymoney_capstoneproject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatProperty;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarGraphActivity extends AppCompatActivity {

    //JSON node name
    private static final String TAG_TYPE = "type";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_DESC = "desc";
    private static final String TAG_DATE = "date";
    private static final String TAG_SUCCESS = "success";
    static final String FETCH_URL = "http://moneymoney.zapto.org:8080";

    String amount = null;
    String desc = null;
    String type = null;
    String date = null;
    String category = null;
    ProgressDialog pDialog;
    List<BarEntry> entries = new ArrayList<BarEntry>();
    BarDataSet dataset;
    BarData barData;

    double amt;
    Date dt;
    BarChart newBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);
        //id the chart layout
        newBarChart = (BarChart) findViewById(R.id.chart_Container);

        new ChartTask().execute();
    }

    public void setupChart() {


}

    //reading from remote database. MongoDB on Node.js server
    private class ChartTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BarGraphActivity.this);
            pDialog.setMessage("Loading data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
           setupChart();
        }

        @Override
        protected String doInBackground(String... String) {
            try {
                URL url = new URL(FETCH_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responsecode = urlConnection.getResponseCode();

                if (responsecode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //grab data and plug it in the chart
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            try {
                JSONArray result = new JSONArray(s);
                JSONObject jsonObject = null;
                for (int i = 0; i < result.length(); i++) {
                    jsonObject = result.getJSONObject(i);
                    amount = jsonObject.getString(TAG_AMOUNT);
                    desc = jsonObject.getString(TAG_DESC);
                    type = jsonObject.getString(TAG_TYPE);
                    category = jsonObject.getString(TAG_CATEGORY);
                    date = jsonObject.getString(TAG_DATE);
                    //System.out.println(date);
                    //write it in the db in a different format like Wednesday, July 12, 2016 12:00 PM
                    SimpleDateFormat readFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a");
                    //SimpleDateFormat writeFormat = new SimpleDateFormat("MMMM dd, yyyy");

                    //check it date string for null or empty string or else it will give Unparseable date: "" (at offset 0) error
                    if (!date.equalsIgnoreCase("")) {
                        try {
                            dt = readFormat.parse(date);  //parse the date string in the read format
                            amt = Double.valueOf(amount.replace(",", ""));

                            entries.add(new BarEntry(Float.valueOf(String.valueOf(date)), Float.valueOf(String.valueOf(amt))));

                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }

                dataset = new BarDataSet(entries, "Transaction");
                dataset.setColor(32);
                dataset.setValueTextColor(12);
                barData = new BarData(dataset);
                newBarChart.setData(barData);
                newBarChart.invalidate();

            }
            catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}



