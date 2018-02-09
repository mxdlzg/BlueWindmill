package project.mxdlzg.com.bluewindmill.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import project.mxdlzg.com.bluewindmill.R;

/**
 * Created by mxdlzg on 18-2-9.
 */

public class ScDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sc_activity_detail);

        findViewById(R.id.sc_dtl_progressBar).postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.sc_dtl_progressBar).setVisibility(View.GONE);
                findViewById(R.id.relativeLayout2).setVisibility(View.VISIBLE);
            }
        },2000);
    }
}
