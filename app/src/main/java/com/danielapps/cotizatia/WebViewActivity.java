package com.danielapps.cotizatia;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("stripe_url");

        if (url == null|| url.isEmpty() || !url.startsWith("https://racheta-hateg.nicalemardan.ro")) {
            Toast.makeText(this, "URL invalid sau nesigur", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("/PlatiOnline/Success")) {
                    Toast.makeText(WebViewActivity.this, "Plată reușită!", Toast.LENGTH_LONG).show();
                    finish();
                    return true;
                } else if (url.contains("/PlatiOnline/Failed")) {
                    Toast.makeText(WebViewActivity.this, "Plată eșuată!", Toast.LENGTH_LONG).show();
                    finish();
                    return true;
                }
                return false;
            }
        });

        webView.loadUrl(url);
    }
}
