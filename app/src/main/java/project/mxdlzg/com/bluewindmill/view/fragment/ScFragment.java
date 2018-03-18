package project.mxdlzg.com.bluewindmill.view.fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.LoginRequest;
import project.mxdlzg.com.bluewindmill.net.request.SCRequest;
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
import android.widget.Toast;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import project.mxdlzg.com.bluewindmill.view.widgets.NoScrollViewPager;
import project.mxdlzg.com.bluewindmill.view.widgets.ViewPagerCustomDuration;

/**
 * Created by mxdlzg on 18-2-6.
 */

public class ScFragment extends BaseFragment {
    @BindView(R.id.second_viewPager)
    public NoScrollViewPager viewPager;
    @BindView(R.id.second_nts_center)
    public NavigationTabStrip navigationTabStrip;

    private ScViewPagerAdapter viewPagerAdapter;
    private Unbinder unbinder;
    private boolean firstEnterSearchView = false;


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
        //If not enter searchView, Set 0 Visible
        if (firstEnterSearchView){
            viewPagerAdapter.getItem(0).setWaitingSearchResult(true);
        }
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
        navigationTabStrip.setTabIndex(0);
        navigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                viewPagerAdapter.getItem(0).kindRefresh(title,index);
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                System.out.println("end select!!!");
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    public void enterSearchView() {
        firstEnterSearchView = true;
    }

    public void search(String keyword) {
        // TODO: 18-3-17 request.search
        // TODO: 18-3-17 search success
//        List<SCActivityDetail> list = new ArrayList<>();
//        list.add(new SCActivityDetail("111","【天天讲】【生态天天讲】野趣魔都——野生动植物保护二三事","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
//        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        SCRequest.requestSearchList(getContext(), keyword, new CommonCallback<NetResult<List<SCActivityDetail>>>() {
            @Override
            public void onSuccess(NetResult<List<SCActivityDetail>> message) {
                viewPagerAdapter.getItem(0).finishSearch(message.getData());
            }

            @Override
            public void onError(NetResult netResult) {
                viewPagerAdapter.getItem(0).finishSearch(null);
            }
        });
        viewPager.setCurrentItem(0);
        viewPagerAdapter.getItem(0).startSearch();
    }
}
