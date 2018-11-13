package eu.delattreepitech.arthur.dev_epicture_2018.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.BaseAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.ImageViewHolder;
import eu.delattreepitech.arthur.dev_epicture_2018.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {

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
        this.displayHome();
    }

    protected void displayHome() {
        try {
            Request request = new Request.Builder().url("https://api.imgur.com/3/gallery/hot/viral/")
                    .addHeader("Authorization", "Bearer " + _user.getAccessToken())
                    .build();
            _client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        final List<Image> images = InterpretAPIRequest.JSONToImages(response.body().string());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                render(images);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void render(final List<Image> images) {
        RecyclerView v = findViewById(R.id.home_view);
        v.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter adapter = new BaseAdapter(this, images, _user.getAccessToken());
        v.setAdapter(adapter);
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16;
            }
        });
    }

    public void onClickHome(MenuItem item) {
        final Intent home = new Intent(Home.this, Home.class);
        home.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(home);
    }

    public void onClickProfile(MenuItem item) {
        final Intent profile = new Intent(Home.this, Profile.class);
        profile.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(profile);
    }

    public void onClickSearch(MenuItem item) {
        final Intent search = new Intent(Home.this, Search.class);
        search.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(search);
    }
}
