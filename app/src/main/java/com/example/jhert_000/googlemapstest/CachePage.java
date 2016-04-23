package com.example.jhert_000.googlemapstest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;


public class CachePage extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private Button importButton;
    private Button mapButton;
    private Button exportButton;
    private TextView favoritesLabel;
    private ListView favoritesListView;
    private TextView knownCachesLabel;
    private TextView selectedCache;
    private TextView selectedCacheLabel;
    private Spinner knownCachesListView;

    private double lat;
    private double lon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_page);

        importButton = (Button) findViewById(R.id.importButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        exportButton = (Button) findViewById(R.id.exportButton);
        favoritesLabel = (TextView) findViewById(R.id.favoritesLabel);
        favoritesListView = (ListView) findViewById(R.id.favoritesListView);
        knownCachesLabel = (TextView) findViewById(R.id.knownCachesLabel);
        selectedCache = (TextView) findViewById(R.id.selectedCache);
        selectedCacheLabel = (TextView) findViewById(R.id.selectedCacheLabel);
        knownCachesListView = (Spinner) findViewById(R.id.knownCachesListView);

        importButton.setOnClickListener(this);
        exportButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        Geocaching info = new Geocaching(this);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] data = info.getData();
        info.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        knownCachesListView.setAdapter(adapter);
        knownCachesListView.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.importButton:
                startActivity(new Intent(this, NewCacheForm.class));
                finish();
                break;
            case R.id.exportButton:
                if(knownCachesListView.getSelectedItem() != null) {
                    String[] tokens = knownCachesListView.getSelectedItem().toString().split(" ");
                    int selectedCacheId = Integer.parseInt(tokens[0]);
                    Geocaching ex = new Geocaching(this);
                    try {
                        ex.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ex.deleteEntry(selectedCacheId);
                    ex.close();
                    finish();
                    startActivity(getIntent());
                }else{
                    Dialog d = new Dialog(this);
                    d.setTitle("Unable to Delete Cache");
                    TextView tv = new TextView(this);
                    tv.setText("No caches are available to be deleted.");
                    d.setContentView(tv);
                    d.show();
                }
                break;
            case R.id.mapButton:
                String[] tokens = knownCachesListView.getSelectedItem().toString().split(" ");
                lat = Double.parseDouble(tokens[2]);
                lon = Double.parseDouble(tokens[3]);
                startActivity(new Intent(this, MapsActivity.class).putExtra("lat", lat).putExtra("lon", lon));
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       String cache = parent.getSelectedItem().toString();
        selectedCache.setText(cache);
        String[] tokens = knownCachesListView.getSelectedItem().toString().split(" ");
        lat = Double.parseDouble(tokens[2]);
        lon = Double.parseDouble(tokens[3]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedCache.setText("");
    }

    public Double getLat(){
        return lat;
    }

    public Double getLon(){
        return lon;
    }

}
