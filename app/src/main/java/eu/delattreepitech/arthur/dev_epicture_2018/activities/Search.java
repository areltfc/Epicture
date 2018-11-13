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
import android.widget.SearchView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.ImageViewHolder;
import eu.delattreepitech.arthur.dev_epicture_2018.JsonToList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.search_layout);

        String tokensUrl = Objects.requireNonNull(getIntent().getExtras()).getString("tokensUrl");
        assert tokensUrl != null;
        _user = new User(tokensUrl);
        _client = new OkHttpClient.Builder().build();
        final SearchView bar = findViewById(R.id.search_bar);
        bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displaySearch(query);
                bar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected void displaySearch(String query) {
        try {
            Request request = new Request.Builder().url("https://api.imgur.com/3/gallery/search/?q=" + query)
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
                        final List<Image> images = JsonToList.Images(response.body().string());
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
        RecyclerView.Adapter<ImageViewHolder> adapter = new RecyclerView.Adapter<ImageViewHolder>() {
            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageViewHolder vh = new ImageViewHolder(getLayoutInflater().inflate(R.layout.home_view_item, null));
                vh._image = vh.itemView.findViewById(R.id.photo);
                vh._name = vh.itemView.findViewById(R.id.title);
                return vh;
            }

            @Override
            public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
                Picasso.get().load("https://i.imgur.com/" + images.get(i).getId() + ".jpg").into(imageViewHolder._image);
                imageViewHolder._name.setText(images.get(i).getName());
            }

            @Override
            public int getItemCount() {
                return images.size();
            }
        };
        v.setAdapter(adapter);
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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
        final Intent home = new Intent(Search.this, Profile.class);
        home.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(home);
    }

    public void onClickSearch(MenuItem item) {}
}
