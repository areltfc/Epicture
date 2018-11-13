package eu.delattreepitech.arthur.dev_epicture_2018.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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

public class Search extends AppCompatActivity {

    private User _user = null;
    private OkHttpClient _client;
    private SearchView _bar;
    private Spinner _sort;
    private Spinner _window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.search_layout);

        String tokensUrl = Objects.requireNonNull(getIntent().getExtras()).getString("tokensUrl");
        assert tokensUrl != null;
        _user = new User(tokensUrl);
        _client = new OkHttpClient.Builder().build();
        _bar = findViewById(R.id.search_bar);
        _bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displaySearch(query);
                _bar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence query = _bar.getQuery();
                String q = query.toString();
                if (!q.isEmpty()) {
                    displaySearch(q);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        _sort = findViewById(R.id.search_sort);
        _sort.setOnItemSelectedListener(listener);
        _window = findViewById(R.id.search_window);
        _window.setOnItemSelectedListener(listener);
    }

    protected void displaySearch(String query) {
        String url = "https://api.imgur.com/3/gallery/search/" + _sort.getSelectedItem().toString().toLowerCase() + "/" + _window.getSelectedItem().toString().toLowerCase() + "/?q=" + query;
        try {
            Request request = new Request.Builder().url(url)
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
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 16;
            }
        });
    }

    public void onClickHome(MenuItem item) {
        final Intent home = new Intent(Search.this, Home.class);
        home.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(home);
    }

    public void onClickProfile(MenuItem item) {
        final Intent profile = new Intent(Search.this, Profile.class);
        profile.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(profile);
    }

    public void onClickSearch(MenuItem item) {
        final Intent search = new Intent(Search.this, Search.class);
        search.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(search);
    }
}
