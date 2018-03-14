package project.mxdlzg.com.bluewindmill.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.UserRecyclerItem;
import project.mxdlzg.com.bluewindmill.view.activity.LoginActivity;
import project.mxdlzg.com.bluewindmill.view.activity.SettingActivity;
import project.mxdlzg.com.bluewindmill.view.adapter.UserRcyAdapter;
import project.mxdlzg.com.bluewindmill.view.base.BaseFragment;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class UserFragment extends BaseFragment {
    @BindView(R.id.user_recyclerView)
    RecyclerView userRecyclerView;
    @BindView(R.id.user_container)
    RelativeLayout userContainer;

    private ImageView userImageView;
    private RelativeLayout headerView;
    private Unbinder unbinder;
    private UserRcyAdapter adapter;
    private List<UserRecyclerItem> list = new ArrayList<>();
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (view instanceof LinearLayout){
                Intent intent = (Intent) view.getTag();
                if (intent != null)
                getContext().startActivity(intent);
                else
                    Toast.makeText(getContext(), "该功能正在建设...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        headerView = (RelativeLayout) inflater.inflate(R.layout.user_header,container,false);

        //Bind
        unbinder = ButterKnife.bind(this, view);

        initUserSetting(view);
        return view;
    }

    @Override
    protected void initData() {
        //Data
        list.add(new UserRecyclerItem("成绩","课程成绩，第二课堂成绩",R.drawable.arrow,null));
        list.add(new UserRecyclerItem("设置","详细设置",R.drawable.ic_settings_black_24dp,new Intent(getContext(), SettingActivity.class)));

        //Adapter
        adapter = new UserRcyAdapter(list);
        adapter.addHeaderView(headerView);
        adapter.setOnItemClickListener(onItemClickListener);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initUserSetting(View view) {
        fragmentContainer = userContainer;
        userImageView = headerView.findViewById(R.id.user_image);
//        final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.test_toggleButton2);
//        toggleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (toggleButton.isChecked()) {
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.updateBottomNavigationColor(true);
////                    activity.getBottomNavigation().hideBottomNavigation();
//                } else {
//                    MainActivity activity = (MainActivity) getActivity();
//                    activity.updateBottomNavigationColor(false);
//                }
//            }
//        });
//        ImageView userImage = (ImageView) view.findViewById(R.id.user_image);
        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
//        Button btnExam = (Button) view.findViewById(R.id.user_btn_exam);
//        btnExam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ExamActivity.class));
//            }
//        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
