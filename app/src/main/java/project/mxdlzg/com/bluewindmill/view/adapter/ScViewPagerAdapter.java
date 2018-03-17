package project.mxdlzg.com.bluewindmill.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import project.mxdlzg.com.bluewindmill.view.fragment.ScDetailFragment;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class ScViewPagerAdapter extends FragmentPagerAdapter {
    private List<ScDetailFragment> list = new ArrayList<>();
    private Context context;

    public void setList(List<ScDetailFragment> list) {
        this.list = list;
    }

    public ScViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
        list.clear();
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
        list.add(new ScDetailFragment());
    }

    public void addFragment(String categoryID){
        ScDetailFragment detailFragment = new ScDetailFragment();
        detailFragment.setCategoryID(categoryID);
        list.add(detailFragment);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public ScDetailFragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
