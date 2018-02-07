package project.mxdlzg.com.bluewindmill.view.fragment;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;

/**
 * Created by mxdlzg on 18-2-6.
 */

public class SCFragment extends BaseFragment {
    @BindView(R.id.second_viewPager)
    public ViewPager viewPager;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_class, container, false);
        initSecondClassSetting(view);
        return view;
    }

    @Override
    protected void initData() {
    }

    private void initSecondClassSetting(View view) {
        fragmentContainer = (RelativeLayout) view.findViewById(R.id.second_class_container);
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
