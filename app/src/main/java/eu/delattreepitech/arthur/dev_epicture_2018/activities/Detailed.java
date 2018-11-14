package eu.delattreepitech.arthur.dev_epicture_2018.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Detailed extends AppCompatActivity {
    private OkHttpClient _client;
    private String _accessToken;
    private String _imageId;
    private Image _image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.image_detailed);

        _image = new Image();
        _imageId = Objects.requireNonNull(getIntent().getExtras()).getString("id");
        assert _imageId != null;
        _accessToken = Objects.requireNonNull(getIntent().getExtras()).getString("accessToken");
        assert _accessToken != null;
        _client = new OkHttpClient.Builder().build();
        displayImage();
    }

    private void displayImage() {
        final Request request = new Request.Builder().url("https://api.imgur.com/3/image/" + _imageId)
                .addHeader("Authorization", "Bearer " + _accessToken).build();
        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    _image = InterpretAPIRequest.JSONToImage(response.body().string());
                    render();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void render() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_image.getType().equals("mp4")) {

                    RelativeLayout layout = findViewById(R.id.image_detailed_layout);

                    VideoView vv = new VideoView(getBaseContext());
                    Uri uri = Uri.parse("https://i.imgur.com/" + _imageId + ".mp4");
                    vv.setVideoURI(uri);
                    vv.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));

                    vv.start();
                    layout.addView(vv);

                    vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });
                } else {
                    ImageView iv = findViewById(R.id.image_detailed_view);
                    Glide.with(getBaseContext())
                            .load("https://i.imgur.com/" + _imageId + ".gif")
                            .apply(new RequestOptions()
                                    .fitCenter())
                            .into(iv);
                }
                ((TextView) findViewById(R.id.image_detailed_title)).setText(_image.getName());
                ((TextView) findViewById(R.id.image_detailed_user)).setText(_image.getName());
                ((TextView) findViewById(R.id.image_detailed_tags)).setText(_image.getName());
            }
        });
    }

    public void onClickHome(MenuItem item) {
        final Intent home = new Intent(Detailed.this, Home.class);
        home.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(home);
    }

    public void onClickProfile(MenuItem item) {
        final Intent profile = new Intent(Detailed.this, Profile.class);
        profile.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(profile);
    }

    public void onClickSearch(MenuItem item) {
        final Intent search = new Intent(Detailed.this, Search.class);
        search.putExtra("tokensUrl", getIntent().getStringExtra("tokensUrl"));
        startActivity(search);
    }
}
