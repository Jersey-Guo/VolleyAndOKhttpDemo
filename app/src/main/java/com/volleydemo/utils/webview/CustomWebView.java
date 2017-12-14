package com.volleydemo.utils.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;

/**
 * Created by guojiadong
 * on 2017/12/14.
 */

public class CustomWebView extends WebView {
    private Context mContext;
    private CustomWebClient mWebViewClent;
    private CustomWebChromClient mWebChromClient;

    private String androidToJsDefaultInterface = "JerseyAndroidInterface";

    private JsCallBackListener mJsCallBack;
    /**
     * 是否缓存数据
     */
    private boolean cacheEnabled;

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }


    /**
     * 是否缓存
     *
     * @param
     */
    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * 加载监听
     */
    public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener) {
        mWebViewClent.setOnPageLoadListener(onPageLoadListener);
        mWebChromClient.setmOnPageLoadListener(onPageLoadListener);
    }

    /**
     * Android和js交互回调
     */
    public void setmJsCallBack(JsCallBackListener mJsCallBack) {
        this.mJsCallBack = mJsCallBack;
    }

    /**
     * js调用Android的接口
     *
     * @param androidToJsDefaultInterface
     */
    public void setAndroidToJsInterface(String androidToJsDefaultInterface) {
        this.androidToJsDefaultInterface = androidToJsDefaultInterface;
    }

    public CustomWebView(Context context) {
        super(context);
        initialize(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mContext = context;
        mWebViewClent = new CustomWebClient();
        setWebViewClient(mWebViewClent);
        mWebChromClient = new CustomWebChromClient();
        setWebChromeClient(mWebChromClient);
        initSetting();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initSetting() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT < 18)
            getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= 19) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(cacheEnabled ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(cacheEnabled);
        settings.setLoadsImagesAutomatically(true);
        setJsInterface(new JavaScriptInterface(), androidToJsDefaultInterface);
    }


    /**
     * js调用Android方法的接口可自定义默认的为{@link JavaScriptInterface}
     * @param javaScriptInterface 自定义js接口
     * @param androidToJsDefaultInterface 自定义被js调用的本地接口名
     */
    @SuppressLint("JavascriptInterface")
    public void setJsInterface(Object javaScriptInterface, String androidToJsDefaultInterface) {
        addJavascriptInterface(javaScriptInterface, androidToJsDefaultInterface);
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {
        if (mContext == null) {
            return;
        }
        File appCacheDir = new File("data/data/" + mContext.getPackageName() + "/app_webview");
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
        }
    }

    /**
     * 调用js方法
     *
     * @param jsMethod js方法名
     */
    public void callJSMethod(String jsMethod) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(jsMethod, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    if (mJsCallBack != null) {
                        mJsCallBack.jsCallBack(value);
                    }
                }
            });
        } else {
            loadUrl(jsMethod);
        }
    }
}
