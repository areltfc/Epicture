package eu.delattreepitech.arthur.dev_epicture_2018;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import eu.delattreepitech.arthur.dev_epicture_2018.Activities.Home;
import eu.delattreepitech.arthur.dev_epicture_2018.Activities.MainActivity;

public class AuthWebViewClient extends WebViewClient {
    private MainActivity context;
    private boolean stopRedirecting;

    public AuthWebViewClient(MainActivity context) {
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
            if (url.contains("callback?error=access_denied")) {
                this.context.setContentView(R.layout.access_denied);
            } else {
                if (!this.stopRedirecting) {
                    this.stopRedirecting = true;
                    this.accessGranted(url);
                }
            }
            return false;
        }
    }

    private void accessGranted(String url) {
        final Intent home = new Intent(this.context, Home.class);
        home.putExtra("user", new Gson().toJson(new User(url)));
        this.context.startActivity(home);
    }
}
