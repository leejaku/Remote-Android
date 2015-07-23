package com.example.lee.remote_android;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity {

    TextView user,password;
    Button login;
    Button register;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "508859012792";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClickListener();
    }


    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

               // etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    public void onClickListener() {

            login = (Button) findViewById(R.id.button);
            register = (Button) findViewById(R.id.button2);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intreg = new Intent("com.example.lee.remote_android.RegisterActivity");
                    startActivity(intreg);
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getRegId();
                    user = (TextView) findViewById(R.id.usr);
                    password = (TextView) findViewById(R.id.pswd);
                    String match="0";

                    LoginIstance.getIst().setLog(user.getText().toString(), password.getText().toString());
                    HttpLogin connection = new HttpLogin(LoginIstance.getIst().getLog()[0],LoginIstance.getIst().getLog()[1]);

                    connection.execute();
                    while(connection.finish()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {

                        }
                    }
                    match = connection.getStringa();





                    if(!match.equals("0")) {
                        LoginIstance.getIst().setID(match);
                        Intent intlog = new Intent("com.example.lee.remote_android.InterfaceActivity");
                        startActivity(intlog);
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "Username/password non validi!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                }
            });

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}