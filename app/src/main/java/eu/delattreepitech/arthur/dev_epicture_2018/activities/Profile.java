package eu.delattreepitech.arthur.dev_epicture_2018.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
import okhttp3.OkHttpClient;

public class Profile extends AppCompatActivity {

    private User _user = null;
    private OkHttpClient _client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.activity_main);

        String tokensUrl = Objects.requireNonNull(getIntent().getExtras()).getString("tokensUrl");
        assert tokensUrl != null;
        _user = new User(tokensUrl);
        _client = new OkHttpClient.Builder().build();
    }

    public void onClickHome(MenuItem item) {
        final Intent home = new Intent(Profile.this, Home.class);
        home.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(home);
    }

    public void onClickProfile(MenuItem item) {}

    public void onClickSearch(MenuItem item) {
        final Intent search = new Intent(Profile.this, Search.class);
        search.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(search);
    }
}
