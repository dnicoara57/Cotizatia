package com.example.cotizatia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    EditText mEditTextAnul;
    Button mbtnTrimite;
    private static String manul;
    private static String moptiuneaMea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//Pentru setarea marimii fontului in appBar
        if (getSupportActionBar() != null) {
            TextView customTitle = new TextView(this);
            customTitle.setText("Cotizația"); // titlul pe care dorim  să-l afișam
            customTitle.setTextSize(18); //  Aici setezi mărimea fontului
            customTitle.setTextColor(ContextCompat.getColor(this, R.color.white)); // sau Color.WHITE
            customTitle.setTypeface(null, android.graphics.Typeface.BOLD); // opțional
            customTitle.setLayoutParams(new androidx.appcompat.app.ActionBar.LayoutParams(
                    androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT,
                    androidx.appcompat.app.ActionBar.LayoutParams.WRAP_CONTENT
            ));

            getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customTitle);
        }

//end setare font

        manul=null;
        moptiuneaMea="";
        mEditTextAnul = findViewById(R.id.editTextAnul);
        mbtnTrimite = findViewById(R.id.btnSend);
        RadioGroup mradioGroup = findViewById(R.id.radio_grup);

        //Aici se trateaza evenimentul de selectare a unui radiobuton

        mradioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            // Add logic here
            // Check which radio button was clicked
            if (checkedId == R.id.radio_cot_persoana) {
                moptiuneaMea = "cotizatie_persoana";
            } else if (checkedId == R.id.radio_total_cot_pe_an) {
                moptiuneaMea = "total_cotizatie_anual";
            } else if (checkedId == R.id.radio_chelt_asoc_an) {
                moptiuneaMea = "total_cheltuieli_anual";
            }else if (checkedId == R.id.radio_sit_gen_asoc) {
                moptiuneaMea = "situatia_financiara_generala";
            }

       });

        mbtnTrimite.setOnClickListener(v -> {
            manul = mEditTextAnul.getText().toString().trim();

            if ( manul.trim().isEmpty() && !getOptiunea().equals("situatia_financiara_generala")) {
                showCustomToast(getString(R.string.lipsa_an));
                //Toast.makeText(this, R.string.lipsa_an, Toast.LENGTH_LONG).show();

            } else if (getOptiunea() == null)
                showCustomToast(getString(R.string.lipsa_optiune));
            //Toast.makeText(this, R.string.lipsa_optiune, Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, AfisareActivity.class);
                //startActivity(intent);
                intentLaunch.launch(intent);  //in loc de startActivityForResult(intent,1) care este deprecated
            }

        });

    }

    //Custom Toast
    private void showCustomToast(String mesaj) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.text_toast);
        text.setText(mesaj);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        // Poziționează toast-ul: centru-sus, la 200 pixeli distanță de top
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);

        toast.show();
    }


    //aici preiau datele trimise din AfisareActivity
    ActivityResultLauncher<Intent> intentLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent dataIntent = result.getData();
                    if (dataIntent != null) {
                        String data = dataIntent.getStringExtra("dan");
                        // Folosește variabila `data` cum ai nevoie
                    }
                } else {
                    mEditTextAnul.setText("");
                }
            }
    );


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

        if (!checked) return;  // nu face nimic dacă nu e selectat

        int id = view.getId();

        if (id == R.id.radio_cot_persoana) {
            moptiuneaMea = "cotizatie_persoana";
        } else if (id == R.id.radio_total_cot_pe_an) {
            moptiuneaMea = "total_cotizatie_anual";
        } else if (id == R.id.radio_chelt_asoc_an) {
            moptiuneaMea = "total_cheltuieli_anual";
        } else if (id == R.id.radio_sit_gen_asoc) {
            moptiuneaMea = "situatia_financiara_generala";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        // Forțează eliminarea tint-ului de pe iconiță
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.action_logout);
        if (icon != null) {
            icon.setTintList(null); // ✨ scoate orice culoare aplicată automat
            logoutItem.setIcon(icon);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_logout) {
            finishAffinity();
            System.exit(0);
        } else if (itemId == R.id.rachete) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://racheta-hateg.nicalemardan.ro/")));
        } else if (itemId == R.id.help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            intentLaunch.launch(intent);  // În loc de startActivityForResult()
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}