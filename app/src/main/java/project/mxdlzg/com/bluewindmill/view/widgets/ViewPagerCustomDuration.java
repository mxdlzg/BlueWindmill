package project.mxdlzg.com.bluewindmill.view.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * Created by mxdlzg on 18-2-10.
 */

public class ViewPagerCustomDuration extends ViewPager {
    public ViewPagerCustomDuration(@NonNull Context context) {
        super(context);
        postInitViewPager();
    }

    public ViewPagerCustomDuration(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    private ScrollerCustomDuration scrollerCustomDuration = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void postInitViewPager() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            scrollerCustomDuration = new ScrollerCustomDuration(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, scrollerCustomDuration);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        scrollerCustomDuration.setScrollDurationFactor(scrollFactor);
    }
}
