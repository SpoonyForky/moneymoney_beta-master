package com.example.shirin.moneymoney_capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import static android.R.attr.onClick;


public class MainActivity extends AppCompatActivity {

    final Context context = this;
    Button btn, btn1, btn2;


    // Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //LinearLayout layout = (LinearLayout) findViewById(R.layout.id);
        //btn = (Button) findViewById(R.layout.);
        //btn = (Button) findViewById(R.id.login);
        //btn1 = (Button) findViewById(R.id.signup);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    //go to login screen
    public void goTologin(View v){

        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }
    //signup screen
    public void goTosignup(View v){

        Intent intent = new Intent(context, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            switch (item.getItemId()){

                case R.id.action_refresh:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    break;
                case R.id.action_help:
                    startActivity(new Intent(getApplicationContext(), AddTransaction.class));
                    break;
                case R.id.action_check_updates:
                    startActivity(new Intent(getApplicationContext(), Bar_Pie_Activity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
