package project.mxdlzg.com.bluewindmill.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import project.mxdlzg.com.bluewindmill.view.base.BaseActivity;

/**
 * Created by mxdlzg on 18-3-16.
 */

public class ScoreActivity extends BaseActivity {
    @BindView(R.id.score_toolbar)
    Toolbar scoreToolbar;
    @BindView(R.id.score_term_spinner)
    NiceSpinner scoreTermSpinner;
    @BindView(R.id.score_recyclerView)
    RecyclerView scoreRecyclerView;

    private int currentPosition;
    private Unbinder unbinder;
    private ScoreRcyAdapter scoreRcyAdapter;
    private ScoreOBJ currentObj;
    private List<ScoreOBJ> list = new ArrayList<>();
    private List<String> paramList = new ArrayList<>(Arrays.asList("2015-2016学年第1学期", "2015-2016学年第2学期", "2016-2017学年第1学期", "2016-2017学年第2学期", "2017-2018学年第1学期","2017-2018学年第2学期", "2018-2019学年第1学期","2018-2019学年第2学期"));
    private AlertDialog.Builder alertDialogBuilder;


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

    //Rcy Item Click Listener
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (adapter.getItemViewType(position) == ScoreOBJ.NOT_EVALUATED){
                currentObj = (ScoreOBJ) adapter.getData().get(position);
                alertDialogBuilder.setMessage("是否对课程："+currentObj.getName()+"进行评教？");
                alertDialogBuilder.show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_score);
        unbinder = ButterKnife.bind(this);

        initView();
    }

    /**
     * Init this activity
     */
    private void initView() {
        scoreToolbar.setTitle("本学期成绩详情");
//        scoreToolbar.setTitleTextColor(Color.WHITE);
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
        scoreRcyAdapter.setOnItemClickListener(onItemClickListener);
        scoreRecyclerView.setAdapter(scoreRcyAdapter);
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Dialog
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("课程评教");
        alertDialogBuilder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final ProgressDialog progressDialog = ProgressDialog.show(alertDialogBuilder.getContext(),"评教","正在评教...",true,false);
                TableRequest.evaluateLesson(alertDialogBuilder.getContext(), Util.convertParam(paramList.get(currentPosition)), currentObj, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String message) {
                        progressDialog.dismiss();
                        Toast.makeText(ScoreActivity.this, message, Toast.LENGTH_SHORT).show();
                        refresh(currentPosition);
                    }

                    @Override
                    public void onError(String message, Context context) {
                        progressDialog.dismiss();
                        super.onError(message, alertDialogBuilder.getContext());
                    }
                });
            }
        });
    }

    private void refresh(int position){
        currentPosition = position;
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
