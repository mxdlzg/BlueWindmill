package android.mxdlzg.com.bluewindmill.view.activity;

import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.local.ManageExam;
import android.mxdlzg.com.bluewindmill.local.ManageSetting;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.mxdlzg.com.bluewindmill.net.request.TableRequest;
import android.mxdlzg.com.bluewindmill.util.wdUtil;
import android.mxdlzg.com.bluewindmill.view.adapter.ExamAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.mxdlzg.com.bluewindmill.util.Util.getIncrement;


/**
 * Created by 廷江 on 2017/4/29.
 */

public class ExamActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ExamAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private List<String> exams = new ArrayList<>();
    List<String> cacheList;

    //config
    private int currentWeek;
    private int currentDay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_exam);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.exam_toolbar);
        toolbar.setTitle(getResources().getString(R.string.titleExam));
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        //config
        initSetting();

        //swipe
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.exam_swipeLayout);
        swipeLayout.setColorSchemeResources(R.color.blue400,R.color.green400,R.color.orange400,R.color.red400);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取
                TableRequest.requestExam(ExamActivity.this, new CommonCallback<List<String>>() {
                    @Override
                    public void onSuccess(List<String> message) {
                        swipeLayout.setRefreshing(false);
                        ManageExam.cacheExam(ExamActivity.this,message);
                        updateAdapter(message);
                        Toast.makeText(ExamActivity.this, "获取完毕", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(List<String> message) {
                        swipeLayout.setRefreshing(false);
                        Toast.makeText(ExamActivity.this, "Get Exam Fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.exam_recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ExamAdapter(exams,this);
        adapter.setThisWeek(currentWeek);
        adapter.setThisDay(currentDay);
        recyclerView.setAdapter(adapter);
        updateAdapter(cacheList);
    }

    private void updateAdapter(List<String> exams){
        adapter.setExams(exams);
        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    public void initSetting(){
        currentWeek = ManageSetting.getIntSetting(this,"currentWeek")+getIncrement(ManageSetting.getLongSetting(this,"time"),System.currentTimeMillis());
        currentDay = wdUtil.getWeekInt();
        cacheList = ManageExam.getExamList(this);
        exams = cacheList;
        if (exams.isEmpty()){
            Toast.makeText(this, "未读取到考试文件", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "从文件读取考试信息", Toast.LENGTH_SHORT).show();
        }
    }
}
