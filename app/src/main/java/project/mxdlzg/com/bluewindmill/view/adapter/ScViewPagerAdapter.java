package project.mxdlzg.com.bluewindmill.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class ScViewPagerAdapter extends FragmentPagerAdapter {
    private List<SCActivityDetail> list;
    private Context context;

    public void setList(List<SCActivityDetail> list) {
        this.list = list;
    }

    public ScViewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
