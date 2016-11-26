package com.example.shirin.moneymoney_capstoneproject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonParser;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class BarGraphActivity extends AppCompatActivity {

    private GraphicalView mChart;
    private TimeSeries transactionSeries;
    private XYMultipleSeriesDataset dataset;
    private XYSeriesRenderer transactionRenderer;
    private XYMultipleSeriesRenderer multiRenderer;
    private TimeChart fuckingtimechart;
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
    double amt;
    Date dt;
    public XYMultipleSeriesDataset datasetOut;
    public XYMultipleSeriesRenderer mRendererOut;
    public LinearLayout chartContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_graph);
        //creating an Timeseries for Transactions
        transactionSeries = new TimeSeries("Transactions");
        //creating a dataset to hold each series
       chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        //start plotting chart

        Log.wtf("start oncreate Tseries:",String.valueOf(transactionSeries.getItemCount()));
        new ChartTask().execute();
      //  setupChart();
    }

    public void setupChart() {


        Log.wtf("setup onpost Tseries:",String.valueOf(transactionSeries.getItemCount()));
        Log.wtf("setup maxX series:",String.valueOf(transactionSeries.getMaxX()));
        Log.wtf("setup maxY series:",String.valueOf(transactionSeries.getMaxY()));


/*
        for (int i = 0; i < transactionSeries.getItemCount(); i++){
            Log.wtf("transeries index", String.valueOf(transactionSeries.getIndexForKey(i)));
            Log.wtf("transeries getX[i]", String.valueOf(transactionSeries.getX(i)));
            Log.wtf("transeries getY[i]", String.valueOf(transactionSeries.getY(i)));
        }
*/
        dataset = new XYMultipleSeriesDataset();
        Log.wtf("start setchart data:",String.valueOf(dataset.getSeriesCount()));
        Log.wtf("start setchart Tseries:",String.valueOf(transactionSeries.getItemCount()));
        //Creating a XYMultipleSeriesRenderer to customize transaction series

        dataset.addSeries(transactionSeries);
        Log.wtf("mid setchart data:",String.valueOf(dataset.getSeriesCount()));
        transactionRenderer = new XYSeriesRenderer();
        transactionRenderer.setColor(Color.GREEN);
      //  transactionRenderer.setPointStyle(PointStyle.CIRCLE);
        transactionRenderer.setPointStyle(PointStyle.SQUARE);

        transactionRenderer.setFillPoints(true);
        transactionRenderer.setLineWidth(2);
        transactionRenderer.setDisplayChartValues(true);

        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.addSeriesRenderer(transactionRenderer);
        multiRenderer.setChartTitle("Transaction Trends");
        multiRenderer.setXTitle("Date");
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setYTitle("Amount");
        multiRenderer.setAxisTitleTextSize(30);
       // multiRenderer.setZoomButtonsVisible(true);
     //   multiRenderer.setSelectableBuffer(10);

        multiRenderer.setBarSpacing(100);
  //    multiRenderer.setInScroll(true);


        // Adding transactionRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same


        Log.wtf("end setchart data:",String.valueOf(dataset.getSeriesCount()));
        // Getting a reference to LinearLayout of the bar graph activity Layout
        chartContainer = (LinearLayout) findViewById(R.id.chart_container);
      //  chartContainer.setBackgroundColor(Color.BLUE);
      //  multiRenderer.setClickEnabled(true);
    //    chartContainer.setScrollContainer(true);
        //BarChartView(getBaseContext(), dataset, multiRenderer, BarChart.Type.DEFAULT);

        mChart =  ChartFactory.getTimeChartView(getBaseContext(),dataset,multiRenderer,"dd-MMM-yyyy");
     //  mChart.setBackgroundColor(Color.WHITE);
        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(mChart);
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

            Log.wtf("start onpost Tseries:",String.valueOf(transactionSeries.getItemCount()));
            try {
                //get JSONObject from JSONArray of String
                JSONArray result = new JSONArray(s);
                JSONObject jsonObject = null;
                //String[] values = new String[2];
                //loop through the array and break the JSONObject into String
               // setupChart();

               /* int count = 10;
                Date[] dtt = new Date[count];
                for(int i=0;i<count;i++){
                    GregorianCalendar gc = new GregorianCalendar(2012, 10, i+1);
                    dtt[i] = gc.getTime();
                    Log.wtf("fuckGC",String.valueOf(dtt[i]));
                    Log.wtf("fuckGC",String.valueOf(dtt[i].getTime()));

                }
                */
                for (int i = 0; i < result.length(); i++) {
                    jsonObject = result.getJSONObject(i);
                    amount = jsonObject.getString(TAG_AMOUNT);
                    desc = jsonObject.getString(TAG_DESC);
                    type = jsonObject.getString(TAG_TYPE);
                    category = jsonObject.getString(TAG_CATEGORY);
                    //getting date as string from database
                    date = jsonObject.getString(TAG_DATE);
                    //System.out.println(date);
                    //write it in the db in a different format like Wednesday, July 12, 2016 12:00 PM
                    SimpleDateFormat readFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a");
                    //SimpleDateFormat writeFormat = new SimpleDateFormat("MMMM dd, yyyy");

                    //check it date string for null or empty string or else it will give Unparseable date: "" (at offset 0) error
                    if (!date.equalsIgnoreCase("")) {
                        try {
                            dt = readFormat.parse(date);  //parse the date string in the read format
                            //String dtStr = writeFormat.format(dt);
                            //dt = writeFormat.parse(dtStr);

                            //System.out.println(dt);
                       //    Log.d("Date: ", String.valueOf(dt.getTime()));
                            amt = Double.valueOf(amount.replace(",", ""));
                            //System.out.println(amt);
                           // Log.d("Amount: ", String.valueOf(amt));
                     //       GregorianCalendar gc = new GregorianCalendar(2012, 10, i+1);
                         //   dtt[i] = gc.getTime();
                            transactionSeries.add(dt.getTime(),amt);
                          //  Log.wtf("FL onpost Tseries:",String.valueOf(transactionSeries.getItemCount()));
                          //  Log.wtf("FL X series:",String.valueOf(transactionSeries.getX(i)));
                         //   Log.wtf("FL Y series:",String.valueOf(transactionSeries.getY(i)));
                         //   Log.wtf("FL maxX series:",String.valueOf(transactionSeries.getMaxX()));
                         //   Log.wtf("FL maxY series:",String.valueOf(transactionSeries.getMaxY()));
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                }
              //  setupChart();

                dataset.addSeries(transactionSeries);

               multiRenderer.addSeriesRenderer(transactionRenderer);
               mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(),dataset,multiRenderer,"dd-MM-YY");
              //  chartContainer.removeAllViews();
           //   chartContainer.addView(mChart);
                mChart.repaint();
             //   Log.wtf("end onpost Tseries:",String.valueOf(transactionSeries.getItemCount()));
              //  Log.wtf("why dont i get here" ," here");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}



