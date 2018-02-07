package android.mxdlzg.com.bluewindmill.view.adapter;

import android.graphics.Color;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import android.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.mxdlzg.com.bluewindmill.view.fragment.SCFragment;
import android.mxdlzg.com.bluewindmill.view.fragment.ScheduleFragment;
import android.mxdlzg.com.bluewindmill.view.fragment.UserFragment;
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
        fragments.add(new SCFragment());
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
                            activity.getToolbar().setBackgroundColor(activity.getColor(R.color.blue700));
                            activity.getNiceSpinner().setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            Animation fadeIns = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
                            activity.getToolbar().startAnimation(fadeIns);
                            activity.getToolbar().setBackgroundColor(Color.TRANSPARENT);
                            activity.getNiceSpinner().setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            Animation fadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
                            activity.getToolbar().startAnimation(fadeIn);
                            activity.getToolbar().setBackgroundColor(Color.TRANSPARENT);
                            activity.getNiceSpinner().setVisibility(View.INVISIBLE);
                            break;
                        default:break;
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    MainActivity activity = (MainActivity) parentActivity;
                    activity.getViewPager().setCurrentItem(nextPosition,false);
                    activity.setMainFragment(activity.getNavigationAdapter().getCurrentFragment());
                    willBeDisplay();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void willBeDisplay() {
        if (currentFragment.getContainer() != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
            currentFragment.getContainer().startAnimation(fadeIn);
        }
    }

}
