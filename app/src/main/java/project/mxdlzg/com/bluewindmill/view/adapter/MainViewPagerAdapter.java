package project.mxdlzg.com.bluewindmill.view.adapter;

import android.graphics.Color;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import project.mxdlzg.com.bluewindmill.view.fragment.ScFragment;
import project.mxdlzg.com.bluewindmill.view.fragment.ScheduleFragment;
import project.mxdlzg.com.bluewindmill.view.fragment.UserFragment;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

/**
 * Created by 廷江 on 2017/9/13.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private BaseFragment currentFragment;
    private View currentFragmentContainer;
    private AppCompatActivity parentActivity;
    
    
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(new ScheduleFragment());
        fragments.add(new ScFragment());
        fragments.add(new UserFragment());
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    public void setParentActivity(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public BaseFragment getCurrentFragment(){
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object){
            currentFragment = ((BaseFragment)object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void setCurrentFragmentIndex(int position) {
        setCurrentFragment(fragments.get(position));
    }

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    //BottomNav
    public void willBeHidden(final int nextPosition) {
        if (currentFragment.getContainer() != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_out);
            currentFragment.getContainer().startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    MainActivity activity = (MainActivity) parentActivity;
                    switch (nextPosition){
                        case 0:
                            break;
                        case 1:
                            //Animation fadeIns = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
                            //activity.getToolbar().startAnimation(fadeIns);
                            //activity.hideToolbarColor();
                            activity.getNiceSpinner().setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            //Animation fadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
                            //activity.getToolbar().startAnimation(fadeIn);
                            activity.hideToolbarColor();
                            activity.getNiceSpinner().setVisibility(View.INVISIBLE);
                            break;
                        default:break;
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    MainActivity activity = (MainActivity) parentActivity;
                    activity.getViewPager().setCurrentItem(nextPosition,true);
                    activity.setMainFragment(activity.getNavigationAdapter().getCurrentFragment());

                    willBeDisplay(nextPosition);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void willBeDisplay(final int postion) {
        if (currentFragment.getContainer() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (postion == 0 || postion == 1){
                        MainActivity activity = (MainActivity) parentActivity;
                        activity.showToolbarColor();
                        if (postion == 0)
                        activity.getNiceSpinner().setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            currentFragment.getContainer().startAnimation(fadeIn);
        }

    }

}
