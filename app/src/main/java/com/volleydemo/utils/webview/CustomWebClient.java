package com.volleydemo.utils.webview;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public class CustomWebClient extends WebViewClient {
    private OnPageLoadListener mOnPageLoadListener;

    public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener) {
        this.mOnPageLoadListener = onPageLoadListener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mOnPageLoadListener != null) mOnPageLoadListener.onFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mOnPageLoadListener != null) mOnPageLoadListener.onStarted(view, url,favicon);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if(mOnPageLoadListener != null) {
            mOnPageLoadListener.onReceivedError(view, request.getUrl().getPath(), error.getErrorCode(), (String) error.getDescription());
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if(mOnPageLoadListener != null) {
            mOnPageLoadListener.onReceivedError(view, failingUrl, errorCode,description);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
