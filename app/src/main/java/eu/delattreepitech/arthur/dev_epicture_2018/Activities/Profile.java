package eu.delattreepitech.arthur.dev_epicture_2018.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.Objects;

import eu.delattreepitech.arthur.dev_epicture_2018.Fragments.ProfileFavoritesFragment;
import eu.delattreepitech.arthur.dev_epicture_2018.Fragments.ProfileImagesFragment;
import eu.delattreepitech.arthur.dev_epicture_2018.R;
import eu.delattreepitech.arthur.dev_epicture_2018.SectionsPageAdapters.SectionsPageAdapter;
import eu.delattreepitech.arthur.dev_epicture_2018.Types.User;

public class Profile extends AppCompatActivity {

    private User _user = null;
    private SectionsPageAdapter _sectionsPageAdapter;
    private ViewPager _viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        this.setContentView(R.layout.profile_layout);
        _user = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString("user"), User.class);
        _sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        _viewPager = findViewById(R.id.container);
        setupViewPager(_viewPager);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(_viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        _sectionsPageAdapter.addFragment(ProfileImagesFragment.newInstance(_user), "Posts");
        _sectionsPageAdapter.addFragment(ProfileFavoritesFragment.newInstance(_user), "Favorites");
        viewPager.setAdapter(_sectionsPageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
