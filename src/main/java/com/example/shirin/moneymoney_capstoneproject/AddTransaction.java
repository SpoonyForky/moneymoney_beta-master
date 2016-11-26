package com.example.shirin.moneymoney_capstoneproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddTransaction extends AppCompatActivity {

    EditText type, amount, desc, category, date;
    Button btnAddTransaction, btntransaction;
    String typetxt, amounttxt, desctxt, categorytxt, datetxt;
    static final String API_URL = "http://moneymoney.zapto.org:8080/insert";

    TextView responseView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        type = (EditText) findViewById(R.id.editTextType);
        amount = (EditText) findViewById(R.id.editTextAmount);
        desc = (EditText) findViewById(R.id.editTextDesc);
        category = (EditText) findViewById(R.id.editTextCatgoty);
        date = (EditText) findViewById(R.id.editTextDate);
        btnAddTransaction = (Button) findViewById(R.id.buttonAdd);
        btntransaction = (Button) findViewById(R.id.buttonTran);

        btnAddTransaction.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });

        btntransaction.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Bar_Pie_Activity.class);
                startActivity(intent);
            }
        });
    }
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        Context context;
        ProgressDialog loading;

        protected void onPreExecute() {
            /**progressBar.setVisibility(View.VISIBLE);
             responseView.setText("");**/

            //UI thread
            typetxt = type.getText().toString();
            amounttxt = amount.getText().toString();
            desctxt = desc.getText().toString();
            categorytxt = category.getText().toString();
            datetxt = date.getText().toString();

            super.onPreExecute();
            loading = ProgressDialog.show(AddTransaction.this, "Adding...", "Wait...",false, false);
        }

        protected String doInBackground(Void... urls) {

            String result = null;
            String data = null;
            // Do some validation here

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setInstanceFollowRedirects(true);
                urlConnection.setInstanceFollowRedirects(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                JSONObject jsonParam = new JSONObject();
                /**jsonParam.put("type", "income");
                 jsonParam.put("amount", "$88.88");
                 jsonParam.put("desc", "Test Shirin");
                 jsonParam.put("date", "Friday, Octber 13");**/

                jsonParam.put("type", typetxt);
                jsonParam.put("amount", amounttxt);
                jsonParam.put("desc", desctxt);
                jsonParam.put("category", categorytxt);
                jsonParam.put("date", datetxt);


                OutputStream os = urlConnection.getOutputStream();
                //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                //write JSON object to the output stream
                os.write(jsonParam.toString().getBytes());

                Log.d ("Sucessfully added", jsonParam.toString());
                //writer.close();
                os.close();


                //Read
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                result = sb.toString();

                Log.d("Success", result);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String s) {
            s = "Transaction added successfully";
            super.onPostExecute(s);
            loading.dismiss();
            Toast.makeText(AddTransaction.this, s, Toast.LENGTH_LONG).show();


        }
    }
}
