package project.mxdlzg.com.bluewindmill.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.TableRequest;
import project.mxdlzg.com.bluewindmill.util.Util;
import project.mxdlzg.com.bluewindmill.view.adapter.ScoreRcyAdapter;

/**
 * Created by mxdlzg on 18-3-16.
 */

public class ScoreActivity extends AppCompatActivity {
    @BindView(R.id.score_toolbar)
    Toolbar scoreToolbar;
    @BindView(R.id.score_term_spinner)
    NiceSpinner scoreTermSpinner;
    @BindView(R.id.score_recyclerView)
    RecyclerView scoreRecyclerView;

    private Unbinder unbinder;
    private ScoreRcyAdapter scoreRcyAdapter;
    private List<ScoreOBJ> list = new ArrayList<>();
    private List<String> paramList = new ArrayList<>(Arrays.asList("2015-2016学年第1学期", "2015-2016学年第2学期", "2016-2017学年第1学期", "2016-2017学年第2学期", "2017-2018学年第1学期","2017-2018学年第2学期", "2018-2019学年第1学期","2018-2019学年第2学期"));

    //ClickListener
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refresh(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_score);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Init this activity
     */
    private void initView() {
        scoreToolbar.setTitle("本学期成绩详情");
        scoreToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(scoreToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Spinner
        scoreTermSpinner.attachDataSource(paramList);
        scoreTermSpinner.setOnItemSelectedListener(onItemSelectedListener);
        scoreTermSpinner.setSelectedIndex(5);
        refresh(5);

        //RecyclerView
        scoreRcyAdapter = new ScoreRcyAdapter(list);
        scoreRcyAdapter.openLoadAnimation();
        scoreRecyclerView.setAdapter(scoreRcyAdapter);
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void refresh(int position){
        String trueParam = Util.convertParam(paramList.get(position));
        TableRequest.requestScore(this, trueParam, new CommonCallback<NetResult<List<ScoreOBJ>>>() {
            @Override
            public void onSuccess(NetResult<List<ScoreOBJ>> message) {
                list.clear();
                list = message.getData();
                scoreRcyAdapter.setNewData(list);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
