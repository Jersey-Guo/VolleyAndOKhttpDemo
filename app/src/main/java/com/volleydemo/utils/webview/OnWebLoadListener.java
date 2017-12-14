package com.volleydemo.utils.webview;

import android.webkit.WebView;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public interface OnWebLoadListener {
    void onLoadErrorListener(WebView view, String url, int errorCode, String desciption);
    void onLoadSuccessListener(WebView view,String title);
}
