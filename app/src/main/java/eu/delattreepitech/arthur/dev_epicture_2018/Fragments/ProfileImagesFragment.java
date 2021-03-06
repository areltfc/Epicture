package eu.delattreepitech.arthur.dev_epicture_2018.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
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
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileImagesFragment extends Fragment {
    public static final int PICK_IMAGE = 1;

    OkHttpClient _client;
    User _user;
    List<Image> _images;
    int _pageNumber;
    View _root;
    RecyclerView _rv;
    EndlessScrollListener _endlessScrollListener;
    BaseAdapter _adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _client = new OkHttpClient.Builder().build();
        _user = new Gson().fromJson(Objects.requireNonNull(getArguments()).getString("user"), User.class);
        return inflater.inflate(R.layout.profile_images_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);

        _images = new ArrayList<>();
        _pageNumber = 0;
        _root = view;

        setupEndlessScrollListener();
        setupRecyclerView();
        final FloatingActionButton upload = view.findViewById(R.id.floatingActionButton);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_IMAGE);
                    }
                });
            }
        });
        displayImages();
    }

    void setupEndlessScrollListener() {
        _endlessScrollListener = new EndlessScrollListener(new EndlessScrollListener.RefreshList() {
            @Override
            public void onRefresh(EndlessScrollListener listener) {
                displayImages();
                listener.notifyMorePages();
            }
        });
    }

    void setupRecyclerView() {
        _rv = _root.findViewById(R.id.recyclerview);

        _rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        _rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 16;
                outRect.bottom = 16;
                outRect.right = 16;
                outRect.left = 16;
            }
        });
        _rv.addOnScrollListener(_endlessScrollListener);
    }

    void displayImages() {
        try {
            Request request = new Request.Builder().url("https://api.imgur.com/3/account/me/images/" + _pageNumber)
                    .addHeader("Authorization", "Bearer " + _user.getAccessToken())
                    .build();
            _client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "A network error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        _pageNumber++;
                        final List<Image> additions = InterpretAPIRequest.JSONToImages(Objects.requireNonNull(response.body()).string());
                        if (additions.size() > 0) {
                            _images.addAll(additions);
                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
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
            _adapter = new BaseAdapter(getActivity(), _images, _user);
            _rv.setAdapter(_adapter);
        } else {
            _adapter.notifyDataSetChanged();
        }
    }

    void uploadImage(String imageStr) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MediaType.parse("image/jpg"), imageStr))
                .build();
        Request request = new Request.Builder().addHeader("Authorization", "Bearer " + _user.getAccessToken())
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();
        _client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Upload is starting...", Toast.LENGTH_SHORT).show();
            try {
                InputStream inputStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                byte[] imageBytes = new byte[(int) Objects.requireNonNull(inputStream).available()];
                inputStream.read(imageBytes, 0, imageBytes.length);
                inputStream.close();
                uploadImage(Base64.encodeToString(imageBytes, Base64.DEFAULT));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ProfileImagesFragment newInstance(User user) {
        Bundle args = new Bundle();
        ProfileImagesFragment fragment = new ProfileImagesFragment();
        args.putString("user", new Gson().toJson(user));
        fragment.setArguments(args);
        return fragment;
    }
}
