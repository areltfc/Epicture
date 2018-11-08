package eu.delattreepitech.arthur.dev_epicture_2018;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            WebView view = new WebView(this);
            WebViewClient redirectUrl = new AuthWebViewClient(this);
            view.setWebViewClient(redirectUrl);
            view.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=7f1c902a0216035&response_type=token&state=application");
            setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
