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
        
        final WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.setVisibility(View.INVISIBLE);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final WebAppInterface webAppI = new WebAppInterface(context);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(false);

        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText linkEditText = (EditText) findViewById(R.id.linkEditText);
                String link = linkEditText.getText().toString();

                if(link.startsWith("https://youtu.be/") || link.startsWith("https://www.youtube.com/watch?v=") || link.startsWith("https://youtube.com/watch?v=")) {

                    MediaTask mediaTask = new MediaTask(loadingProgressBar, myWebView);
                    mediaTask.execute(link);

                }
                else {
                    toast.setText("Link Inválido");
                    toast.show();
                }
            }
        });
    }
}
