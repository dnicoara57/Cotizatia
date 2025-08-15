package com.danielapps.cotizatia.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielapps.cotizatia.R;
import com.google.android.material.snackbar.Snackbar;
import android.graphics.drawable.GradientDrawable;

public class SnackbarUtils {

    public static void showCustomSnackbar(View rootView, String mesaj, MessageType type,
                                          int duration, String actionText, View.OnClickListener actionListener,
                                          int offsetYdp) {

        // Creează Snackbar gol
        Snackbar snackbar = Snackbar.make(rootView, "", duration);

        // Obține layout-ul Snackbar-ului
        ViewGroup snackbarLayout = (ViewGroup) snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT);

        // Inflate layout personalizat
        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        View customView = inflater.inflate(R.layout.snackbar_custom, snackbarLayout, false);

        // Referințe la text și icon
        TextView text = customView.findViewById(R.id.snackbar_text);
        ImageView icon = customView.findViewById(R.id.snackbar_icon);

        // Setează mesajul
        text.setText(mesaj);

        // Aplică fundal cu colțuri rotunjite
        customView.setBackgroundResource(R.drawable.snackbar_background);
        GradientDrawable background = (GradientDrawable) customView.getBackground();

        // Stilizează în funcție de tip
        switch (type) {
            case SUCCESS:
                icon.setImageResource(R.drawable.ic_success);
                background.setColor(Color.parseColor("#81C784")); // verde
                text.setTextColor(Color.BLACK); // contrast bun pe verde deschis
                break;
            case ERROR:
                icon.setImageResource(R.drawable.ic_error);
                background.setColor(Color.parseColor("#E57373")); // roșu
                text.setTextColor(Color.WHITE); // contrast bun pe roșu
                break;
            case WARNING:
                icon.setImageResource(R.drawable.ic_warning);
                background.setColor(Color.parseColor("#FFD54F")); // galben
                text.setTextColor(Color.BLACK); // contrast bun pe galben
                break;
        }


        // Umbră subtilă
        customView.setElevation(dpToPx(rootView, 6));

        // Înlocuiește layout-ul default
        snackbarLayout.removeAllViews();
        snackbarLayout.addView(customView);

        // Ridică Snackbar-ul pe verticală
        snackbarLayout.setTranslationY(-dpToPx(rootView, offsetYdp));

        // Adaugă acțiune dacă e definită
        if (actionText != null && actionListener != null) {
            snackbar.setAction(actionText, actionListener);
            snackbar.setActionTextColor(Color.WHITE);
        }

        // Afișează
        snackbar.show();
    }

    private static int dpToPx(View view, int dp) {
        float density = view.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


}
