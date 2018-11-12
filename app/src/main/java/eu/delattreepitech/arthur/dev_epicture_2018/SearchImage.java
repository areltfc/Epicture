package eu.delattreepitech.arthur.dev_epicture_2018;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class SearchImage extends AppCompatActivity {

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        String tokensUrl = Objects.requireNonNull(getIntent().getExtras()).getString("tokensUrl");
        this.user = new User(tokensUrl);
        System.out.println(this.user.getAccessToken());
        this.searchImage();
    }

    protected void searchImage() {
    }
}
