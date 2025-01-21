package com.example.cotizatia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    //ListView listView;
    //ArrayAdapter<String> mAdapter;
    EditText mEditTextAnul;
    Button mbtnTrimite;
    private static String manul;
    private static String moptiuneaMea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manul=null;
        moptiuneaMea="";
        mEditTextAnul = findViewById(R.id.editTextAnul);
        mbtnTrimite = findViewById(R.id.btnSend);
        RadioGroup mradioGroup = findViewById(R.id.radio_grup);

        //Aici se trateaza evenimentul de selectare a unui radiobuton

        mradioGroup.setOnCheckedChangeListener((group, checkedId) -> {

             // Add logic here
            // Check which radio button was clicked
            switch(checkedId) {
                case R.id.radio_cot_persoana:
                        moptiuneaMea = "cotizatie_persoana";
                    break;
                case R.id.radio_total_cot_pe_an:
                        moptiuneaMea = "total_cotizatie_anual";
                    break;
                case R.id.radio_chelt_asoc_an:
                        moptiuneaMea = "total_cheltuieli_anual";
                    break;
                case R.id.radio_sit_gen_asoc:
                        moptiuneaMea = "situatia_financiara_generala";
                    break;
            }

        });

        mbtnTrimite.setOnClickListener(v -> {
            manul = mEditTextAnul.getText().toString().trim();

            if ((manul == null || manul.length() == 0) && !getOptiunea().equals("situatia_financiara_generala")) {
                Toast.makeText(this, R.string.lipsa_an, Toast.LENGTH_LONG).show();

            } else if (getOptiunea() == null)
                Toast.makeText(this, R.string.lipsa_optiune, Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, AfisareActivity.class);
                //startActivity(intent);
                intentLaunch.launch(intent);  //in loc de startActivityForResult(intent,1) care este deprecated
            }

        });

    }

    //aici preiau datele trimise din AfisareActivity
    ActivityResultLauncher<Intent>intentLaunch=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==Activity.RESULT_OK)
                {
                    String data=result.getData().getStringExtra("dan");
                }else
                    mEditTextAnul.setText("");

            } );


    public static String getAnul()
    {
        return manul;
    }
    public static String getOptiunea()
    {
        return moptiuneaMea;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cot_persoana:
                if(checked)
                    moptiuneaMea = "cotizatie_persoana";
                break;
            case R.id.radio_total_cot_pe_an:
                if(checked)
                    moptiuneaMea = "total_cotizatie_anual";
                break;
            case R.id.radio_chelt_asoc_an:
                if(checked)
                    moptiuneaMea = "total_cheltuieli_anual";
                break;
            case R.id.radio_sit_gen_asoc:
                if(checked)
                    moptiuneaMea = "situatia_financiara_generala";
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                finishAffinity();
                System.exit(0);
                break;

            case R.id.rachete:
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://racheta-hateg.nicalemardan.ro/")));
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://racheta-hateg-responsive.sportandmath.tk/")));

                break;

            case R.id.help:
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                //startActivity(intent);
                intentLaunch.launch(intent);  //in loc de startActivityForResult(intent,1) care este deprecated

                break;

            default:return super.onOptionsItemSelected(item);
        }
        return true;

    }
}