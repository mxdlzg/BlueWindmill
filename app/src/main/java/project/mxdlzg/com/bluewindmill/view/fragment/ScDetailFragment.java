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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class ScDetailFragment extends BaseFragment {
    @BindView(R.id.second_child_swipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.second_child_recyclerView)
    RecyclerView recyclerView;

    private List<SCActivityDetail> list;
    private ScRcyAdapter adapter;

    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.loadMoreFail();
                    Toast.makeText(getContext(), "Load more!!!", Toast.LENGTH_SHORT).show();
                }
            },500);
        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View
        View view = inflater.inflate(R.layout.fragment_second_child,container,false);

        //Bind
        unbinder = ButterKnife.bind(this,view);

        list = new LinkedList<>();
        list.add(new SCActivityDetail("111","【天天讲】【生态天天讲】野趣魔都——野生动植物保护二三事","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));

        //Adapter
        adapter = new ScRcyAdapter(list,getContext());
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(requestLoadMoreListener,recyclerView);
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getContext().startActivity(new Intent(getContext(), ScDetailActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(getActivity(),view,getString(R.string.sc_rcy_item_2_scDtl)).toBundle());
                }else {
                    getContext().startActivity(new Intent(getContext(), ScDetailActivity.class));
                }
            }
        });

        //Return
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
