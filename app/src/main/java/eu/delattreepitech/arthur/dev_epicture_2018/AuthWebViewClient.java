package eu.delattreepitech.arthur.dev_epicture_2018;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthWebViewClient extends WebViewClient {
    MainActivity main;

    AuthWebViewClient(MainActivity main) {
        super();
        this.main = main;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("facebook") || url.contains("twitter") || url.contains("google") || url.contains("yahoo")) {
            view.loadUrl(url);
            return true;
        } else {
            System.out.println(url);
            return false;
        }
    }
}
