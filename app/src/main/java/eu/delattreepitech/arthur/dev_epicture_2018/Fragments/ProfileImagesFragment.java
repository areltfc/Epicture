package eu.delattreepitech.arthur.dev_epicture_2018.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Adapter.BaseAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Image;
import eu.delattreepitech.arthur.dev_epicture_2018.InterpretAPIRequest;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileImagesFragment extends Fragment {
    OkHttpClient _client;
    User _user;

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
        displayImages(view);
    }

    private void displayImages(final View root) {
        try {
            Request request = new Request.Builder().url("https://api.imgur.com/3/account/me/images/")
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
                        final List<Image> images = InterpretAPIRequest.JSONToImages(Objects.requireNonNull(response.body()).string());
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                render(root, images);
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

    private void render(final View root, final List<Image> images) {
        RecyclerView v = root.findViewById(R.id.recyclerview);
        v.setLayoutManager(new LinearLayoutManager(getActivity()));
        BaseAdapter adapter = new BaseAdapter(getActivity(), images, _user);
        v.setAdapter(adapter);
        v.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 16;
                outRect.bottom = 16;
                outRect.right = 16;
                outRect.left = 16;
            }
        });
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
