package project.mxdlzg.com.bluewindmill.view.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.SCRequest;
import project.mxdlzg.com.bluewindmill.util.Util;
import project.mxdlzg.com.bluewindmill.view.activity.ScDetailActivity;
import project.mxdlzg.com.bluewindmill.view.adapter.ScRcyAdapter;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.santalu.emptyview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class ScDetailFragment extends BaseFragment {
    @BindView(R.id.second_child_swipeLayout)
    SmartRefreshLayout smartRefreshLayout;  //header refreshLayout
    @BindView(R.id.second_child_recyclerView)
    RecyclerView recyclerView;              //rcyView

    private List<SCActivityDetail> list;    //Data List
    private ScRcyAdapter adapter;           //Data Adapter
    private EmptyView emptyView;            //rcyEmptyView

    private int pageNo = 1;
    final int pageSize = 20;
    private String categoryID = "001";
    private boolean isWaitingSearchResult = false;
    private boolean canLoadMore = true;
    private String[] ids = new String[]{"001",
            "8ab17f543fe62d5d013fe62e6dc70001",
            "ff8080814e241104014eb867e1481dc3",
            "402881de5d62ba57015d6320f1a7000c",
            "8ab17f2a3fe6585e013fe6596c300001",
            "8ab17f543fe626a8013fe6278a880001",
            "8ab17f543fe62d5d013fe62efd3a0002",
            "8ab17f533ff05c27013ff06d10bf0001",
            "ff8080814e241104014fedbbf7fd329d"};

    /*footer loadmore listener*/
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            loadMoreData();
        }
    };

    /*rcy item click listener*/
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            //Intent
            Intent intent = new Intent(getContext(),ScDetailActivity.class);
            intent.putExtra("activityId",(SCActivityDetail)view.findViewById(R.id.sc_child_rcy_headerView).getTag());

            //Start Activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Animation
                getContext().startActivity((intent),
                        ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,getString(R.string.sc_rcy_item_2_scDtl)).toBundle());
            }else {
                //No animation
                getContext().startActivity(intent);
            }
        }
    };

    /*REFRESH-1 header refresh listener*/
    private OnRefreshListener OnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(final RefreshLayout refreshLayout) {
            refreshData(refreshLayout);
        }
    };

    //REFRESH-2 empty refresh listener
    private View.OnClickListener emptyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emptyView.showLoading();
            refreshData(null);
        }
    };

    //REFRESH CommonCallback
    private CommonCallback<List<SCActivityDetail>> commonCallback = new CommonCallback<List<SCActivityDetail>>() {
        @Override
        public void onSuccess(List<SCActivityDetail> message) {

        }
    };

    /**
     * Do request data in this method
     * @param refreshLayout refreshLayout
     */
    private void refreshData(final RefreshLayout refreshLayout){
        //Net request
        SCRequest.requestActivityList(getContext(), 1, pageSize, categoryID, new CommonCallback<NetResult<List<SCActivityDetail>>>() {
            @Override
            public void onSuccess(NetResult<List<SCActivityDetail>> message) {
                finishRefresh(message.getData(),refreshLayout);
            }

            @Override
            public void onError(NetResult netResult) {
                finishRefresh(null,refreshLayout);
            }
        });
    }

    /**
     * Load more activity list data
     * <Note>上拉自动加载触发-》pageSize为每页显示的数量，超过该数量才会调用加载更多的回调。</Note>
     */
    private void loadMoreData(){
        if (!canLoadMore){
            adapter.loadMoreEnd();
            return;
        }
        SCRequest.requestActivityList(getContext(), pageNo+1, pageSize, categoryID, new CommonCallback<NetResult<List<SCActivityDetail>>>() {
            @Override
            public void onSuccess(NetResult<List<SCActivityDetail>> message) {
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
     * @param refreshLayout header refreshlayout
     */
    private void finishRefresh(List<SCActivityDetail> list,RefreshLayout refreshLayout){
        adapter.setNewData(list);
        recyclerView.smoothScrollToPosition(0);
        this.list = list;
        pageNo = 1;
        canLoadMore = true;
        if (refreshLayout != null){
            refreshLayout.finishRefresh(true);
        }else if (list == null){
            emptyView.showEmpty();
        }
    }

    public void finishSearch(List<SCActivityDetail> list){
        adapter.setNewData(list);
        this.list = list;
        isWaitingSearchResult = false;
        canLoadMore = false;
        if (list == null){
            emptyView.showEmpty("未找到搜索结果");
        }
    }


    public void startSearch() {
        emptyView.showLoading();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View
        View view = inflater.inflate(R.layout.fragment_second_child,container,false);
        emptyView = (EmptyView) inflater.inflate(R.layout.rcy_empty_view,container,false);

        //Bind
        unbinder = ButterKnife.bind(this,view);

        //View
        if (Util.getNavigationBarSize(getContext()).equals(0,0)){
            System.out.println("Set Padding--------------->>>>>>>>>>>>>>>>>>>>>>");
            recyclerView.setPadding(0,0,0,0);
        }

        //Return
        return view;
    }

    @Override
    protected void initData() {
        //EmptyView
        emptyView.setOnClickListener(emptyOnClickListener);
        emptyView.showEmpty();
        if (!isWaitingSearchResult){
            emptyView.findViewById(R.id.empty_button).performClick();
        }
        Log.e("dtlFragment","emptyView");

        //Adapter & Recyclerview
        adapter = new ScRcyAdapter(null,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        //Adapter
        adapter.setOnLoadMoreListener(requestLoadMoreListener,recyclerView);
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
        adapter.setEmptyView(emptyView);
        adapter.setOnItemClickListener(onItemClickListener);

        //Smart Swipe
        smartRefreshLayout.setOnRefreshListener(OnRefreshListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setWaitingSearchResult(boolean waitingSearchResult) {
        isWaitingSearchResult = waitingSearchResult;
    }

    /**
     * Choose another kind of Sc activities
     * @param title key words
     * @param index index
     */
    public void kindRefresh(String title, int index) {
        categoryID = ids[index];
        if (smartRefreshLayout!=null){
            smartRefreshLayout.autoRefresh();
        }
    }

}
