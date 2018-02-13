package project.mxdlzg.com.bluewindmill.view.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
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

    /*footer loadmore listener*/
    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
                    list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
                    list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
                    list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
                    adapter.loadMoreComplete();
                    Toast.makeText(getContext(), "Load more!!!", Toast.LENGTH_SHORT).show();
                }
            },500);
        }
    };

    /*rcy item click listener*/
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getContext().startActivity(new Intent(getContext(), ScDetailActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,getString(R.string.sc_rcy_item_2_scDtl)).toBundle());
            }else {
                getContext().startActivity(new Intent(getContext(), ScDetailActivity.class));
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
            refreshData(null);
        }
    };

    /**
     * Do request data in this method
     * @param refreshLayout refreshLayout
     */
    private void refreshData(RefreshLayout refreshLayout){
        //request data
        if (list == null){list = new LinkedList<>();}
        list.add(new SCActivityDetail("111","【天天讲】【生态天天讲】野趣魔都——野生动植物保护二三事","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));

        //commit change
        finishRefresh(list,refreshLayout);
    }

    /**
     * finish Refresh
     * @param list newList OR append Data
     * @param refreshLayout header refreshlayout
     */
    private void finishRefresh(List<SCActivityDetail> list,RefreshLayout refreshLayout){
        adapter.setNewData(list);
        if (refreshLayout != null){
            refreshLayout.finishRefresh(true);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View
        View view = inflater.inflate(R.layout.fragment_second_child,container,false);
        emptyView = (EmptyView) inflater.inflate(R.layout.rcy_empty_view,container,false);

        //Bind
        unbinder = ButterKnife.bind(this,view);

        //Return
        return view;
    }

    @Override
    protected void initData() {
        //Data List

        //EmptyView
        emptyView.setOnClickListener(emptyOnClickListener);
        emptyView.showEmpty("点击重试或下拉刷新！");
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

}
