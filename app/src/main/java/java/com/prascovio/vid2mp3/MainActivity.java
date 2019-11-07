package com.prascovio.vid2mp3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetVideo = (Button) findViewById(R.id.btnGetVideo);

        final Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        final Toast toast = Toast.makeText(context, "", duration);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);

        final WebView myWebView = (WebView) findViewById(R.id.myWebView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        final WebAppInterface webAppI = new WebAppInterface(context);
        myWebView.addJavascriptInterface(webAppI, "Android");

        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setSupportZoom(false);
        myWebView.loadUrl("file:///android_asset/index.html");

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText linkEditText = (EditText) findViewById(R.id.linkEditText);
                String link = linkEditText.getText().toString();

                if(link.startsWith("https://youtu.be/") || link.startsWith("https://www.youtube.com/watch?v=") || link.startsWith("https://youtube.com/watch?v=")) {
                    String src;
                    //myWebView.loadUrl("javascript:toggleElement('loadingDiv');");
                    ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    try {
                        do {
                            src = new RetrieveHtmlTask().execute(link).get();
                        }while(src == "");
                        loadingProgressBar.setVisibility(View.INVISIBLE);

                        //myWebView.loadUrl("javascript:toggleElement('loadingDiv');");
                        myWebView.loadUrl("javascript:toggleElement('videoDiv');");
                        myWebView.loadUrl("javascript:setVideoSrc(\""+src+"\");");
                        myWebView.setVisibility(View.VISIBLE);
                        //webAppI.setVideoSrc(src);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    toast.setText("Link Inválido");
                    toast.show();
                }
            }
        });
    }
}
