package com.prascovio.vid2mp3;

import java.util.regex.Pattern;

public class Video extends Media {
    private int width;
    private int height;
    private int fps;
    private String qualityLabel;

    public Video(String info) {
        super(info);
        P = Pattern.compile("width\":(.+?),");
        M = P.matcher(info);
        width = M.find() ? Integer.parseInt(M.group(1)) : -1;
        P = Pattern.compile("height\":(.+?),");
        M = P.matcher(info);
        height = M.find() ? Integer.parseInt(M.group(1)) : -1;
        P = Pattern.compile("fps\":(.+?),");
        M = P.matcher(info);
        fps = M.find() ? Integer.parseInt(M.group(1)) : -1;
        P = Pattern.compile("qualityLabel\":\"(.+?)\",");
        M = P.matcher(info);
        qualityLabel = M.find() ? M.group(1) : null;
    }
}
