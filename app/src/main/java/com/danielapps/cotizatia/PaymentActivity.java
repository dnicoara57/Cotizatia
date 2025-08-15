package com.danielapps.cotizatia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;


import androidx.appcompat.app.AppCompatActivity;

import com.danielapps.cotizatia.model.PlataOnline;
import com.danielapps.cotizatia.model.StripeSessionResponse;
import com.danielapps.cotizatia.network.ApiClient;
import com.danielapps.cotizatia.network.StripeApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private EditText emailInput, sumaInput, numeInput;
    private Spinner tipPlataSpinner;
    private Button trimitePlata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button renuntaPlata = findViewById(R.id.renuntaPlata);
        renuntaPlata.setOnClickListener(v -> {
            finish(); // Închide PaymentActivity, te întoarce la activitatea anterioară (MainActivity)
        });

        // Legăm componentele din layout
        emailInput = findViewById(R.id.emailInput);
        sumaInput = findViewById(R.id.sumaInput);
        numeInput = findViewById(R.id.numeInput);
        tipPlataSpinner = findViewById(R.id.tipPlataSpinner);
        trimitePlata = findViewById(R.id.trimitePlata);

        // Populăm spinner-ul cu opțiunile din strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tip_plata_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipPlataSpinner.setAdapter(adapter);

        trimitePlata.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String sumaText = sumaInput.getText().toString().trim();
            String nume = numeInput.getText().toString().trim();
            String tip = tipPlataSpinner.getSelectedItem().toString();

            // ✅ Validare câmpuri goale
            if (email.isEmpty() || sumaText.isEmpty() || nume.isEmpty()) {
                Toast.makeText(this, "Completați toate câmpurile.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Validare conversie sumă
            double suma;
            try {
                suma = Double.parseDouble(sumaText);
                if (suma <= 0) {
                    Toast.makeText(this, "Suma trebuie să fie mai mare decât zero.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Introduceți o sumă validă.", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Construim obiectul de plată
            PlataOnline plata = new PlataOnline(suma, nume, email, tip);

            // ✅ Retrofit call
            StripeApiService service = ApiClient.getService();
            Call<StripeSessionResponse> call = service.createStripeSession(plata);

            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<StripeSessionResponse> call,@NonNull Response<StripeSessionResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String url = response.body().getSessionUrl();

                        // Deschide WebViewActivity cu URL-ul Stripe
                        Intent intent = new Intent(PaymentActivity.this, WebViewActivity.class);
                        intent.putExtra("stripe_url", url);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PaymentActivity.this, "Eroare server. Încearcă din nou.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StripeSessionResponse> call, @NonNull Throwable t) {
                    Toast.makeText(PaymentActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    Log.e("PaymentError", "Eroare la procesarea plății", t);
                }

            });
        });
    }
}
