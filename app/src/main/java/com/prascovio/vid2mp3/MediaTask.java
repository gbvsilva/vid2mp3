package com.prascovio.vid2mp3;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MediaTask extends AsyncTask<String, Void, Media> {
    Context context;
    ProgressBar loadingProgressBar;
    WebView webView;

    public MediaTask(ProgressBar pb, WebView wv) {
        loadingProgressBar = pb;
        loadingProgressBar.setVisibility(View.VISIBLE);
        webView = wv;
    }
    @Override
    protected Media doInBackground(String... urls) {
        Media media = null;
        if(urls.length > 0) {
            do {
                System.out.println("params[0]: " + urls[0]);
                URL url = null;
                try {
                    url = new URL(urls[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();


                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


                    StringBuffer sb = new StringBuffer("");
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    in.close();
                    String s = sb.toString();
                    int i = s.indexOf("{\\\"itag\\\":18,");
                    int j = s.substring(i).indexOf("\"}") + 2;
                    String info = s.substring(i, i + j).replace("\\", "");
                    System.out.println("Info -> " + info);
                    media = new Media(info);
                    media.setSrc();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(media.getSrc() == null);
        }
        return media;
    }


    @Override
    protected void onPostExecute(Media media) {
        System.out.println("Video Src -> " + media.getSrc());
        loadingProgressBar.setVisibility(View.INVISIBLE);
        webView.loadUrl("javascript:setVideoSrc(\"" + media.getSrc() + "\");");
        webView.setVisibility(View.VISIBLE);
    }
}
