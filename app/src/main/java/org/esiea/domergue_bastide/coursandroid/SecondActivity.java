package org.esiea.domergue_bastide.coursandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondActivity extends AppCompatActivity {

    public static final String BEERS_UPDATE = "org.esiea.domergue_bastide.coursandroid.BEERS_UPDATE";
    private static final String TAG = "Second Activity";
    private Intent intent;
    private RecyclerView rv;

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
        rv = (RecyclerView) findViewById(R.id.rv_beer);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(new BeersAdapter(new JSONArray()));
    }

    public class BeerUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "" + getIntent().getAction());
            Log.d("JSON ARRAY", "get beers from file");
            if (rv != null) {
                rv.setAdapter(new BeersAdapter(getBiersFromFile()));
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

    private class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeerHolder> {

        private JSONArray beers;

        public BeersAdapter(JSONArray beers) {
            this.beers = beers;
        }

        @Override
        public BeersAdapter.BeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_beer_element, parent, false);
            return new BeerHolder(v);
        }

        @Override
        public void onBindViewHolder(BeersAdapter.BeerHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return beers.length();
        }

        public class BeerHolder extends RecyclerView.ViewHolder {
            public BeerHolder(View itemView) {
                super(itemView);
                //Diapo 38
            }
        }
    }

}
