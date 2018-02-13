package project.mxdlzg.com.bluewindmill.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.mxdlzg.com.bluewindmill.R;

/**
 * Created by mxdlzg on 18-2-9.
 */

public class ScDetailActivity extends AppCompatActivity {
    @BindView(R.id.sc_dtl_btnApply)
    CircularProgressButton btnApply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sc_activity_detail);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(null);
        }
        final Animation fadein = AnimationUtils.loadAnimation(this,R.anim.view_fade_in);

        findViewById(R.id.sc_dtl_progressBar).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.sc_dtl_progressBar).setVisibility(View.GONE);
                findViewById(R.id.relativeLayout2).setVisibility(View.VISIBLE);
                findViewById(R.id.relativeLayout2).startAnimation(fadein);
            }
        },1000);


    }

    @OnClick(R.id.sc_dtl_btnApply)
    void apply(final CircularProgressButton btnApply){
        btnApply.startAnimation();
        btnApply.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnApply.revertAnimation(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        btnApply.setText("√");
                    }
                });
                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnApply.dispose();
    }
}
