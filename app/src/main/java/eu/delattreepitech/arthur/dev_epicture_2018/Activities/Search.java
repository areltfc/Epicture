package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Adapters.BaseAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.RecyclerView.OnScrollListeners.EndlessScrollListener;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.RequestUtils.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;
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
    private boolean _spinnerUpdate;
    private RecyclerView _rv;
    private EndlessScrollListener _endlessScrollListener;
    private BaseAdapter _adapter;
    private int _pageNumber = 0;
    private List<Image> _images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.search_layout);

        _user = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("user"), User.class);
        _client = new OkHttpClient.Builder().build();
        _images = new ArrayList<>();

        setupEndlessScrollListener();
        setupRecyclerView();
        setupSearchBar();
        setupSpinners();

        String redirectedQuery = Objects.requireNonNull(getIntent().getExtras()).getString("query");
        if (redirectedQuery != null) {
            _bar.setQuery("#" + redirectedQuery, true);
        }
    }

    private void setupEndlessScrollListener() {
        _endlessScrollListener = new EndlessScrollListener(new EndlessScrollListener.RefreshList() {
            @Override
            public void onRefresh(EndlessScrollListener listener) {
                listener.notifyMorePages();
                displaySearch();
            }
        });
    }

    private void setupRecyclerView() {
        _rv = findViewById(R.id.search_view);

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

    private void setupSearchBar() {
        _bar = findViewById(R.id.search_bar);

        _bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                _bar.clearFocus();
                _pageNumber = 0;
                _endlessScrollListener.reset();
                displaySearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setupSpinners() {
        _sort = findViewById(R.id.search_sort);
        _window = findViewById(R.id.search_window);
        _spinnerUpdate = false;

        _sort.setSelection(0, false);
        _window.setSelection(0, false);
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!_bar.getQuery().toString().isEmpty()) {
                    _spinnerUpdate = true;
                    _pageNumber = 0;
                    _endlessScrollListener.reset();
                    displaySearch();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        _sort.setOnItemSelectedListener(listener);
        _window.setOnItemSelectedListener(listener);
    }

    private String getEndpoint() {
        String query = _bar.getQuery().toString();
        String endpoint;

        if (query.charAt(0) == '#') {
            endpoint = "https://api.imgur.com/3/gallery/t/" + query.replace(' ', '_').substring(1)
                    + "/" + _sort.getSelectedItem().toString().toLowerCase()
                    + "/" + _window.getSelectedItem().toString().toLowerCase() + "/" + _pageNumber;
        } else {
            endpoint = "https://api.imgur.com/3/gallery/search/"
                    + _sort.getSelectedItem().toString().toLowerCase()
                    + "/" + _window.getSelectedItem().toString().toLowerCase()
                    + "/" + _pageNumber + "/?q=" + query;
        }
        return endpoint;
    }

    private void displaySearch() {
        try {
            Request request = new Request.Builder().url(getEndpoint())
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
                        if (response.isSuccessful()) {
                            _pageNumber++;
                            if (_spinnerUpdate) {
                                _images = new ArrayList<>();
                            }
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
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                                }
                            });
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
    private void render() {
        if (_adapter == null || _spinnerUpdate) {
            _spinnerUpdate = false;
            _adapter = new BaseAdapter(Search.this, _images, _user);
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
