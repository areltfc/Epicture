package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

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

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Adapters.BaseAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.RequestUtils.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import eu.delattreepitech.arthur.dev_epicture_2018.RecyclerView.OnScrollListeners.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    User _user;
    OkHttpClient _client;
    List<Image> _images;
    int _pageNumber;
    RecyclerView _rv;
    EndlessScrollListener _endlessScrollListener;
    BaseAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.home_layout);

        _user = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("user"), User.class);
        _client = new OkHttpClient.Builder().build();
        _images = new ArrayList<>();
        _pageNumber = 0;

        setupEndlessScrollListener();
        setupRecyclerView();
        displayHome();
    }

    void setupEndlessScrollListener() {
        _endlessScrollListener = new EndlessScrollListener(new EndlessScrollListener.RefreshList() {
            @Override
            public void onRefresh(int pageNumber, EndlessScrollListener listener) {
                _pageNumber = pageNumber + 1;
                displayHome();
                listener.notifyMorePages();
            }
        });
    }

    void setupRecyclerView() {
        _rv = findViewById(R.id.home_view);

        _rv.setLayoutManager(new LinearLayoutManager(this));
        _rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                outRect.top = 16;
                outRect.bottom = 16;
                outRect.right = 16;
                outRect.left = 16;
            }
        });
        _rv.addOnScrollListener(_endlessScrollListener);
    }

    void displayHome() {
        try {
            String requestUrl = "https://api.imgur.com/3/gallery/hot/viral/" + _pageNumber;
            final Request request = new Request.Builder().url(requestUrl)
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
                        final List<Image> additions = InterpretAPIRequest.JSONToImages(Objects.requireNonNull(response.body()).string());
                        if (additions.size() > 0) {
                            _images.addAll(additions);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    render();
                                }
                            });
                        } else {
                            _endlessScrollListener.noMorePages();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void render() {
        if (_adapter == null) {
            _adapter = new BaseAdapter(Home.this, _images, _user);
            _rv.setAdapter(_adapter);
        } else {
            _adapter.notifyDataSetChanged();
        }
    }

    public void onClickHome(MenuItem item) {
        final Intent home = new Intent(this, Home.class);
        home.putExtra("user", new Gson().toJson(_user));
        startActivity(home);
    }

    public void onClickProfile(MenuItem item) {
        final Intent profile = new Intent(this, Profile.class);
        profile.putExtra("user", new Gson().toJson(_user));
        startActivity(profile);
    }

    public void onClickSearch(MenuItem item) {
        final Intent search = new Intent(this, Search.class);
        search.putExtra("user", new Gson().toJson(_user));
        startActivity(search);
    }
}
