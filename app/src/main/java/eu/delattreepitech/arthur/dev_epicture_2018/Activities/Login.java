package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;

import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import eu.delattreepitech.arthur.dev_epicture_2018.WebViews.AuthWebViewClient;

public class Login extends AppCompatActivity {
    final static String CLIENT_ID = "7f1c902a0216035";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        lookForExistingTokens();

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

    void lookForExistingTokens() {
        final SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preferences_file), Context.MODE_PRIVATE);
        final String tokens = sharedPref.getString("tokens", null);

        if (tokens != null) {
            final Intent home = new Intent(getApplicationContext(), Home.class);
            home.putExtra("user", new Gson().toJson(new User(tokens)));
            startActivity(home);
        }
    }

    void redirectLogin() {
        try {
            WebView view = new WebView(this);
            view.setWebViewClient(new AuthWebViewClient(this));
            view.loadUrl("https://api.imgur.com/oauth2/authorize?client_id= " + CLIENT_ID + "&response_type=token");
            setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
