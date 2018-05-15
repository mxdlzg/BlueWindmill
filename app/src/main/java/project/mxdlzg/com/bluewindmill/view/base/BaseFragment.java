package project.mxdlzg.com.bluewindmill.view.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.view.WindApplication;

/**
 * Created by mxdlzg on 18-2-6.
 */

public abstract class BaseFragment extends Fragment {
    protected LayoutInflater inflater;  //inflater
    private boolean isFirst = true;     //First load
    private boolean isFinishLoad = false; //Finish load
    private boolean isReload = false;       //Reload
    protected boolean isVisible;          //是否可见状态
    protected View view;                //fragment view
    protected View fragmentContainer;   //view->container
    protected Unbinder unbinder;        //butter knife unbinder

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = initView(inflater,container,savedInstanceState);
        this.isFinishLoad = true;
        Log.e("fragment","createView");
        if (isReload){
            Log.e("fragment","createView--Reload");
            lazyLoad();
            isReload = false;
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WindApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
        Log.w("fragment","destory");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.isFirst = true;
        this.isFinishLoad = false;
        this.isReload = true;
        Log.w("fragment","destoryView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w("fragment","detach");
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
        Log.w("fragment","enter lazyLoad!!");
        if (!isFinishLoad || !isVisible || !isFirst) {
            return;
        }
        Log.e("fragment","Do lazyLoad!!");
        isFirst = false;
        initData();
    }

    public View getContainer() {
        return fragmentContainer;
    }

    /**
     * 在new fragment时调用，初始化基本的view
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved
     * @return view
     */
    //子类需要实现此处函数
    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);  //初始化view、event

    /**
     * 在fragment进入visible状态并且为第一次加载时调用
     */
    protected abstract void initData();     //初始化fragment内的data

    /**
     * fragment进入visible状态调用
     */
    protected void onShow(){}

    /**
     * fragment进入hide状态调用
     */
    protected void onHide(){}
}
