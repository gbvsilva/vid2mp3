package com.prascovio.vid2mp3;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveHtmlTask extends AsyncTask<String, Void, String> {
    private Exception exception;

    @Override
    protected String doInBackground(String... params) {
        String html = "";
        if(params.length > 0) {
            try {
                URL url = new URL(params[0]);
                //System.out.println(("params[0]: "+params[0]));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String inputLine;
                while((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                html = sb.toString();
                System.out.println("html len = "+html.length());
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }
        return html;
    }
}
