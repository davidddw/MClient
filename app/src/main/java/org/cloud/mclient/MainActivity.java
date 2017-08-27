package org.cloud.mclient;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.webView1)
    WebView mWebView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView mAVLoadingIndicatorView;


    private View decodeView;

    protected static final String TAG = "MeBlog";

    private final String PREFERNAME = "org.cloud.meblog.data";
    private SharedPreferences mPreferences;

    @BindView(R.id.layout_no_net)
    RelativeLayout mLayoutNoWifi;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        decodeView = getWindow().getDecorView();
        //decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mPreferences = getSharedPreferences(PREFERNAME, 0);

        initData(savedInstanceState);
        mLayoutNoWifi.setOnClickListener((View v) -> initData(savedInstanceState));

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mAVLoadingIndicatorView.setVisibility(android.view.View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mAVLoadingIndicatorView.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                mAVLoadingIndicatorView.setVisibility(android.view.View.GONE);
            }
        });

        mWebView.setHapticFeedbackEnabled(false);
        mWebView.setOnLongClickListener((v) -> true);
    }

    protected void initData(Bundle savedInstanceState) {
        if (NetUtil.isNetworkAvailable(MainActivity.this)) {
            if (savedInstanceState == null) {
                String url = mPreferences.getString("location", this.getString(R.string.app_url));
                mWebView.loadUrl(url);
            } else {
                mWebView.restoreState(savedInstanceState);
            }
            //隐藏不可达UI
            mLayoutNoWifi.setVisibility(View.GONE);
        } else {
            //设置网络不可达UI
            mLayoutNoWifi.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            new AlertDialog.Builder(this).setTitle("退出提示")
                    .setMessage("确定要退出吗?")
                    .setPositiveButton("确定", (DialogInterface dialog, int which) -> {
                                // 点击“确认”后的操作
                                SharedPreferences mPreferences = getSharedPreferences(PREFERNAME, 0);
                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putString("location", getResources().getString(R.string.app_url));
                                editor.commit();
                                Log.d(TAG, "save to file.");
                                MainActivity.this.finish();
                            }
                    )
                    .setNegativeButton("返回", (DialogInterface dialog, int which) -> {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                    ).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        mWebView.restoreState(state);
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onPause() {
        SharedPreferences mPreferences = getSharedPreferences(PREFERNAME, 0);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("location", mWebView.getUrl());
        editor.apply();
        Log.d(TAG, "save to file.");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private synchronized void hideReadBar() {
        hideStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    private synchronized void showReadBar() { // 显示工具栏
        showStatusBar();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
