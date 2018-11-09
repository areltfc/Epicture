package eu.delattreepitech.arthur.dev_epicture_2018;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            this.redirectLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void redirectLogin() {
        try {
            final Intent searchImage = new Intent(this, SearchImage.class);

            WebView view = new WebView(this);
            view.setWebViewClient(new AuthWebViewClient(this) {
                public void onPageFinished(WebView view, String url) {
                    searchImage.putExtra("AccessToken", getUser().getAccessToken());
                    startActivity(searchImage);
                }
            });
            view.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=7f1c902a0216035&response_type=token");
            setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) { this.user = user; }

    public User getUser() { return user; }
}
