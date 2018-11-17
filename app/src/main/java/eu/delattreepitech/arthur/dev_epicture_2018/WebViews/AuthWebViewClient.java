package eu.delattreepitech.arthur.dev_epicture_2018.WebViews;

import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import eu.delattreepitech.arthur.dev_epicture_2018.Activities.Home;
import eu.delattreepitech.arthur.dev_epicture_2018.Activities.MainActivity;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;

public class AuthWebViewClient extends WebViewClient {
    private MainActivity _context;
    private boolean _stopRedirecting;

    public AuthWebViewClient(MainActivity context) {
        super();
        _context = context;
        _stopRedirecting = false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains("facebook") || url.contains("twitter") || url.contains("google") || url.contains("yahoo")) {
            view.loadUrl(url);
            return true;
        } else {
            if (url.contains("callback?error=access_denied")) {
               accessDenied();
            } else {
                if (!_stopRedirecting) {
                    _stopRedirecting = true;
                    accessGranted(url);
                }
            }
            return false;
        }
    }

    private void accessDenied() {
        Intent login = new Intent();
        login.putExtra("denied", true);
        _context.startActivity(login);
    }

    private void accessGranted(String url) {
        final Intent home = new Intent(_context, Home.class);
        home.putExtra("user", new Gson().toJson(new User(url)));
        _context.startActivity(home);
    }
}
