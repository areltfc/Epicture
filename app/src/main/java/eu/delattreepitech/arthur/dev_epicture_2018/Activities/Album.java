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
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Adapters.AlbumAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Adapters.TagsAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.RequestUtils.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.TextViews.MontserratTextView;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Album extends AppCompatActivity {
    private OkHttpClient _client;
    private User _user;
    private Image _cover;
    private List<Image> _album;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.album_layout);

        _cover = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("image"), Image.class);
        _user = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("user"), User.class);
        _client = new OkHttpClient.Builder().build();
        displayAlbum();
        displayTitle();
        displayAccount();
        displayTags();
        CheckBox favorite = findViewById(R.id.favorite);
        favorite.setChecked(_cover.getFavorite());
        if (favorite.isChecked()) {
            favorite.setText(getString(R.string.remove_from_favorites));
        } else {
            favorite.setText(getString(R.string.add_to_favorites));
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { switchFavorite((CheckBox) v); }
        });
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
                    _album = InterpretAPIRequest.JSONToAlbum(Objects.requireNonNull(response.body()).string());
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
        v.setLayoutManager(new LinearLayoutManager(Album.this));
        AlbumAdapter adapter = new AlbumAdapter(Album.this, _album, _user);
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

    private void displayTitle() {
        MontserratTextView title = findViewById(R.id.album_name);
        title.setText(_cover.getName());
    }

    private void displayAccount() {
        MontserratTextView title = findViewById(R.id.album_account);
        title.setText(this.getString(R.string.image_account, _cover.getUser()));
    }

    private void displayTags() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() { renderTags(); }
        });
    }

    private void renderTags() {
        RecyclerView v = findViewById(R.id.album_tags_view);
        v.setLayoutManager(new LinearLayoutManager(Album.this));
        v.setAdapter(new TagsAdapter(Album.this, _cover.getTags(), _user));
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                outRect.bottom = 16;
                outRect.left = 16;
            }
        });
    }

    private void switchFavorite(final CheckBox check) {
        String endpoint;
        if (_cover.getId().equals(_cover.getRealId())) {
            endpoint = "https://api.imgur.com/3/image/" + _cover.getRealId() + "/favorite";
        } else {
            endpoint = "https://api.imgur.com/3/album/" + _cover.getRealId() + "/favorite";
        }
        final RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("title", _cover.getName()).build();
        final Request request = new Request.Builder().url(endpoint).addHeader("Authorization", "Bearer " + _user.getAccessToken()).post(body).build();
        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "A network error occurred, please try again later", Toast.LENGTH_SHORT).show();
                        check.setChecked(!check.isChecked());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (check.isChecked()) {
                            check.setText(getString(R.string.add_to_favorites));
                            Toast.makeText(getBaseContext(), "The image was added to your favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            check.setText(getString(R.string.remove_from_favorites));
                            Toast.makeText(getBaseContext(), "The image was removed from your favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
