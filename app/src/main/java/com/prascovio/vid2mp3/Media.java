package com.prascovio.vid2mp3;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Media {
    private int itag;
    private String mimeType;
    private int bitrate;
    private Map<String, Integer> initRange;
    private Map<String, Integer> indexRange;
    private String lastModified;
    private int contentLength;
    private String quality;
    private String projectionType;
    private int averageBitrate;
    private String approxDurationMs;
    private String url;
    private String cipher;
    Pattern P;
    Matcher M;

    public Media(String info) {
        P = Pattern.compile("itag\":(.+?),");
        M = P.matcher(info);
        itag = M.find() ? Integer.parseInt(M.group(1)) : null;
        P = Pattern.compile("mimeType\":\"(.+?)\",");
        M = P.matcher(info);
        mimeType = M.find() ? M.group(1) : null;
        P = Pattern.compile("bitrate\":(.+?),");
        M = P.matcher(info);
        bitrate = M.find() ? Integer.parseInt(M.group(1)) : null;

        P = Pattern.compile("initRange\":{\"start\":\"(.+?)\",\"end\":\"(.+?)\"}");
        M = P.matcher(info);
        initRange.put("start", M.find() ? Integer.parseInt(M.group(1)) : -1);
        initRange.put("end", M.find() ? Integer.parseInt(M.group(2)) : -1);
        P = Pattern.compile("indexRange\":{\"start\":\"(.+?)\",\"end\":\"(.+?)\"}");
        M = P.matcher(info);
        indexRange.put("start", M.find() ? Integer.parseInt(M.group(1)) : -1);
        indexRange.put("end", M.find() ? Integer.parseInt(M.group(2)) : -1);

        P = Pattern.compile("lastModified\":\"(.+?)\",");
        M = P.matcher(info);
        lastModified = M.find() ? M.group(1) : null;
        P = Pattern.compile("contentLength\":\"(.+?)\",");
        M = P.matcher(info);
        contentLength = M.find() ? Integer.parseInt(M.group(1)) : null;
        P = Pattern.compile("quality\":\"(.+?)\",");
        M = P.matcher(info);
        quality = M.find() ? M.group(1) : null;
        P = Pattern.compile("projectionType\":\"(.+?)\",");
        M = P.matcher(info);
        projectionType = M.find() ? M.group(1) : null;

        //averageBitrate
        P = Pattern.compile("averageBitrate\":\"(.+?)\",");
        M = P.matcher(info);
        averageBitrate = M.find() ? Integer.parseInt(M.group(1)) : -1;

        //approxDurationMs
        P = Pattern.compile("approxDurationMs\":\"(.+?)\",");
        M = P.matcher(info);
        approxDurationMs = M.find() ? M.group(1) : null;

        P = Pattern.compile("cipher\":\"(.+?)\"");
        M = P.matcher(info);
        cipher = M.find() ? M.group(1) : null;
        //System.out.println(cipher);

        P = Pattern.compile("url\":\"(.+?)\"");
        M = P.matcher(info);
        url = M.find() ? M.group(1) : null;
    }

    public void setUrl() throws UnsupportedEncodingException {
        if(cipher != null) {
            String decodedCipher = URLDecoder.decode(cipher, Charset.forName("UTF-8").toString());
            Pattern p;
            Matcher m;
            String url = "";
            String old_sig = "";
            if (decodedCipher.indexOf("s=") < decodedCipher.indexOf("url=")) {
                P = Pattern.compile("url=(.+)");
                M = P.matcher(decodedCipher);
                url = M.find() ? M.group(1) : null;
                P = Pattern.compile("s=(.+?)LAu0026");
                M = P.matcher(decodedCipher);
                old_sig = M.find() ? M.group(1) : null;
            }else {
                P = Pattern.compile("url=(.+?)u0026");
                M = P.matcher(decodedCipher);
                url = M.find() ? M.group(1) : null;
                P = Pattern.compile("u0026s=(.+LA?)LA");
                M = P.matcher(decodedCipher);
                old_sig = M.find() ? M.group(1) : null;
            }
            //System.out.println("Reverse sig: "+sig);
            StringBuilder sig = new StringBuilder();
            sig.append(old_sig);
            sig = sig.reverse();
            char char1 = sig.charAt(sig.length()-1);
            sig.deleteCharAt(sig.length()-1);
            sig.deleteCharAt(sig.length()-1);
            char char2 = sig.charAt(sig.length()-12);
            sig.setCharAt(sig.length()-12, char1);

            if(sig.length() == 100) {
                sig.setCharAt(45, char2);
            }else if(sig.length() == 104) {
                sig.setCharAt(sig.indexOf("="), char2);
            }
            this.url = url+"&sig="+sig;
        }else {
            url = url.replace("u0026", "&");
        }
    }

    public void printUrl() {
        System.out.println(this.getClass().getSimpleName()+" URL -> "+this.url);
    }
}
