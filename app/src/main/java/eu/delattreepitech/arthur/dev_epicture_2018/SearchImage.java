package eu.delattreepitech.arthur.dev_epicture_2018;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

public class SearchImage extends AppCompatActivity {

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String accessToken = Objects.requireNonNull(getIntent().getExtras()).getString("AccessToken");
        System.out.println(accessToken);

        this.searchImage();
    }

    protected void searchImage() {
    }
}
