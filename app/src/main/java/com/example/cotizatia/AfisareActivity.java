package com.example.cotizatia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class AfisareActivity extends AppCompatActivity implements View.OnClickListener{

    //ListView listView;

    String result="";
    Button mbtnInapoi;

    public ArrayList<String> nume_cotizanti=new ArrayList<>();
    public ArrayList<String>prenume_cotizanti=new ArrayList<>();
    public ArrayList<String>total_cotizatie=new ArrayList<>();

    String manulTrimis;
    String moptiuneaTrimisa;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afisare);

        //listView=(ListView)findViewById(R.id.listView) ;
        //mAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //listView.setAdapter(mAdapter);

        //Retin in variabila manulTrimis valoarea trimisa din MainActivity
        manulTrimis=MainActivity.getAnul();
        moptiuneaTrimisa=MainActivity.getOptiunea();

        mTextView= findViewById(R.id.textView);
        mbtnInapoi= findViewById(R.id.buttonInapoi);
        //mTextView.setText("Cotizatia pe anul : "+manulTrimis);


        mbtnInapoi.setOnClickListener(v -> {
            //transmit date in MainActivity

            Intent returnIntent = new Intent();
            returnIntent.putExtra("dan","");
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();  //inchide activitatea curenta si ma duce in MainActivity

        });


        Connection con= new Connection(getApplicationContext(), this,manulTrimis,moptiuneaTrimisa);
        con.executeRequest();
    }


    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSizeInPx);
        tv.setTextSize(10);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        tv.setOnClickListener(this);
        return tv;
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    /**
     * This function add the headers to the table
     **/
    @SuppressLint("SetTextI18n")
    public void addHeaders() {
        TableLayout tl = findViewById(R.id.tableHeader);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        switch (moptiuneaTrimisa)
        {
            case "cotizatie_persoana":
                mTextView.setText("Cotizatia membrilor asociatiei pe anul : " + manulTrimis);
                tr.addView(getTextView(0, "Nr", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Nume membru", Color.WHITE, Typeface.BOLD, Color.BLUE));
                //tr.addView(getTextView(0, "Prenume", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Total cotizatie", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tl.addView(tr, getTblLayoutParams());
                break;

            case "total_cotizatie_anual":
                mTextView.setText("Total cotizatie pe anul : " + manulTrimis);
                tr.addView(getTextView(0, "Nr.cotizanti", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Anul", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Total cotizatie", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tl.addView(tr, getTblLayoutParams());
                break;

            case "total_cheltuieli_anual":
                mTextView.setText("Cheltuieli asociatie pe anul : " + manulTrimis);
                tr.addView(getTextView(0, "Nr", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Obiectul", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Anul", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Total cost", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tl.addView(tr, getTblLayoutParams());
                break;

            case "situatia_financiara_generala":
                mTextView.setText("Situatia financiara generala");
                tr.addView(getTextView(0, "Tot.venituri", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Tot.cheltuieli", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tr.addView(getTextView(0, "Sold", Color.WHITE, Typeface.BOLD, Color.BLUE));
                tl.addView(tr, getTblLayoutParams());

                break;
        }

    }

    /**
     * This function add the data to the table
     **/
    public void addData() {
        //int numRecords =nume.size();
        int numRecords =nume_cotizanti.size();

        TableLayout tl = findViewById(R.id.table);
        for (int i = 0; i < numRecords; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(getLayoutParams());
            switch (moptiuneaTrimisa){
                case "cotizatie_persoana":

                    tr.addView(getTextView(i + 1, Integer.toString(i+1), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1,nume_cotizanti.get(i)+"  "+prenume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    //tr.addView(getTextView(i + 1+numRecords,prenume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+2*numRecords, total_cotizatie.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tl.addView(tr, getTblLayoutParams());
                    break;
                case "total_cotizatie_anual":
                    tr.addView(getTextView(i + 1,nume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+numRecords,prenume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+2*numRecords, total_cotizatie.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tl.addView(tr, getTblLayoutParams());
                    break;
                case "total_cheltuieli_anual":
                    tr.addView(getTextView(i + 1, Integer.toString(i+1), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1,nume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+numRecords,prenume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+2*numRecords, total_cotizatie.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tl.addView(tr, getTblLayoutParams());

                    break;

                case "situatia_financiara_generala":
                    tr.addView(getTextView(i + 1,nume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+numRecords,prenume_cotizanti.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tr.addView(getTextView(i + 1+2*numRecords, total_cotizatie.get(i), Color.WHITE, Typeface.NORMAL, ContextCompat.getColor(this, R.color.colorAccent)));
                    tl.addView(tr, getTblLayoutParams());

                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tv = findViewById(id);
        if (null != tv) {
            Log.i("onClick", "Clicked on row :: " + id);
            Toast.makeText(this, "Clicked on row :: " + id + ", Text :: " + tv.getText(), Toast.LENGTH_SHORT).show();
        }
    }



}