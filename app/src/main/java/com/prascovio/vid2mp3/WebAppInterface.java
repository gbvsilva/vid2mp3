package com.prascovio.vid2mp3;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebAppInterface {
    private Context mContext;
    private String videoSrc;
    private Boolean isLoadingVideo;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
        isLoadingVideo = true;
    }

    @JavascriptInterface
    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String src) {
        videoSrc = src;
        isLoadingVideo = false;
    }

    public Boolean getLoadStatus() {
        return isLoadingVideo;
    }
}
