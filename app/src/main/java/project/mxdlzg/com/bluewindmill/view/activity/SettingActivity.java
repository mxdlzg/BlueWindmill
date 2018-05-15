package project.mxdlzg.com.bluewindmill.view.activity;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import project.mxdlzg.com.bluewindmill.util.Util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by 廷江 on 2017/4/4.
 */

public class SettingActivity extends AppCompatActivity {
    private NiceSpinner niceSpinner;
    private Toolbar toolbar;

    private int currentWeek;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        //读取设置
        initData();

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        //spinner
        niceSpinner = (NiceSpinner) findViewById(R.id.setting_weeks_spinner);
        niceSpinner.attachDataSource(new LinkedList<>(Arrays.asList("第1周", "第2周", "第3周", "第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周", "第13周", "第14周", "第15周", "第16周", "第17周", "第18周", "第19周", "第20周")));
        niceSpinner.setSelectedIndex(currentWeek);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendar = Calendar.getInstance();
//                System.out.println(calendar.getTimeInMillis());
//                int day = calendar.get(Calendar.DAY_OF_WEEK);
                calendar.set(Calendar.DAY_OF_WEEK,2);
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                ManageSetting.addIntSetting(SettingActivity.this,"currentWeek",position);
                ManageSetting.addLongSetting(SettingActivity.this,"time",calendar.getTimeInMillis());
//                System.out.println(calendar.getTime());
//                System.out.println(calendar.getTimeZone());
//                System.out.println(calendar.getTimeInMillis());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData(){
        currentWeek = ManageSetting.getIntSetting(this, "currentWeek") + Util.getIncrement(ManageSetting.getLongSetting(this, "time"), System.currentTimeMillis());
    }
}
