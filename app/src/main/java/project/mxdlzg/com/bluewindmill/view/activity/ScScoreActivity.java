package project.mxdlzg.com.bluewindmill.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.santalu.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import project.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.SCRequest;
import project.mxdlzg.com.bluewindmill.view.adapter.ScRcyAdapter;
import project.mxdlzg.com.bluewindmill.view.adapter.ScScoreRcyAdapter;

/**
 * Created by mxdlzg on 18-3-18.
 */

public class ScScoreActivity extends AppCompatActivity {
    @BindView(R.id.sc_score_toolbar)
    Toolbar scScoreToolbar;
    @BindView(R.id.sc_score_text)
    TextView scScoreText;
    @BindView(R.id.sc_score_text_detail)
    TextView scScoreTextDetail;
    @BindView(R.id.sc_score_recyclerView)
    RecyclerView recyclerView;

    private int pageNo = 1;
    final int pageSize = 20;
    private EmptyView emptyView;
    private ScScoreRcyAdapter adapter;           //Data Adapter
    private List<SCScoreDetail> list = new ArrayList<>();

    //REFRESH-1 empty refresh listener
    private View.OnClickListener emptyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emptyView.showLoading();
            refreshData();
        }
    };


    /*footer loadmore listener*/
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            loadMoreData();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sc_score);
        ButterKnife.bind(this);

        //View
        init();

        //Show total score
        showTotalScore();
    }

    private void showTotalScore() {
        SCRequest.requestScInfo(this, new CommonCallback<SCInfo>() {
            @Override
            public void onSuccess(SCInfo message) {
                if (message!=null){
                    scScoreText.setText("第二课堂学分："+String.valueOf(message.getScScore(0)));
                    scScoreTextDetail.setText(message.getscPresentationText());
                }
            }

            @Override
            public void onError(NetResult netResult) {
                super.onError(netResult);
            }
        });
    }

    private void init() {
        scScoreToolbar.setTitle("第二课堂成绩详情");
//        scoreToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(scScoreToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //EmptyView
        emptyView = (EmptyView) this.getLayoutInflater().inflate(R.layout.rcy_empty_view,null,false);
        emptyView.setOnClickListener(emptyOnClickListener);
        emptyView.showEmpty("点击查询详情");

        //Adapter & Recyclerview
        adapter = new ScScoreRcyAdapter(R.layout.sc_score_rcy_item,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Adapter
        adapter.setOnLoadMoreListener(requestLoadMoreListener,recyclerView);
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
        adapter.setEmptyView(emptyView);
    }

    /**
     * Do request data in this method
     */
    private void refreshData(){
        //Net request
        SCRequest.requestSCScoreDetail(this, 1, pageSize, new CommonCallback<NetResult<List<SCScoreDetail>>>() {
            @Override
            public void onSuccess(NetResult<List<SCScoreDetail>> message) {
                finishRefresh(message.getData());
            }

            @Override
            public void onError(NetResult netResult) {
                finishRefresh(null);
            }
        });
    }

    /**
     * Load more activity list data
     * <Note>上拉自动加载触发-》pageSize为每页显示的数量，超过该数量才会调用加载更多的回调。</Note>
     */
    private void loadMoreData(){
        SCRequest.requestSCScoreDetail(this, pageNo+1, pageSize, new CommonCallback<NetResult<List<SCScoreDetail>>>() {
            @Override
            public void onSuccess(NetResult<List<SCScoreDetail>> message) {
                pageNo++;
                if (list != null && message.getData()!=null){
                    if (message.getData().size()!=0){
                        list.addAll(message.getData());
                        adapter.loadMoreComplete();
                    }else {
                        adapter.loadMoreEnd();
                    }
                }else {
                    adapter.loadMoreEnd();
                }
            }

            @Override
            public void onError(NetResult netResult) {
                adapter.loadMoreFail();
            }
        });
    }

    /**
     * finish Refresh
     * @param list newList OR append Data
     */
    private void finishRefresh(List<SCScoreDetail> list){
        adapter.setNewData(list);
        this.list = list;
        pageNo = 1;
        if (list == null){
            emptyView.showEmpty();
        }
    }
}
