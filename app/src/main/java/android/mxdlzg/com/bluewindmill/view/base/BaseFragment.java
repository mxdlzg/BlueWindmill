package android.mxdlzg.com.bluewindmill.view.base;

import android.graphics.Color;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by mxdlzg on 18-2-6.
 */

public abstract class BaseFragment extends Fragment {
    protected LayoutInflater inflater;
    private boolean isFirst = true;
    private boolean isFinishLoad = false;
    private boolean isVisible;                  //是否可见状态
    protected View view;
    protected View fragmentContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = initView(inflater,container,savedInstanceState);
        this.isFinishLoad = true;
        lazyLoad();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    private void onInvisible() {
        onHide();
    }

    private void onVisible() {
        lazyLoad();
        onShow();
    }

    private void lazyLoad() {
        if (!isFinishLoad || !isVisible || !isFirst) {
            return;
        }
        isFirst = false;
        initData();
    }

    public View getContainer() {
        return fragmentContainer;
    }

    //子类需要实现此处函数
    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);  //初始化view、event
    protected abstract void initData();     //初始化fragment内的data
    protected void onShow(){}
    protected void onHide(){}
}
