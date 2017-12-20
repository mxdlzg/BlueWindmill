package android.mxdlzg.com.bluewindmill.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.mxdlzg.com.bluewindmill.net.request.TableRequest;
import android.mxdlzg.com.bluewindmill.view.adapter.AddScheduleAdapter;
import android.mxdlzg.com.bluewindmill.model.entity.config.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.config.TermOBJ;
import android.mxdlzg.com.bluewindmill.local.ManageClassOBJ;
import android.mxdlzg.com.bluewindmill.local.ManageSchedule;
import android.mxdlzg.com.bluewindmill.net.GetScheduleTest;
import android.mxdlzg.com.bluewindmill.model.process.PrepareSchedule;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/3/26.
 */

public class AddScheduleActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AppCompatButton btn_newTerm;
    private NumberPicker yearPicker,termPicker;
    private View newTermView;
    private AddScheduleAdapter addScheduleAdapter;

    private String[] termTime = {"2016220171","2016220172","2017220181","2017220182"};
    private List<TermOBJ> list = new ArrayList<>();

    private Handler handler_notifiy_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schedule_add);

        //handler
        handler_notifiy_add = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                addScheduleAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                return false;
            }
        });

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.schedule_add_toolbar);
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setTitle("课程表");
        setSupportActionBar(toolbar);

        //获取数据
        ManageSchedule.getAll(this,list);


        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.schedule_add_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        addScheduleAdapter = new AddScheduleAdapter(this,list);
        recyclerView.setAdapter(addScheduleAdapter);

        //btn
        btn_newTerm = (AppCompatButton) findViewById(R.id.schedule_add_btn_newTerm);
        btn_newTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newTermView
                newTermView = LayoutInflater.from(AddScheduleActivity.this).inflate(R.layout.layout_schedule_new_term,null);
                //picker
                yearPicker = (NumberPicker) newTermView.findViewById(R.id.schedule_add_year_picker);
                String[] years = {"2016秋","2017春","2017秋","2018春"};
                yearPicker.setDisplayedValues(years);
                yearPicker.setMaxValue(years.length-1);
                yearPicker.setMinValue(0);
                yearPicker.setValue(0);
                yearPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

                termPicker = (NumberPicker) newTermView.findViewById(R.id.schedule_add_term_picker);
                String[] terms = {"2016-2017第1学期","2016-2017第2学期","2017-2018第1学期","2017-2018第2学期"};
                termPicker.setDisplayedValues(terms);
                termPicker.setMaxValue(terms.length-1);
                termPicker.setMinValue(0);
                termPicker.setValue(0);
                termPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                //dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AddScheduleActivity.this);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AddScheduleActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/3/27 通知getSchedule去获取，cookie使用系统内存中的，如果获取完毕就启动分析函数，分析完毕
                        // TODO: 2017/3/27 写入schedulelist文件，和一个新的schedule+uuid的文件，以后启动就获取新文件
                        final ProgressDialog getScheduleDialog = ProgressDialog.show(AddScheduleActivity.this,"获取中","正在获取schedule",true,false);

                        String year1 = yearPicker.getDisplayedValues()[yearPicker.getValue()];
                        String year2 = termPicker.getDisplayedValues()[termPicker.getValue()];
                        TableRequest.requestSchedule(AddScheduleActivity.this, termTime[termPicker.getValue()], year1, "1", year2, new CommonCallback<List<ClassOBJ>>() {
                            @Override
                            public void onFail(List<ClassOBJ> message) {
                                getScheduleDialog.dismiss();
                                Toast.makeText(AddScheduleActivity.this, R.string.requestScheduleFail, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(String status) {
                                System.out.println(status+Thread.currentThread().getId());
                                getScheduleDialog.setMessage(status);
                            }

                            @Override
                            public void onSuccess(List<ClassOBJ> message) {
                                System.out.println("OnSuccess"+Thread.currentThread().getId());
                                //分析
                                getScheduleDialog.setMessage("正在存储Class信息");
                                ManageClassOBJ.cacheClassList(AddScheduleActivity.this,Long.valueOf(termTime[termPicker.getValue()]),message);

                                //更新ui
                                String name = termPicker.getDisplayedValues()[termPicker.getValue()];
                                int start = Integer.valueOf(name.substring(0,4));
                                int end = Integer.valueOf(name.substring(5,9));
                                int num = Integer.valueOf(name.substring(10,11));
                                Long id = Long.valueOf((start + "2" + end + num));
                                TermOBJ termOBJ = new TermOBJ(name,start,end,num,id);
                                list.add(termOBJ);

                                // TODO: 2017/3/28 cache Schedule信息到文件
                                getScheduleDialog.setMessage("正在存储Schedule信息");
                                ManageSchedule.addSchedule(AddScheduleActivity.this,termOBJ);
                                getScheduleDialog.dismiss();

                                // TODO: 2017/3/28 handler更新ui
                                handler_notifiy_add.sendMessage(new Message());
                            }
                        });
                    }
                });
                builder.setView(newTermView);
                builder.setCancelable(true);
                builder.show();

            }
        });





    }
}
