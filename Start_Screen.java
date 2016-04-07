package com.example.ben.geocaching_final;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.content.Intent;


public class Start_Screen extends Activity
        implements OnClickListener{

    private TextView selectedCache;
    private Button importButton;
    private Button exportButton;
    private Button mapButton;
    private Spinner knownCachesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__screen);

        importButton = (Button) findViewById(R.id.importButton);
        exportButton = (Button) findViewById(R.id.exportButton);
        mapButton = (Button) findViewById(R.id.mapButton);
        selectedCache = (TextView) findViewById(R.id.selectedCache);
        knownCachesListView = (Spinner) findViewById(R.id.knownCachesListView);


        importButton.setOnClickListener(this);
        exportButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.importButton:

                break;
            case R.id.exportButton:

                break;
            case R.id.mapButton:
                startActivity(new Intent(Start_Screen.this, MapActivity.class));
                break;
        }
    }
}
