package com.prascovio.vid2mp3;

import java.util.regex.Pattern;

public class Audio extends Media {
    private String audioQuality;
    private String audioSampleRate;
    private int audioChannels;

    public Audio(String info) {
        super(info);
        P = Pattern.compile("audioQuality\":\"(.+?)\",");
        M = P.matcher(info);
        audioQuality = M.find() ? M.group(1) : null;
        P = Pattern.compile("audioSampleRate\":\"(.+?)\",");
        M = P.matcher(info);
        audioSampleRate = M.find() ? M.group(1) : null;
        P = Pattern.compile("audioChannels\":(.+?),");
        M = P.matcher(info);
        audioChannels = M.find() ? Integer.parseInt(M.group(1)) : -1;
    }
}
