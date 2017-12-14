package com.volleydemo.utils.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public interface OnPageLoadListener {
    void onStarted(WebView view, String url, Bitmap favicon);
    void onFinished(WebView view, String url);
    void onProgressChanged(WebView view, int newProgress);
    void onReceivedError(WebView view,String url,int errorCode,String desciption);
    void onReceiveTitle(WebView view,String title);
}
