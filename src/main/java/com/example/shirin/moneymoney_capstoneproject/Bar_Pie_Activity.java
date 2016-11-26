package com.example.shirin.moneymoney_capstoneproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Bar_Pie_Activity extends AppCompatActivity implements View.OnClickListener {
    Button bar, pie;
    TextView txtV;
    double balance = 0.0;
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_TYPE = "type";
    String amount;
    String type;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar__pie_);

        bar = (Button) findViewById(R.id.generateBarChart);
        pie = (Button) findViewById(R.id.generatePieChart);
        //txtV = (TextView) findViewById(R.id.textViewTotal);

        //new BarChartAsyncTask().execute();

        bar.setOnClickListener(this);
        pie.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        if (v == bar) {
            startActivity(new Intent(getBaseContext(), BarGraphActivity.class));
        }
        if (v == pie) {
            startActivity(new Intent(getBaseContext(), PieActivity.class));
        }
    }

    /**public void total(String json) {
        try {
            //get JSONObject from JSONArray since the data is array
            JSONArray result = new JSONArray(json);
            JSONObject jsonObject = null;

            //loop through the array and break the JSONObject into String
            for (int i = 0; i < result.length(); i++) {

                jsonObject = result.getJSONObject(i);

                amount = jsonObject.getString(TAG_AMOUNT);
                type = jsonObject.getString(TAG_TYPE);

                //balance += (amount);

                Log.d("Amount: ", amount);
                Log.d("Description: ", type);

            }
            txtV.setText((int) balance);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }**/



    /**public class BarChartAsyncTask extends AsyncTask<String, String, String> {
        String url = "http://moneymoney.zapto.org:8080";
        JSONArray jsonArray = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Bar_Pie_Activity.this);
            dialog.setMessage("Fetching Data ..");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;

            try {
                URL urlServer = new URL(url);
                connection = (HttpURLConnection) urlServer.openConnection();
                connection.connect();

                InputStream in = new BufferedInputStream(connection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            //Log.d("Success", result.toString());
            return result.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            total(result);
        }
    }**/
}
