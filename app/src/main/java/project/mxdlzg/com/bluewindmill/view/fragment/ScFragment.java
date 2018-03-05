package project.mxdlzg.com.bluewindmill.view.fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.util.Util;
import project.mxdlzg.com.bluewindmill.view.adapter.ScViewPagerAdapter;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import butterknife.BindView;
import project.mxdlzg.com.bluewindmill.view.widgets.ViewPagerCustomDuration;

/**
 * Created by mxdlzg on 18-2-6.
 */

public class ScFragment extends BaseFragment {
    @BindView(R.id.second_viewPager)
    public ViewPagerCustomDuration viewPager;
    @BindView(R.id.second_nts_center)
    public NavigationTabStrip navigationTabStrip;

    private ScViewPagerAdapter viewPagerAdapter;
    private Unbinder unbinder;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_class, container, false);

        //Bind
        unbinder = ButterKnife.bind(this,view);

        initSecondClass(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initData() {
        viewPagerAdapter.getItem(0).setUserVisibleHint(true);
    }

    private void initSecondClass(View view) {


        //Container
        fragmentContainer = (RelativeLayout) view.findViewById(R.id.second_class_container);

        //View Pager
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new ScViewPagerAdapter(this.getChildFragmentManager(),this.getContext());
        viewPager.setAdapter(viewPagerAdapter);

        //Tab
        navigationTabStrip.setViewPager(viewPager,0);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onHide() {
        super.onHide();
    }
}
