package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import eu.delattreepitech.arthur.dev_epicture_2018.AuthWebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        try {
            this.redirectLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void redirectLogin() {
        try {
            WebView view = new WebView(this);
            view.setWebViewClient(new AuthWebViewClient(this));
            view.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=7f1c902a0216035&response_type=token");
            setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
