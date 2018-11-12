package eu.delattreepitech.arthur.dev_epicture_2018.activities;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.ImageViewHolder;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
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
            Response response = _client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.out.println("Request was not successful");
            } else {
                JSONObject obj = new JSONObject(response.body().string());
                JSONArray data = obj.getJSONArray("data");
                final List<Image> images = new ArrayList<Image>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject item = data.getJSONObject(i);
                    Image image = new Image();
                    if (item.getBoolean("is_album")) {
                        image.setId(item.getString("cover"));
                    } else {
                        image.setId(item.getString("id"));
                    }
                    image.setName(item.getString("title"));
                    images.add(image);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        render(images);
                    }
                });
            }
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
        System.out.println(item);
    }

    public void onClickProfile(MenuItem item) {
        System.out.println(item);
    }

    public void onClickSearch(MenuItem item) {
        System.out.println(item);
    }
}
