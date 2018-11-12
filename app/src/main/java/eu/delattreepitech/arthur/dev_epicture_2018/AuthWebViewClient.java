package eu.delattreepitech.arthur.dev_epicture_2018;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthWebViewClient extends WebViewClient {
    private MainActivity context;
    private boolean stopRedirecting;

    AuthWebViewClient(MainActivity context) {
        super();
        this.context = context;
        this.stopRedirecting = false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("facebook") || url.contains("twitter") || url.contains("google") || url.contains("yahoo")) {
            view.loadUrl(url);
            return true;
        } else {
            if (!this.stopRedirecting) {
                this.stopRedirecting = true;
                this.accessGranted(url);
            }
            return false;
        }
    }

    private void accessGranted(String url) {
        final Intent searchImage = new Intent(this.context, SearchImage.class);
        searchImage.putExtra("tokensUrl", url);
        this.context.startActivity(searchImage);
    }
}
