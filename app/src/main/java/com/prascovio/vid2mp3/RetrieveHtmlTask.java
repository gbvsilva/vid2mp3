package com.prascovio.vid2mp3;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetrieveHtmlTask extends AsyncTask<String, Void, String> {
    private Exception exception;

    @Override
    protected String doInBackground(String... params) {
        String src = "";
        if(params.length > 0) {
            try {
                URL url = new URL(params[0]);
                System.out.println("params[0]: "+params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //System.out.println("Passou1");
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String inputLine;
                while((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();

                String s = sb.toString();
                int i = s.indexOf("{\\\"itag\\\":18,");
                int j = s.substring(i).indexOf("\"}")+2;
                String info = s.substring(i, i+j).replace("\\", "");
                System.out.println("Info -> "+info);

                if(info.indexOf("\"url\":") == -1) {

                    String regex = "\"s=(.+?)u0026.*url=(.+?)\"";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(info);
                    int group1 = 0, group2 = 0;
                    String decodedOldSig, decodedUrl;
                    if (m.find()) {
                        System.out.println("group1 -> " + m.group(1));
                        System.out.println("group2 -> " + m.group(2));
                        group1 = 2;
                        group2 = 1;
                        //decodedOldSig = URLDecoder.decode(m.group(1), "utf-8");
                        //decodedUrl = URLDecoder.decode(m.group(2), "utf-8");
                        System.out.println("Found1");
                    } else {
                        regex = s.indexOf("s=") < s.indexOf("sp=sig") ? "url=(.+?)u0026s=(.+?)u0026" : "url=(.+?)u0026.*s=(.+?)\"";
                        p = Pattern.compile(regex);
                        m = p.matcher(info);
                        if (m.find()) {
                            System.out.println("group1 -> " + m.group(1));
                            System.out.println("group2 -> " + m.group(2));
                            //decodedOldSig = URLDecoder.decode(m.group(2), "utf-8");
                            //decodedUrl = URLDecoder.decode(m.group(1), "utf-8");
                            System.out.println("Found2");
                            group1 = 1;
                            group2 = 2;
                        } else {
                            System.out.println("Pattern not found!");
                            //src = "Not found";
                        }
                    }

                    if (group1 != 0 && group2 != 0) {
                        decodedUrl = URLDecoder.decode(m.group(group1), "utf-8");
                        decodedOldSig = URLDecoder.decode(m.group(group2), "utf-8");

                        StringBuilder sig = new StringBuilder();
                        sig.append(decodedOldSig);
                        sig = sig.reverse();
<<<<<<< HEAD

                        char c1, c2;
                        if (sig.indexOf("=") > -1 && sig.indexOf("=") < 50) {
                            sig.setCharAt(sig.indexOf("="), sig.charAt(sig.length()-1));
                            sig.setCharAt(sig.length()-1, '=');
                            sig.setCharAt(41, sig[0]);
                        }else {
                            c1 = sig.charAt(52);
                            sig.setCharAt(89, sig.charAt(sig.length()-1));
                            //c2 = sig.charAt(52);
=======
                        
						char c1, c2;
                        if (sig.indexOf("=") > -1 && sig.indexOf("=") < 50) {
                        	sig.setCharAt(sig.indexOf("="), sig.charAt(sig.length()-1));
							sig;setCharAt(sig.length()-1, '=');
							sig.setCharAt(41, 'A');
							sig.delete(0,3);
							sig.setCharAt(0, 'A');
                            System.out.println("new sig -> " + sig);
						}else {
							c1 = sig.charAt(89);
                            sig.setCharAt(89, sig.charAt(sig.length()-1));
                            c2 = sig.charAt(52);
>>>>>>> 71be877c02f41dba74c077ef6fc9a6d3895f69d4
                            sig.setCharAt(52, sig.charAt(0));
                            sig.setCharAt(40, c1);
                            sig.delete(100, 103);
<<<<<<< HEAD
                        }
                        sig.delete(0, 3);
                        sig.setCharAt(0, 'A');
                        System.out.println("new sig -> " + sig);
=======
                            sig.setCharAt(0, 'A');
                            System.out.println("new sig -> " + sig);
						}
>>>>>>> 71be877c02f41dba74c077ef6fc9a6d3895f69d4
                        src = decodedUrl + "&sig=" + sig;
                    }
                }else {
                    Pattern p = Pattern.compile("\"url\":\"(.+?)\"");
                    Matcher m = p.matcher(info);
                    if(m.find())
                        src = URLDecoder.decode(m.group(1).replace("u0026", "&"), "utf-8");
                }
                System.out.println("src -> " + src);
            } catch (java.io.IOException e) {
                System.out.println("Erro de excecao");
                e.printStackTrace();
            }

        }

        return src;
    }
}
