package com.danielapps.cotizatia;

import android.os.Handler;
import android.os.Looper;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.danielapps.cotizatia.utils.MessageType;
import com.danielapps.cotizatia.utils.SnackbarUtils;
import com.google.android.material.snackbar.Snackbar;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webview);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("stripe_url");

        if (url == null || url.isEmpty() || !url.startsWith("https://racheta-hateg.nicalemardan.ro")) {
            showSnackbarAndFinish("⚠️ URL invalid sau nesigur", MessageType.WARNING);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(@NonNull WebView view, @NonNull String url) {
                progressBar.setVisibility(View.GONE);
            }

            // Noua metodă (API >= 24)
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull WebResourceRequest request) {
                return handleUrl(request.getUrl().toString(), view);
            }

            // Metoda veche (pentru API < 24)
            @Override
            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull String url) {
                return handleUrl(url, view);
            }

            private boolean handleUrl(String url, WebView view) {
                if (url.contains("/PlatiOnline/Success")) {
                    showSnackbarAndFinish("✅ Plată reușită!", MessageType.SUCCESS);
                    return true;
                } else if (url.contains("/PlatiOnline/Failed")) {
                    showSnackbarAndFinish("❌ Plată eșuată!", MessageType.ERROR);
                    return true;
                }
                return false;
            }
        });

        webView.loadUrl(url);
    }

    private void showSnackbarAndFinish(String mesaj, MessageType type) {
        SnackbarUtils.showCustomSnackbar(
                findViewById(android.R.id.content),
                mesaj,
                type,
                Snackbar.LENGTH_LONG,
                null,
                null,
                80
        );
        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 2000);    }
}
