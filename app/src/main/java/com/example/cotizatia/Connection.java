package com.example.cotizatia;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.cotizatia.AfisareActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private Context context;
    private String manul;
    private AfisareActivity mActivity;
    private String moptiuneaMea;

    public Connection(Context ctx, AfisareActivity activity, String anTrimis, String Myop) {
        this.context = ctx;
        this.mActivity = activity;
        this.manul = anTrimis;
        this.moptiuneaMea = Myop;
    }

    // Metoda pentru apelul API
    private String fetchDataFromServer() {
        String host = "https://racheta-hateg.nicalemardan.ro/api/CotizatieApi?anul=" + manul + "&optiunea=" + moptiuneaMea;
        StringBuilder stringBuffer = new StringBuilder();

        try {
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.connect();

            InputStream ips = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
            String linie;

            while ((linie = reader.readLine()) != null) {
                stringBuffer.append(linie);
            }

            reader.close();
            ips.close();
            urlConnection.disconnect();

            Log.d("API_RESPONSE", "Răspuns primit: " + stringBuffer.toString());
            return stringBuffer.toString();

        } catch (MalformedURLException e) {
            Log.e("API_ERROR", "URL greșit: " + e.getMessage());
        } catch (IOException e) {
            Log.e("API_ERROR", "Exceptie conexiune: " + e.getMessage());
        }
        return null;
    }

    // Metoda pentru inițierea request-ului în fundal
    public void executeRequest() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String result = fetchDataFromServer();

            handler.post(() -> {
                if (result != null) {
                    processJsonResponse(result);
                } else {
                    Toast.makeText(context, "Eroare conexiune API!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Metoda de procesare JSON (înlocuiește `onPostExecute`)
    private void processJsonResponse(String result) {
        try {
            JSONObject jsonResult = new JSONObject(result);
            int success = jsonResult.getInt("success");

            if (success == 1) {
                JSONArray cotizatia = jsonResult.getJSONArray("cotizatie");
                for (int i = 0; i < cotizatia.length(); i++) {
                    JSONObject cot = cotizatia.getJSONObject(i);

                    String numele = cot.getString("Nume");
                    String prenumele = cot.getString("Prenume");
                    int total = cot.getInt("total");

                    mActivity.nume_cotizanti.add(numele);
                    mActivity.prenume_cotizanti.add(prenumele);
                    mActivity.total_cotizatie.add(Double.toString(total));
                }

                Toast.makeText(context, "Exista înregistrări în baza de date", Toast.LENGTH_SHORT).show();
                mActivity.addHeaders();
                mActivity.addData();
            } else {
                Toast.makeText(context, "Nu există înregistrări în baza de date", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Eroare parsare JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
