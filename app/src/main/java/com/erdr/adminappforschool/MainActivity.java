package com.erdr.adminappforschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void ChangeClass(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String s = String.valueOf(spinner.getSelectedItem());
        final String[] separated = s.split(" ");
        String finalWord = separated[0]+separated[1];
        Intent intent = new Intent(this, Page2.class);
        intent.putExtra("NameOfClass",finalWord);
        startActivity(intent);
    }
}