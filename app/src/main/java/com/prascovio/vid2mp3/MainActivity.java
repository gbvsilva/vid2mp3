package com.prascovio.vid2mp3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetVideo = (Button) findViewById(R.id.btnGetVideo);

        btnGetVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText linkEditText = (EditText) findViewById(R.id.linkEditText);
                String link = linkEditText.getText().toString();

                String html = null;
                try {
                    html = new RetrieveHtmlTask().execute(link).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(html != null) {
                    //System.out.println("===HTML===");
                    //System.out.println(html);
                    int i = html.indexOf("{\\\"itag\\\":18,");
                    int j = html.substring(i).indexOf("\"}")+2;
                    String info = html.substring(i, i+j).replace("\\", "");
                    System.out.println(info);
                    Video video = new Video(info);
                    try {
                        video.setUrl();
                        video.printUrl();
                        TextView videoUrlTextView = (TextView) findViewById(R.id.videoUrlTextView);
                        videoUrlTextView.setText(video.getUrl());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

//                    i = html.indexOf("{\\\"itag\\\":140}");
//                    j = html.substring(i).indexOf("}]}") + 1;
//                    String audioInfo = html.substring(i, i + j).replace("\\", "");
//                    Audio audio = new Audio(audioInfo);
//                    try {
//                        audio.setUrl();
//                        //audio.printUrl();
//                        TextView audioUrlTextView = (TextView) findViewById(R.id.audioUrlTextView);
//                        audioUrlTextView.setText(audio.getUrl());
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

                }
            }
        });
    }
}
