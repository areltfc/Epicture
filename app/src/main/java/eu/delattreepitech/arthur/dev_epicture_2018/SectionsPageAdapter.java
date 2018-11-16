package eu.delattreepitech.arthur.dev_epicture_2018;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionsPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> _tabs = new ArrayList<>();
    private final List<String> _names = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String name) {
        _tabs.add(fragment);
        _names.add(name);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return _names.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return _tabs.get(i);
    }

    @Override
    public int getCount() { return _tabs.size(); }
}
