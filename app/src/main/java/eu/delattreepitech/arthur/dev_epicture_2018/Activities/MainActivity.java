package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import eu.delattreepitech.arthur.dev_epicture_2018.WebView.AuthWebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        Intent deniedCheck = getIntent();
        if (deniedCheck != null && deniedCheck.getBooleanExtra("denied", false)) {
            Toast.makeText(getApplicationContext(), "Please grant us access to your account", Toast.LENGTH_SHORT).show();
        }
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
