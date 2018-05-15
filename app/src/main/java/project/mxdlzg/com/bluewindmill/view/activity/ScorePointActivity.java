package project.mxdlzg.com.bluewindmill.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.TableRequest;
import project.mxdlzg.com.bluewindmill.util.Util;

/**
 * Created by mxdlzg on 18-3-18.
 */

public class ScorePointActivity extends AppCompatActivity {
    @BindView(R.id.score_point_toolbar)
    Toolbar scorePointToolbar;
    @BindView(R.id.score_point_term_start)
    NiceSpinner scorePointTermStart;
    @BindView(R.id.score_point_term_end)
    NiceSpinner scorePointTermEnd;
    @BindView(R.id.score_point_progressbar)
    ProgressBar scorePointProgressbar;
    @BindView(R.id.score_point_text)
    TextView scorePointText;

    private List<String> paramList = new ArrayList<>(Arrays.asList("2015-2016学年第1学期", "2015-2016学年第2学期", "2016-2017学年第1学期", "2016-2017学年第2学期", "2017-2018学年第1学期","2017-2018学年第2学期", "2018-2019学年第1学期","2018-2019学年第2学期"));
    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refresh(paramList.get(scorePointTermStart.getSelectedIndex()),paramList.get(scorePointTermEnd.getSelectedIndex()),"此区间平均绩点：");
            //refresh(scorePointTermStart.getText().toString(),scorePointTermEnd.getText().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_score_point);
        ButterKnife.bind(this);

        scorePointTermStart.attachDataSource(paramList);
        scorePointTermEnd.attachDataSource(paramList);
        scorePointTermStart.setOnItemSelectedListener(listener);
        scorePointTermEnd.setOnItemSelectedListener(listener);

        //
        refresh("","","当前总平均绩点为：");
    }

    private void refresh(String start,String end,final String description){
        String trueStart = Util.convertParam(start);
        String trueEnd = Util.convertParam(end);
        startRefresh();
        TableRequest.requestScorePoint(this, trueStart, trueEnd, new CommonCallback<NetResult<String>>() {
            @Override
            public void onSuccess(NetResult<String> message) {
                endRefresh(description+message.getData());
            }

            @Override
            public void onError(NetResult netResult) {
                endRefresh(description+netResult.getMsg());
            }
        });
    }

    private void startRefresh(){
        scorePointProgressbar.setVisibility(View.VISIBLE);
        scorePointText.setVisibility(View.INVISIBLE);
    }
    private void endRefresh(String text){
        scorePointProgressbar.setVisibility(View.INVISIBLE);
        scorePointText.setVisibility(View.VISIBLE);
        scorePointText.setText(text);
    }
}








