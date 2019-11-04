package com.prascovio.vid2mp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetVideo = (Button) findViewById(R.id.btnGetVideo);

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView linkTextView = (TextView) findViewById(R.id.linkTextView);
                String link = linkTextView.getText().toString();
                try {
                    URL url = new URL(link);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine, html = "";
                    while((inputLine = in.readLine()) != null)
                        html += inputLine;

                    int i = html.indexOf("{\\\"itag\\\":134}");
                    int j = html.substring(i).indexOf(",{\\\"itag\\\":}");
                    String videoInfo = html.substring(i, i+j).replace("\\", "");
                    Video video = new Video(videoInfo);
                    video.setUrl();
                    video.printUrl();

                    i = html.indexOf("{\\\"itag\\\":140}");
                    j = html.substring(i).indexOf("}]}")+1;
                    String audioInfo = html.substring(i, i+j).replace("\\", "");
                    Audio audio = new Audio(audioInfo);
                    audio.setUrl();
                    audio.printUrl();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } finally {

                }

            }
        });
    }
}
