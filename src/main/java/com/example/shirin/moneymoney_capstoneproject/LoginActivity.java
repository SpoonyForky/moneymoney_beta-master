package com.example.shirin.moneymoney_capstoneproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shirin.moneymoney_capstoneproject.AccountActivity;
import com.example.shirin.moneymoney_capstoneproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    EditText username;
    EditText password;
    Button login;
    TextView linksignup;
    CheckBox remember;
    SharedPreferences pref;
    String uname, pwd;
    ProgressDialog dialog;
    List<Pair<String, String>> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        linksignup = (TextView) findViewById(R.id.link_signup);
        remember = (CheckBox) findViewById(R.id.remember_me);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new LoginAsyncTask().execute();
                getUser();

                //Intent intent = new Intent(LoginActivity.this,AddTransaction.class);
            }
        });
    }

    //token based authetication
    private void getUser() {

        uname = username.getText().toString();
        pwd = password.getText().toString();

        /**if (!uname.equals("testaccount") || !pwd.equals("testaccount")){
            Toast.makeText(context, "Username or password incorrect", Toast.LENGTH_LONG).show();
        }
        else{**/
            startActivity(new Intent(this, AddTransaction.class));
        }


        /**new LoginAsyncTask().execute(uname, pwd);

        params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<String, String>("name", uname));
        params.add(new Pair<String, String>("password", pwd));

        try {
            //get JSONObject from JSONArray since the data is array
            JSONArray result = new JSONArray(json);
            JSONObject jsonObject = null;

            //loop through the array and break the JSONObject into String
            for (int i = 0; i < result.length(); i++) {
                jsonObject = result.getJSONObject(i);
                String jsonStr = jsonObject.getString("response");
                if (jsonObject.getBoolean("res")) {
                    String token = jsonObject.getString("token");
                    pwd = jsonObject.getString("grav");

                    SharedPreferences.Editor edit = pref.edit();
                    //Storing Data using SharedPreferences
                    // edit.putString("token", token);
                    //  edit.putString("grav", grav);
                    // edit.commit();
                    // Intent profactivity = new Intent(LoginActivity.this,AddTransaction.class);

                    // startActivity(profactivity);
                    finish();
                }

                Toast.makeText(getApplication(), jsonStr, Toast.LENGTH_LONG).show();

                Log.d("Amount: ", uname);
                Log.d("Description: ", pwd);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }**/


    }

    /**public class LoginAsyncTask extends AsyncTask<String, String, String> {
        String url = "http://moneymoney.zapto.org:8080/userlogin";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Please wait ..");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            uname = params[0];
            pwd = params[1];

            String data = null;
            try {
                data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(password), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


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

                //this will return 200 if connection established or 500 means
                //no search happend or 204 means password or name incorrect
                int responseCode = connection.getResponseCode();

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
            getUser(result);
        }


        /**public void login(View v) {
         String uname = username.getText().toString();
         String pwd = password.getText().toString();

         if (uname.equals("")) {
         Toast toast = Toast.makeText(context, "enter a valid username and password", Toast.LENGTH_LONG);
         toast.show();
         } else {
         Intent intent = new Intent(context, AccountActivity.class);
         startActivity(intent);
         }
         }

    }**/



