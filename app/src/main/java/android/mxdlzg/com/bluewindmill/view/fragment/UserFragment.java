package android.mxdlzg.com.bluewindmill.view.fragment;

import android.content.Intent;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.activity.ExamActivity;
import android.mxdlzg.com.bluewindmill.view.activity.LoginActivity;
import android.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import android.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

/**
 * Created by mxdlzg on 18-2-7.
 */

public class UserFragment extends BaseFragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initUserSetting(view);
        return view;
    }

    @Override
    protected void initData() {

    }

    private void initUserSetting(View view) {
        fragmentContainer = (RelativeLayout) view.findViewById(R.id.user_container);
        final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.test_toggleButton2);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.updateBottomNavigationColor(true);
//                    activity.getBottomNavigation().hideBottomNavigation();
                } else {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.updateBottomNavigationColor(false);
                }
            }
        });
        ImageView userImage = (ImageView) view.findViewById(R.id.user_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        Button btnExam = (Button) view.findViewById(R.id.user_btn_exam);
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ExamActivity.class));
            }
        });
    }

}
