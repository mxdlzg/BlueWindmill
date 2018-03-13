package project.mxdlzg.com.bluewindmill.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.inspector.screencast.ScreencastDispatcher;
import com.lzy.okgo.OkGo;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.SCRequest;

/**
 * Created by mxdlzg on 18-2-9.
 */

public class ScDetailActivity extends AppCompatActivity {
    @BindView(R.id.sc_dtl_btnApply)
    CircularProgressButton btnApply;
    @BindView(R.id.sc_dtl_label)
    TextView tvDtlLabel;
    @BindView(R.id.sc_dtl_WeekTime)
    TextView tvWeekTime;
    @BindView(R.id.sc_dtl_Time)
    TextView tvTime;
    @BindView(R.id.sc_dtl_TitleContent)
    TextView tvTitle;

    @BindView(R.id.sc_dtl_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.relativeLayout2)
    RelativeLayout bottomLayout;
    @BindViews({R.id.sc_dtl_tvLocation,R.id.sc_dtl_tvTimeSpan,R.id.sc_dtl_tvStartEndTime,R.id.sc_dtl_tvHost,R.id.sc_dtl_tvContent})
    List<TextView> detailTvList;

    private SCActivityDetail item;
    private CommonCallback<SCActivityDetail> commonCallback = new CommonCallback<SCActivityDetail>() {
        @Override
        public void onSuccess(SCActivityDetail message) {
            detailTvList.get(0).setText(message.getLocation());
            detailTvList.get(1).setText(message.getTimeSpan());
            detailTvList.get(2).setText(message.getCardStart()+getString(R.string.str_until)+message.getCardEnd());
            detailTvList.get(3).setText(message.getHost()+"       "+message.getOrganizer());
            detailTvList.get(4).setText(message.getContent());
            showDetail(true);
        }

        @Override
        public void onError(NetResult netResult) {
            super.onError(netResult);
            detailTvList.get(4).setText("获取活动详情失败！");
            showDetail(false);
        }
    };

    private CommonCallback<NetResult<String>> applyCallback = new CommonCallback<NetResult<String>>() {
        @Override
        public void onSuccess(NetResult<String> message) {
            btnApply.revertAnimation(new OnAnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    btnApply.setText("√");
                }
            });
            Toast.makeText(ScDetailActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(NetResult netResult) {
            btnApply.revertAnimation();
            Toast.makeText(ScDetailActivity.this, netResult.getMsg(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sc_activity_detail);
        ButterKnife.bind(this);

        //Intent
        item = getIntent().getParcelableExtra("activityId");
        tvDtlLabel.setText(Html.fromHtml(item.getLabel()));
        tvWeekTime.setText(item.getWeekTime());
        tvTime.setText(item.getTime());
        tvTitle.setText(item.getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(null);
            //getWindow().setSharedElementExitTransition(null);
            //getWindow().setSharedElementReturnTransition(null);
        }

        SCRequest.requestActivityDetail(this, item.getId(), commonCallback);
    }

    /**
     * Load activity detail!
     */
    private void showDetail(final boolean isSuccess){
        //Animation
        final Animation fadein = AnimationUtils.loadAnimation(this,R.anim.view_fade_in);

        //Show
        progressBar.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        bottomLayout.startAnimation(fadein);
    }

    /**
     * Apply this activity
     * @param btnApply btn
     */
    @OnClick(R.id.sc_dtl_btnApply)
    void apply(final CircularProgressButton btnApply){
        btnApply.startAnimation();
        SCRequest.applyActivity(this,item.getId(),applyCallback);

//        btnApply.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                btnApply.revertAnimation(new OnAnimationEndListener() {
//                    @Override
//                    public void onAnimationEnd() {
//                        btnApply.setText("√");
//                    }
//                });
//                Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_SHORT).show();
//            }
//        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnApply.dispose();
    }
}
