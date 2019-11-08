package com.prascovio.vid2mp3;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Media {
    private String title;
    private int itag;
    private String mimeType;
    private int bitrate;
    private HashMap<String, Integer> initRange;
    private HashMap<String, Integer> indexRange;
    private String lastModified;
    private int contentLength;
    private String quality;
    private String projectionType;
    private int averageBitrate;
    private String approxDurationMs;
    private String src;
    private String cipher;
    Pattern P;
    Matcher M;

    public Media(String info) {
        P = Pattern.compile("\"title\":\"(.+?)\"");
        M = P.matcher(info);
        title = M.find() ? M.group(1) : null;
        P = Pattern.compile("itag\":(.+?),");
        M = P.matcher(info);
        itag = M.find() ? Integer.parseInt(M.group(1)) : null;
        P = Pattern.compile("mimeType\":\"(.+?)\",");
        M = P.matcher(info);
        mimeType = M.find() ? M.group(1) : null;
        P = Pattern.compile("bitrate\":(.+?),");
        M = P.matcher(info);
        bitrate = M.find() ? Integer.parseInt(M.group(1)) : null;

        initRange = new HashMap<>();
        P = Pattern.compile("initRange\":\\{\"start\":\"(.+?)\",\"end\":\"(.+?)\"\\}");
        M = P.matcher(info);
        initRange.put("start", M.find() ? Integer.parseInt(M.group(1)) : -1);
        initRange.put("end", M.find() ? Integer.parseInt(M.group(2)) : -1);
        indexRange = new HashMap<>();
        P = Pattern.compile("indexRange\":\\{\"start\":\"(.+?)\",\"end\":\"(.+?)\"\\}");
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

        P = Pattern.compile("url\":\"(.+?)\"");
        M = P.matcher(info);
        try {
            src = M.find() ? URLDecoder.decode(M.group(1).replace("u0026", "&"), "utf-8") : null;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error on decoding URL");
            e.printStackTrace();
        }
    }

    public void setSrc() throws UnsupportedEncodingException {
        if(src == null) {
            String regex = "\"s=(.+?)u0026.*url=(.+?)\"";
            P = Pattern.compile(regex);
            M = P.matcher(cipher);
            int group1 = 0, group2 = 0;
            String decodedOldSig, decodedUrl;
            if (M.find()) {
                System.out.println("group1 -> " + M.group(1));
                System.out.println("group2 -> " + M.group(2));
                group1 = 2;
                group2 = 1;
                System.out.println("Found1");
            } else {
                regex = cipher.indexOf("s=") < cipher.indexOf("sp=sig") ? "url=(.+?)u0026s=(.+?)u0026" : "url=(.+?)u0026.*s=(.+?)\"";
                P = Pattern.compile(regex);
                M = P.matcher(cipher);
                if (M.find()) {
                    System.out.println("group1 -> " + M.group(1));
                    System.out.println("group2 -> " + M.group(2));
                    System.out.println("Found2");
                    group1 = 1;
                    group2 = 2;
                } else {
                    System.out.println("Pattern not found!");
                }
            }

            if (group1 != 0 && group2 != 0) {

                decodedUrl = URLDecoder.decode(M.group(group1), "utf-8");
                decodedOldSig = URLDecoder.decode(M.group(group2), "utf-8");

                StringBuilder sig = new StringBuilder();
                sig.append(decodedOldSig);

                P = Pattern.compile("ALgx");
                M = P.matcher(sig);
                char c1, c2;
                if(M.find()) {
                    if(sig.indexOf("=") > -1 && sig.indexOf("=") < 100) {
                        sig.setCharAt(sig.indexOf("="), sig.charAt(sig.length() - 1));
                        sig.setCharAt(sig.length() - 1, '=');
                        sig.delete(0, 1);
                        System.out.println("new sig -> " + sig);
                        src = decodedUrl + "&sig=" + sig;
                    }
                }else {
                    sig = sig.reverse();
                    c1 = sig.charAt(36);
                    sig.setCharAt(36, sig.charAt(sig.length() - 1));
                    sig.setCharAt(sig.length() - 1, c1);
                    sig.setCharAt(41, sig.charAt(0));
                    sig.delete(0, 3);
                    sig.setCharAt(0, 'A');
                    System.out.println("new sig -> " + sig);
                    src = decodedUrl + "&sig=" + sig;
                }

            }
        }
    }

    public String getSrc() {
        return src;
    }

    public void printSrc() {
        System.out.println(this.getClass().getSimpleName()+" URL -> "+src);
    }
}
