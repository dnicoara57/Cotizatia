package com.example.cotizatia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    Button mbtnInapoi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mbtnInapoi=(Button) findViewById(R.id.buttonInapoi);

        mbtnInapoi.setOnClickListener(v -> {
            //transmit date in MainActivity

            Intent returnIntent = new Intent();
            returnIntent.putExtra("dani","");
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();  //inchide activitatea curenta si ma duce in MainActivity

        });


    }
    
}
