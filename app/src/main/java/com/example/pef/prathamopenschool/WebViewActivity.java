package com.example.pef.prathamopenschool;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    private Context mContext;
    boolean Resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        webView = (WebView) findViewById(R.id.loadPage);
        String gamePath=getIntent().getStringExtra("path");
        createWebView(Uri.parse(gamePath));


    }

    public void createWebView(Uri GamePath) {

        String myPath= GamePath.toString();
/*
        Toast toast = Toast.makeText(WebViewActivity.this, "IN WEBVIEW: "+myPath, Toast.LENGTH_SHORT);
        toast.show();
*/


        webView = (android.webkit.WebView) findViewById(R.id.loadPage);
        webView.loadUrl(myPath);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JSInterface(this, webView), "Android");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.clearCache(true);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

    }
}
