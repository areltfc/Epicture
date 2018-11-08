package eu.delattreepitech.arthur.dev_epicture_2018;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthWebViewClient extends WebViewClient {
    private String url;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        this.url = url;
        return false;
    }

    public String getUrl() { return this.url; }
}
