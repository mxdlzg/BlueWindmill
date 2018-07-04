package com.longmai.mtoken.ui.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.longmai.mtoken.widget.MResource;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends Activity {
    private static List mActivityList = new ArrayList();
    protected View mBackArrow;
    protected ImageView mBackArrowImg;
    private RelativeLayout mContent;
    private Button mRightOptionButton;
    public ImageView mRightOptionImage;
    private View mRightOptionLayout;
    protected View mTitleBar;
    protected View mTitleDivider;
    protected TextView mTitleText;

    final class BackEvent implements OnClickListener {
        private BaseActivity activity;

        public BackEvent(BaseActivity activity) {
            this.activity = activity;
        }

        public void onClick(View view) {
            this.activity.finish();
        }
    }

    protected abstract void initWidget();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setRequestedOrientation(1);
        initWidget();
        mActivityList.add(this);
    }

    private View getContentView() {
        return ((ViewGroup) getWindow().getDecorView()).getChildAt(0);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(MResource.getIdByName(getApplication(), "layout", "activity_frame"));
        View localView = getLayoutInflater().inflate(layoutResID, null);
        this.mContent = (RelativeLayout) findViewById(MResource.getIdByName(getApplication(), "id", "content"));
        this.mContent.addView(localView, new LayoutParams(-1, -1));
        getContentView().setBackgroundDrawable(localView.getBackground());
        initTitleBar();
    }

    private void initTitleBar() {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        this.mTitleBar = findViewById(MResource.getIdByName(getApplication(), "id", "title_bar"));
        this.mTitleText = (TextView) findViewById(MResource.getIdByName(getApplication(), "id", "bar_title"));
        this.mTitleDivider = findViewById(MResource.getIdByName(getApplication(), "id", "title_bar_divider"));
        ((MarginLayoutParams) this.mTitleBar.getLayoutParams()).height = localDisplayMetrics.heightPixels / 12;
        this.mBackArrow = findViewById(MResource.getIdByName(getApplication(), "id", "bar_back_button"));
        this.mBackArrowImg = (ImageView) findViewById(MResource.getIdByName(getApplication(), "id", "bar_back_img"));
        this.mRightOptionButton = (Button) findViewById(MResource.getIdByName(getApplication(), "id", "bar_right_button"));
        this.mBackArrow.setVisibility(0);
        this.mBackArrow.setOnClickListener(new BackEvent(this));
    }

    public void setRightTitleImage(int resId, OnClickListener l) {
        if (this.mRightOptionImage == null) {
            this.mRightOptionLayout = findViewById(MResource.getIdByName(getApplication(), "id", "bar_right_layout"));
            this.mRightOptionImage = (ImageView) findViewById(MResource.getIdByName(getApplication(), "id", "bar_right_image"));
        }
        this.mRightOptionLayout.setVisibility(0);
        this.mRightOptionImage.setImageResource(resId);
        this.mRightOptionLayout.setTag(this.mRightOptionImage);
        this.mRightOptionLayout.setOnClickListener(l);
    }

    public void setRightTitleImageHide() {
        if (this.mRightOptionImage != null) {
            this.mRightOptionLayout.setVisibility(8);
        }
    }

    public void hideTitle() {
        if (this.mTitleBar != null) {
            this.mTitleBar.setVisibility(8);
        }
    }

    public void hideBackArrow() {
        if (this.mBackArrow != null) {
            this.mBackArrow.setVisibility(8);
        }
    }

    public Button getRightTitleButton() {
        return this.mRightOptionButton;
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        try {
            mActivityList.remove(this);
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void exit() {
//        for (Activity finish : mActivityList) {
//            finish.finish();
//        }
    }

    public static void clearAllActivities() {
        for (int i = 0; i < mActivityList.size(); i++) {
            Activity localActivity = (Activity) mActivityList.get(i);
            if (!(localActivity == null || localActivity.isFinishing())) {
                localActivity.finish();
            }
        }
        mActivityList.clear();
    }

    public void setTitle(CharSequence title) {
        if (this.mTitleBar != null && this.mTitleText != null) {
            this.mTitleBar.setVisibility(0);
            this.mTitleText.setText(title);
        }
    }

    public void setTitle(int titleId) {
        if (this.mTitleBar != null && this.mTitleText != null) {
            this.mTitleBar.setVisibility(0);
            this.mTitleText.setText(getString(titleId));
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
