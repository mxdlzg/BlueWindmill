package project.mxdlzg.com.bluewindmill.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.TableRequest;
import project.mxdlzg.com.bluewindmill.util.Util;
import project.mxdlzg.com.bluewindmill.view.adapter.AddScheduleAdapter;
import project.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import project.mxdlzg.com.bluewindmill.model.entity.TermOBJ;
import project.mxdlzg.com.bluewindmill.model.local.ManageClassOBJ;
import project.mxdlzg.com.bluewindmill.model.local.ManageSchedule;
import project.mxdlzg.com.bluewindmill.view.base.BaseActivity;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/3/26.
 */

public class AddScheduleActivity extends BaseActivity {
    @BindView(R.id.schedule_add_toolbar)
    Toolbar toolbar;
    @BindView(R.id.schedule_add_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.schedule_add_btn_newTerm)
    AppCompatButton btn_newTerm;
    //    @BindView(R.id.schedule_add_term_picker)
    private NumberPicker termPicker;

    private View newTermView;
    private String[] termTime = {"2016220171", "2016220172", "2017220181", "2017220182"};
    private List<TermOBJ> list = new ArrayList<>();
    private AddScheduleAdapter addScheduleAdapter;

    private Handler handler_notifiy_add;
    private Unbinder unbinder = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schedule_add);

        //ButterKnife
        unbinder = ButterKnife.bind(this);

        //View
        initView();

        //handler
        handler_notifiy_add = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                addScheduleAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
                return false;
            }
        });
    }

    private void initView() {
        //toolbar
        toolbar.setTitle("课程表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //获取数据
        ManageSchedule.getAll(this, list);


        //recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        addScheduleAdapter = new AddScheduleAdapter(this, list);
        recyclerView.setAdapter(addScheduleAdapter);

        //btn
        btn_newTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newTermView
                newTermView = LayoutInflater.from(AddScheduleActivity.this).inflate(R.layout.layout_schedule_new_term, null);
                //picker
                termPicker = (NumberPicker) newTermView.findViewById(R.id.schedule_add_term_picker);
                String[] terms = {"2016-2017第1学期", "2016-2017第2学期", "2017-2018第1学期", "2017-2018第2学期"};
                termPicker.setDisplayedValues(terms);
                termPicker.setMaxValue(terms.length - 1);
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
                        final ProgressDialog getScheduleDialog = ProgressDialog.show(AddScheduleActivity.this, "获取中", "正在获取schedule", true, false);

                        String year2 = termPicker.getDisplayedValues()[termPicker.getValue()];
                        String year1 = Util.convertParam(year2);

                        TableRequest.requestSchedule(AddScheduleActivity.this, year1, "1", year2, new CommonCallback<List<ClassOBJ>>() {
                            @Override
                            public void onFail(List<ClassOBJ> message) {
                                getScheduleDialog.dismiss();
                                Toast.makeText(AddScheduleActivity.this, R.string.requestScheduleFail, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(String status) {
                                System.out.println(status + Thread.currentThread().getId());
                                getScheduleDialog.setMessage(status);
                            }

                            @Override
                            public void onSuccess(List<ClassOBJ> message) {
                                System.out.println("OnSuccess" + Thread.currentThread().getId());
                                //分析
                                getScheduleDialog.setMessage("正在存储Class信息");
                                ManageClassOBJ.cacheClassList(AddScheduleActivity.this, Long.valueOf(termTime[termPicker.getValue()]), message);

                                //更新ui
                                String name = termPicker.getDisplayedValues()[termPicker.getValue()];
                                int start = Integer.valueOf(name.substring(0, 4));
                                int end = Integer.valueOf(name.substring(5, 9));
                                int num = Integer.valueOf(name.substring(10, 11));
                                Long id = Long.valueOf((start + "2" + end + num));
                                TermOBJ termOBJ = new TermOBJ(name, start, end, num, id);
                                list.add(termOBJ);

                                // TODO: 2017/3/28 cache Schedule信息到文件
                                getScheduleDialog.setMessage("正在存储Schedule信息");
                                ManageSchedule.addSchedule(AddScheduleActivity.this, termOBJ);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
