package project.mxdlzg.com.bluewindmill.view.fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.view.adapter.ScRcyAdapter;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View
        View view = inflater.inflate(R.layout.fragment_second_child,container,false);

        //Bind
        unbinder = ButterKnife.bind(this,view);

        list = new LinkedList<>();
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));
        list.add(new SCActivityDetail("111","title","2018-02-08 13:44:57"));

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new ScRcyAdapter(list));

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
