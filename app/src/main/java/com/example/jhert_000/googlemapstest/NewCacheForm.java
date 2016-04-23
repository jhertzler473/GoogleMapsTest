package com.example.jhert_000.googlemapstest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class NewCacheForm extends Activity implements View.OnClickListener {

    private TextView nameLabel;
    private TextView latLabel;
    private TextView lonLabel;
    private TextView favoriteLabel;
    private EditText nameField;
    private EditText latField;
    private EditText lonField;
    private CheckBox favCheck;
    private Button addButton;
    private Button cancelButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cache_form);

        nameLabel = (TextView) findViewById(R.id.nameLabel);
        latLabel = (TextView) findViewById(R.id.latLabel);
        lonLabel = (TextView) findViewById(R.id.lonLabel);
        favoriteLabel = (TextView) findViewById(R.id.favoriteLabel);
        nameField = (EditText) findViewById(R.id.nameField);
        latField = (EditText) findViewById(R.id.latField);
        lonField = (EditText) findViewById(R.id.lonField);
        favCheck = (CheckBox) findViewById(R.id.favCheck);
        addButton = (Button) findViewById(R.id.addButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.addButton:
                Intent intent = new Intent(this, CachePage.class);
                boolean didItWork = true;
                try {
                    String name = nameField.getText().toString();
                    if(name.contains(" ")){
                        Dialog d = new Dialog(this);
                        d.setTitle("Error");
                        TextView tv = new TextView(this);
                        tv.setText("Caches cannot contain spaces");
                        d.setContentView(tv);
                        d.show();
                        break;
                    }else {
                        String latitude = latField.getText().toString();
                        String longitude = lonField.getText().toString();

                        Geocaching entry = new Geocaching(this);
                        entry.open();
                        entry.createEntry(name, latitude, longitude);
                        entry.close();

                    }
                }
                catch(Exception e){
                    didItWork = false;
                    String error = e.toString();
                    Dialog d = new Dialog(this);
                    d.setTitle("Error");
                    TextView tv = new TextView(this);
                    tv.setText(error);
                    d.setContentView(tv);
                    d.show();
                }
                finally{
                    if(didItWork){
                        Dialog d = new Dialog(this);
                        d.setTitle("Cache Added!");
                        TextView tv = new TextView(this);
                        tv.setText("Your cache has been added successfully.");
                        d.setContentView(tv);
                        d.show();
                    }
                }
                finish();
                startActivity(new Intent(this, CachePage.class));
                break;
            case R.id.cancelButton:
                finish();
                startActivity(new Intent(this, CachePage.class));
                break;
        }
    }
}
