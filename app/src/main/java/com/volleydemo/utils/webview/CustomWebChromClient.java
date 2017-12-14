package com.volleydemo.utils.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public class CustomWebChromClient extends WebChromeClient {
    private OnPageLoadListener mOnPageLoadListener;

    public void setmOnPageLoadListener(OnPageLoadListener onPageLoadListener) {
        this.mOnPageLoadListener = onPageLoadListener;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if(mOnPageLoadListener != null){
            mOnPageLoadListener.onProgressChanged(view,newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if(mOnPageLoadListener != null){
            mOnPageLoadListener.onReceiveTitle(view,title);
        }
    }
}
