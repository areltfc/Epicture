package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Adapter.AlbumAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Adapter.BaseAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Adapter.TagsAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Detailed extends AppCompatActivity {
    private OkHttpClient _client;
    private User _user;
    private Image _cover;
    private List<Image> _album;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.album);

        _cover = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("image"), Image.class);
        _user = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("user"), User.class);
        _client = new OkHttpClient.Builder().build();
        displayAlbum();
        TextView title = findViewById(R.id.album_name);
        title.setTextColor(this.getColor(R.color.white));
        title.setText(_cover.getName());
        //displayTags();
    }

    private void displayAlbum() {
        String endpoint;
        if (_cover.getId().equals(_cover.getRealId())) {
            endpoint = "https://api.imgur.com/3/image/" + _cover.getRealId();
        } else {
            endpoint = "https://api.imgur.com/3/gallery/album/" + _cover.getRealId();
        }
        final Request request = new Request.Builder().url(endpoint).addHeader("Authorization", "Bearer " + _user.getAccessToken()).build();
        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    _album = InterpretAPIRequest.JSONToAlbum(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { renderAlbum(); }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void renderAlbum() {
        RecyclerView v = findViewById(R.id.album_images_view);
        v.setLayoutManager(new LinearLayoutManager(Detailed.this));
        AlbumAdapter adapter = new AlbumAdapter(Detailed.this, _album, _user);
        v.setAdapter(adapter);
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
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
    }

/*    private void displayTags() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() { renderTags(); }
        });
    }

    private void renderTags() {
        RecyclerView v = findViewById(R.id.album_tags_view);
        v.setLayoutManager(new LinearLayoutManager(Detailed.this));
        TagsAdapter adapter = new TagsAdapter(Detailed.this, _cover.getTags(), _user);
        v.setAdapter(adapter);
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                outRect.right = 16;
            }
        });
    }*/

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
