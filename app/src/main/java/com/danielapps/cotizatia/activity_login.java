package com.danielapps.cotizatia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class activity_login extends AppCompatActivity {
    EditText emailInput;
    Button loginButton;
    String apiUrl = "https://racheta-hateg.nicalemardan.ro/api/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            // ✅ Verificare email valid înainte de login
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Emailul introdus nu este valid", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!email.isEmpty()) {
                authenticateUser(email);
            } else {
                Toast.makeText(this, "Introdu un email valid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authenticateUser(String email) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("email", email);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    apiUrl,
                    payload,
                    response -> {
                        // 🎉 Mesaj de bun venit cu nume complet (dacă există)
                        String nume = response.optString("nume");
                        String prenume = response.optString("prenume");
                        Toast.makeText(this, "Bun venit, " + prenume + " " + nume, Toast.LENGTH_LONG).show();

                        // 💾 Salvăm totul în sesiune
                        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                        prefs.edit()
                                .putString("email", response.optString("email"))
                                .putString("nume", nume)
                                .putString("prenume", prenume)
                                .putInt("id", response.optInt("id"))
                                .apply();

                        // 🔁 Redirecționare către activitatea principală
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    },
                    error -> {
                        String mesaj = "Autentificare eșuată";

                        if (error.networkResponse != null) {
                            switch (error.networkResponse.statusCode) {
                                case 401:
                                    mesaj = "Email invalid sau membru inactiv";
                                    break;
                                case 400:
                                    mesaj = "Cerere incorectă";
                                    break;
                                case 500:
                                    mesaj = "Eroare server";
                                    break;
                            }
                        }

                        Toast.makeText(this, mesaj, Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            Toast.makeText(this, "Eroare: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}