package com.example.cotizatia;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConnectionTask {

    private Context context;
    private String manul;
    private String moptiuneaMea;
    private AfisareActivity mActivity;

    public ConnectionTask(Context ctx, AfisareActivity activity, String anTrimis, String Myop) {
        this.context = ctx;
        this.mActivity = activity;
        this.manul = anTrimis;
        this.moptiuneaMea = Myop;
    }

    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String result = doInBackground();
            if (context instanceof AfisareActivity) {
                ((AfisareActivity) context).runOnUiThread(() -> onPostExecute(result));
            }
        });
    }

    private String doInBackground() {
        //String host = "https://racheta-hateg.000webhostapp.com/Cotizatia.php?anul=" + manul + "&optiunea=" + moptiuneaMea;
        String host = "https://racheta-hateg.nicalemardan.ro/Cotizatia.php?anul=" + manul + "&optiunea=" + moptiuneaMea;
        try {
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            InputStream ips = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            reader.close();
            ips.close();
            urlConnection.disconnect();

            return stringBuffer.toString();

        } catch (IOException e) {
            return "Excepție: " + e.getMessage();
        }
    }

    private void onPostExecute(String result) {
        if (result.startsWith("Excepție:")) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            JSONObject jsonResult = new JSONObject(result);
            int success = jsonResult.optInt("success", 0);

            if (success == 1) {
                JSONArray cotizatia = jsonResult.optJSONArray("cotizatie");
                if (cotizatia != null) {
                    for (int i = 0; i < cotizatia.length(); i++) {
                        JSONObject cot = cotizatia.optJSONObject(i);
                        if (cot != null) {
                            String numele = cot.optString("Nume", "Necunoscut");
                            String prenumele = cot.optString("Prenume", "Necunoscut");
                            int total = cot.optInt("total", 0);

                            synchronized (mActivity.nume_cotizanti) {
                                mActivity.nume_cotizanti.add(numele);
                            }
                            mActivity.prenume_cotizanti.add(prenumele);
                            mActivity.total_cotizatie.add(Double.toString(total));
                        }
                    }
                    mActivity.addHeaders();
                    mActivity.addData();
                }
                Toast.makeText(context, "Datele au fost încărcate cu succes.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Nu există date disponibile.", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(context, "Eroare JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

