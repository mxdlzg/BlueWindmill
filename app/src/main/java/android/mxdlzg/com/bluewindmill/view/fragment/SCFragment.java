package android.mxdlzg.com.bluewindmill.view.fragment;

import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mxdlzg on 18-2-6.
 */

public class SCFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_class, container, false);
        return view;
    }

    @Override
    protected void initData() {
        initSecondClassSetting(view);
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
