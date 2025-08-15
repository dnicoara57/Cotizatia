package com.danielapps.cotizatia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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

import com.danielapps.cotizatia.utils.MessageType;
import com.danielapps.cotizatia.utils.SnackbarUtils;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    EditText mEditTextAnul;
    Button mbtnTrimite;

    Button btnPlataOnline;
    Button btnBalantaDetalii;
    private static String manul;
    private static String moptiuneaMea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Verifică dacă a fost transmis un mesaj de bun venit
        String mesaj = getIntent().getStringExtra("welcome_message");
        if (mesaj != null) {
            SnackbarUtils.showCustomSnackbar(
                    findViewById(android.R.id.content),
                    mesaj,
                    MessageType.SUCCESS,
                    Snackbar.LENGTH_LONG,
                    null,
                    null,
                    70
            );
        }

//Pentru setarea marimii fontului in appBar
        if (getSupportActionBar() != null) {
            TextView customTitle = new TextView(this);
            customTitle.setText(getString(R.string.app_name)); // titlul pe care dorim  să-l afișam
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
        btnPlataOnline = findViewById(R.id.btnPlataOnline);
        btnBalantaDetalii = findViewById(R.id.btnBalantaDetalii);

        //Aici se trateaza evenimentul de selectare a unui radiobuton

        mradioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            // Add logic here
            // Check which radio button was clicked
            if (checkedId == R.id.radio_cot_persoana) {
                moptiuneaMea = "cotizatie_persoana";
            } else if (checkedId == R.id.radio_total_cot_pe_an) {
                moptiuneaMea = "total_cotizatie_anual";
            }else if (checkedId == R.id.radio_donatii_asoc_an) {
                moptiuneaMea = "total_donatii_anual";
            }else if (checkedId == R.id.radio_chelt_asoc_an) {
                moptiuneaMea = "total_cheltuieli_anual";
            }else if (checkedId == R.id.radio_sit_gen_anuala) {
                moptiuneaMea = "balanta_generala_anual";
            }else if (checkedId == R.id.radio_sit_gen_asoc) {
                moptiuneaMea = "situatia_financiara_generala";
            }

       });


        mbtnTrimite.setOnClickListener(v -> {
            manul = mEditTextAnul.getText().toString().trim();
            String optiunea = getOptiunea();

            if (manul.isEmpty()) {
                if ("situatia_financiara_generala".equals(optiunea)) {
                    manul = "1900"; // sau orice alt an care nu interferează cu datele reale
                } else {
                    View rootView = findViewById(android.R.id.content); // sau R.id.root_layout dacă ai unul definit
                    MessageType type = MessageType.WARNING; // sau ERROR / WARNING în funcție de context
                    int duration = Snackbar.LENGTH_LONG;
                    String actionText = null; // sau "OK" dacă vrei o acțiune
                    View.OnClickListener actionListener = null; // sau definește acțiunea
                    int offsetYdp = 70; // cât de sus să fie Snackbar-ul
                    String mesajul=getString(R.string.lipsa_an);
                    SnackbarUtils.showCustomSnackbar(rootView, mesajul, type, duration, actionText, actionListener, offsetYdp);
                    //showCustomToast(getString(R.string.lipsa_an));
                    return;
                }
            }

            if (optiunea == null) {
                showCustomToast(getString(R.string.lipsa_optiune));
            } else {
                Intent intent = new Intent(MainActivity.this, AfisareActivity.class);
                intent.putExtra("anul", manul);
                intent.putExtra("optiunea", optiunea);
                intentLaunch.launch(intent);
            }
        });

        btnPlataOnline.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });


        btnBalantaDetalii.setOnClickListener(v -> {
            // Intent către activitatea detaliată
            manul = mEditTextAnul.getText().toString().trim();
            Intent intent = new Intent(MainActivity.this, BalantaDetaliiActivity.class);
            if (manul != null && !manul.isEmpty()) {
                intent.putExtra("anul", manul); // trimite ca String
            }
            startActivity(intent);

        });


    }

    //Custom Toast
    private void showCustomToast(String mesaj) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ (API 30): evităm setView()
            Toast.makeText(getApplicationContext(), mesaj, Toast.LENGTH_LONG).show();
        } else {
            // Versiuni mai vechi: folosește layout personalizat
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

            TextView text = layout.findViewById(R.id.text_toast);
            text.setText(mesaj);

            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            toast.setView(layout); // ⚠️ Deprecated, dar funcțional pe API < 30
            toast.show();
        }
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
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    mEditTextAnul.setText("");
                    refreshUI(); // o metodă care readuce layout-ul la starea inițială
                }
            }
    );

    private void refreshUI() {
        //Toast.makeText(this, "refreshUI called", Toast.LENGTH_SHORT).show();
        View layout = findViewById(R.id.mainLayout);
        layout.setVisibility(View.VISIBLE); // dacă era ascuns
        layout.invalidate(); // forțează redesenarea
        layout.requestLayout(); // reface constrângerile
    }

    //Se executa la revenirea in MainActivity
    @Override
    protected void onResume() {
        super.onResume();
        EditText editTextAn = findViewById(R.id.editTextAnul);
        editTextAn.setText(""); // golește anul
    }

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
        } else if (id == R.id.radio_donatii_asoc_an) {
            moptiuneaMea = "total_donatii_anual";
        } else if (id == R.id.radio_chelt_asoc_an) {
            moptiuneaMea = "total_cheltuieli_anual";
        }else if (id == R.id.radio_sit_gen_anuala) {
            moptiuneaMea = "balanta_generala_anual";
        }else if (id == R.id.radio_sit_gen_asoc) {
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