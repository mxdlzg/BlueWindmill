package android.mxdlzg.com.bluewindmill.view.adapter;

import android.mxdlzg.com.bluewindmill.view.fragment.MainFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by 廷江 on 2017/9/13.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<MainFragment> fragments = new ArrayList<>();
    private MainFragment currentFragment;
    
    
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        
        fragments.clear();
        fragments.add(MainFragment.newInstance(0));
        fragments.add(MainFragment.newInstance(1));
        fragments.add(MainFragment.newInstance(2));
    }

    @Override
    public MainFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public MainFragment getCurrentFragment(){
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object){
            currentFragment = ((MainFragment)object);
        }
        super.setPrimaryItem(container, position, object);
    }
}
