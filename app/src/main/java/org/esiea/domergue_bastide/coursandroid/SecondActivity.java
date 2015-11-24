package org.esiea.domergue_bastide.coursandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondActivity extends AppCompatActivity {

    public static final String BEERS_UPDATE = "org.esiea.domergue_bastide.coursandroid.BEERS_UPDATE";
    private static final String TAG = "Second Activity";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        IntentFilter intentFilter = new IntentFilter(BEERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BeerUpdate(), intentFilter);
        intent = new Intent(getApplicationContext(), GetBeersService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(intent);
            }
        });
    }

    public class BeerUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "" + getIntent().getAction());
            try {
                Log.d("JSON ARRAY", getBiersFromFile().getString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public JSONArray getBiersFromFile() {
            try {
                InputStream is = new FileInputStream(getCacheDir() + "/" + "bieres.json");
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                return new JSONArray(new String(buffer, "UTF-8"));

            } catch (IOException e) {
                e.printStackTrace();
                return new JSONArray();
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONArray();
            }
        }
    }

}
