package com.volleydemo.utils.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.volleydemo.R;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public class WebLayout extends RelativeLayout implements OnPageLoadListener, SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener {
    private SwipeRefreshLayout refreshLayout;
    private CustomWebView webView;
    private OnWebLoadListener onLoadErrorListener;

    private Context mContext;

    private String webTitle;


    public WebLayout(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    public WebLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initialize();
    }

    public WebLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WebLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

    @SuppressLint("ResourceAsColor")
    public void initialize() {

        refreshLayout = new SwipeRefreshLayout(getContext());
        refreshLayout.setLayoutParams(new WebLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setrefreshSchemeColor();
        refreshLayout.setOnRefreshListener(this);
        addView(refreshLayout);

        webView = new CustomWebView(getContext());
        webView.setLayoutParams(new SwipeRefreshLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        webView.setOnPageLoadListener(this);
        webView.setOnKeyListener(this);
        refreshLayout.addView(webView);
    }

    /**
     * 设置刷新样式，默认是三种颜色
     */

    public void setrefreshSchemeColor(int... schemeColor) {
        if (schemeColor.length > 0) {
            refreshLayout.setColorSchemeColors(schemeColor);
        } else {
            refreshLayout.setColorSchemeColors(mContext.getResources().getColor(R.color.colorAccent),
                    mContext.getResources().getColor(R.color.colorPrimary),
                    mContext.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onFinished(WebView view, String url) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onStarted(WebView view, String url, Bitmap favicon) {
        refreshLayout.setRefreshing(false);
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress >= 100)
            refreshLayout.setRefreshing(false);
        else {
            refreshLayout.setRefreshing(false);
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onReceivedError(WebView view, String url, int errorCode, String desciption) {
        if (onLoadErrorListener != null) {
            onLoadErrorListener.onLoadErrorListener(view, url, errorCode, desciption);
        }
    }

    @Override
    public void onReceiveTitle(WebView view, String title) {
        if (onLoadErrorListener != null) {
            onLoadErrorListener.onLoadSuccessListener(view, title);
        }
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getUrl() {
        return getWebView().getUrl();
    }

    public void loadUrl(String url) {
        refreshLayout.setRefreshing(true);
        getWebView().loadUrl(url);
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

    public CustomWebView getWebView() {
        return webView;
    }


    public void setOnLoadErrorListener(OnWebLoadListener onLoadErrorListener) {
        this.onLoadErrorListener = onLoadErrorListener;
    }

    /**
     * 避免WebView内存泄漏
     */
    public void onDestroy(){
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }
}
