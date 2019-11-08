package com.prascovio.vid2mp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();

        Button btnGetVideo = (Button) findViewById(R.id.btnGetVideo);

        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, "", duration);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);

        final WebView myWebView = (WebView) findViewById(R.id.myWebView);
        myWebView.setVisibility(View.INVISIBLE);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final WebAppInterface webAppI = new WebAppInterface(context);
        myWebView.addJavascriptInterface(webAppI, "Android");

        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.loadUrl("file:///android_asset/index.html");

        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText linkEditText = (EditText) findViewById(R.id.linkEditText);
                String link = linkEditText.getText().toString();

                if(link.startsWith("https://youtu.be/") || link.startsWith("https://www.youtube.com/watch?v=") || link.startsWith("https://youtube.com/watch?v=")) {


                    //loadingProgressBar.setVisibility(View.VISIBLE);
                    MediaTask mediaTask = new MediaTask(loadingProgressBar, myWebView);
                    mediaTask.execute(link);
                    /*System.out.println("Video Src -> " + media.getSrc());
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    myWebView.loadUrl("javascript:setVideoSrc(\"" + media.getSrc() + "\");");
                    myWebView.setVisibility(View.VISIBLE);*/

                }
                else {
                    toast.setText("Link Inválido");
                    toast.show();
                }
            }
        });
    }
}
