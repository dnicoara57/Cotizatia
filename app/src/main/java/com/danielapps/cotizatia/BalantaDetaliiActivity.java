package com.danielapps.cotizatia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BalantaDetaliiActivity extends AppCompatActivity {

    private TextView titluTextView, cotizatieTextView, donatiiTextView, cheltuieliTextView, comisioaneTextView, soldTextView;
    private int anul = -1;
    private final String API_URL = "https://racheta-hateg.nicalemardan.ro/api/BalantaApi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balanta_detalii);

        titluTextView = findViewById(R.id.textViewTitlu);
        cotizatieTextView = findViewById(R.id.textCotizatie);
        donatiiTextView = findViewById(R.id.textDonatii);
        cheltuieliTextView = findViewById(R.id.textCheltuieli);
        comisioaneTextView = findViewById(R.id.textComisioane);
        soldTextView = findViewById(R.id.textSold);

        TextView textViewData = findViewById(R.id.textViewData);

        // Formatul "14 august 2025"
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", new Locale("ro"));
        String dataCurenta = dateFormat.format(new Date());

        textViewData.setText(dataCurenta);

        // Citim anul corect ca String
        String anulStr = getIntent().getStringExtra("anul");
        if (anulStr != null && !anulStr.isEmpty()) {
            try {
                anul = Integer.parseInt(anulStr);
            } catch (NumberFormatException e) {
                Log.e("BalantaDetalii", "An invalid: " + anulStr);
                anul = -1;
            }
        }

        // Setăm titlul
        String titlu = (anul != -1)
                ? "Balanța asociației pe anul " + anul
                : "Balanța generală a asociației";
        titluTextView.setText(titlu);

        // Construim URL-ul corect
        String url = (anul != -1) ? API_URL + "?an=" + anul : API_URL;
        fetchBalanta(url);

        // Buton înapoi
        Button buttonInapoi = findViewById(R.id.buttonInapoi);
        buttonInapoi.setOnClickListener(v -> {
            Intent intent = new Intent(BalantaDetaliiActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


    }

    private void fetchBalanta(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extragem cu optString pentru siguranță
                        BigDecimal cotizatie = new BigDecimal(response.optString("totalCotizatie", "0"));
                        BigDecimal donatii = new BigDecimal(response.optString("totalDonatii", "0"));
                        BigDecimal cheltuieli = new BigDecimal(response.optString("totalCheltuieli", "0"));
                        BigDecimal comisioane = new BigDecimal(response.optString("totalComisionStripe", "0"));
                        BigDecimal sold = new BigDecimal(response.optString("balantaFinala", "0"));

                        cotizatieTextView.setText(getString(R.string.total_cotizatie, formatValoare(cotizatie)));
                        donatiiTextView.setText(getString(R.string.total_donatii, formatValoare(donatii)));
                        cheltuieliTextView.setText(getString(R.string.total_cheltuieli, formatValoare(cheltuieli)));
                        comisioaneTextView.setText(getString(R.string.total_comisioane, formatValoare(comisioane)));
                        soldTextView.setText(getString(R.string.total_sold, formatValoare(sold)));

                    } catch (Exception e) {
                        Toast.makeText(this, getString(R.string.error_processing_data), Toast.LENGTH_SHORT).show();
                        Log.e("DataProcessing", "Eroare la procesarea datelor", e);
                    }

                },
                error -> {
                    Toast.makeText(this, getString(R.string.error_api_connection), Toast.LENGTH_SHORT).show();
                    Log.e("APIConnection", "Eroare la conectarea cu API-ul", error);
                }

        );

        queue.add(request);
    }

    private String formatValoare(BigDecimal valoare) {
        return String.format(Locale.getDefault(), "%.2f", valoare);
    }
}
