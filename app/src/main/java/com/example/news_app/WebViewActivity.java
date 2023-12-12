package com.example.news_app;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Call the superclass method to perform the default activity setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Retrieve the URL from the Intent that started this activity
        String url = getIntent().getStringExtra("url");

        WebView webView = findViewById(R.id.webView);

        // Get the WebSettings of the WebView to enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load the specified URL into the WebView
        webView.loadUrl(url);

        ImageView backArrowImageView = findViewById(R.id.backArrowImageView2);
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  go back to the previous activity
                finish();
            }
        });
    }
}

