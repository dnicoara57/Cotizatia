package com.example.cotizatia;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
import android.util.Log;


class Connection extends AsyncTask<String,Void,String>
{
    private Context context;
    private String manul;
    AfisareActivity mActivity;
    private String moptiuneaMea;

    public Connection(Context ctx, AfisareActivity activity, String anTrimis, String Myop) {
        context = ctx;
        mActivity=activity;
        manul=anTrimis;
        moptiuneaMea=Myop;
    }


    @Override
    protected String doInBackground(String... params) {

        //String host="http://10.0.2.2/store/cars.php?anul="+manul;
        //String host="https://racheta-hateg.000webhostapp.com/Cotizatia.php?anul="+manul+"&optiunea="+moptiuneaMea;
        String host = "https://racheta-hateg.nicalemardan.ro/api/CotizatieApi?anul=" + manul + "&optiunea=" + moptiuneaMea;
        URL url = null;
        try {
            url = new URL(host);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.connect();

//            int responseCode=urlConnection.getResponseCode();
//            if(responseCode>=400)
//                    Toast.makeText(context, "Conexiunea a esuat !", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(context, "Conexiune reusita !", Toast.LENGTH_SHORT).show();

            InputStream ips=urlConnection.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
            String linie;
            StringBuilder stringBuffer=new StringBuilder();

            while ((linie= reader.readLine())!=null)
            {
                stringBuffer.append(linie);
                System.out.println(linie);

            }
            String response = stringBuffer.toString();
            Log.d("API_RESPONSE", "Răspuns primit: " + response);
            System.out.println("Răspuns API: " + response);


            mActivity.result=stringBuffer.toString();
            reader.close();
            ips.close();
            urlConnection.disconnect();

        } catch (IOException e) {
            Log.e("API_ERROR", "Exceptie: " + e.getMessage());
            return "  Exceptie : " + e.getMessage();
        }

        return mActivity.result;
    }


    protected void onPostExecute(String result)
    {
        //Parsing Json data here

        //Toast.makeText(context, "Exista masini in stoc", Toast.LENGTH_SHORT).show();

        try {
            JSONObject jsonResult=new JSONObject(result);
            int success=jsonResult.getInt("success");
            if(success==1)
            {
                //JSONArray cars=jsonResult.getJSONArray("cars");
                JSONArray cotizatia=jsonResult.getJSONArray("cotizatie");
                for(int i=0;i<cotizatia.length();i++)
                {
                    JSONObject cot=cotizatia.getJSONObject(i);

                   //int id=car.getInt("id");
                    //String name=car.getString("name");
                    //double price=car.getDouble("price");
                    //String description=car.getString("description");
                    //String line=id+"-"+name+"-"+price+"-"+ description;

                    //mAdapter.add(line);

//                    mActivity.nume.add(name);
//                    mActivity.pret.add(Double.toString(price));
//                    mActivity.descriere.add(description);

                    String numele=cot.getString("Nume");
                    String prenumele=cot.getString("Prenume");
                    int total=cot.getInt("total");


                    mActivity.nume_cotizanti.add(numele);
                    mActivity.prenume_cotizanti.add(prenumele);
                    mActivity.total_cotizatie.add(Double.toString(total));

                }

                Toast.makeText(context, "Exista inregistrari in baza de date", Toast.LENGTH_SHORT).show();

                mActivity.addHeaders();
                mActivity.addData();
            }
            else
            {
                Toast.makeText(context, "Nu exista inregistrari in baza de date", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}



