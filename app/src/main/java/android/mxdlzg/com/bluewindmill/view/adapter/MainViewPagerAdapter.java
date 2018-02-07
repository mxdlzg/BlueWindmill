package android.mxdlzg.com.bluewindmill.view.adapter;

import android.graphics.Color;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import android.mxdlzg.com.bluewindmill.view.fragment.MainFragment;
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
    private ArrayList<MainFragment> fragments = new ArrayList<>();
    private MainFragment currentFragment;
    private View currentFragmentContainer;
    private AppCompatActivity parentActivity;
    
    
    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        
        fragments.clear();
        fragments.add(MainFragment.newInstance(0));
        fragments.add(MainFragment.newInstance(1));
        fragments.add(MainFragment.newInstance(2));
    }

    @Override
    public MainFragment getItem(int position) {
        currentFragmentContainer = fragments.get(position).getContainer();
        return fragments.get(position);
    }

    public void setParentActivity(AppCompatActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public MainFragment getCurrentFragment(){
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object){
            currentFragment = ((MainFragment)object);
        }
        super.setPrimaryItem(container, position, object);
    }



    //BottomNav
    public void willBeHidden(final int nextPosition) {
        if (currentFragmentContainer != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_out);
            currentFragmentContainer.startAnimation(fadeOut);
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
                    activity.getMainFragment().willBeDisplay();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public void willBeDisplay() {
        if (currentFragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fade_in);
            currentFragmentContainer.startAnimation(fadeIn);
        }
    }

}
