package org.nasa.spaceapps.nasaspaceapps2018;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter{
    private FragmentManager fm;
    public FragmentAdapter(FragmentManager fm){
        super(fm);
        this.fm = fm;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment nsaFragment = new NSAFragment(position);
        Bundle args = new Bundle();
        args.putInt(NSAFragment.ARG_OBJECT,position);
        nsaFragment.setArguments(args);
        return nsaFragment;
    }
    @Override
    public int getCount() {
        return 4;
    }
}
