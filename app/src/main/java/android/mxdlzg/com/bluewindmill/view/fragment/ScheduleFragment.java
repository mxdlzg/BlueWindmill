package android.mxdlzg.com.bluewindmill.view.fragment;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.Color;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.local.ManageClassOBJ;
import android.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import android.mxdlzg.com.bluewindmill.util.Util;
import android.mxdlzg.com.bluewindmill.view.activity.MainActivity;
import android.mxdlzg.com.bluewindmill.view.base.BaseFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.percent.PercentFrameLayout;
import android.support.percent.PercentLayoutHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import me.drakeet.materialdialog.MaterialDialog;

import static android.mxdlzg.com.bluewindmill.util.Util.getIncrement;
import static android.mxdlzg.com.bluewindmill.util.Util.getStatusBarSize;

/**
 * Created by mxdlzg on 18-2-6.
 */

public class ScheduleFragment extends BaseFragment {
    private PercentFrameLayout scheduleParentLayout;    //container


    private Long currentId;  //当前课程表的uuid
    private int currentWeek; //当前周,1开始
    private int currentDay;
    private List<ClassOBJ> classList;
    private int selectedColor;
    private Boolean scheduleColored;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initScheduleSetting(view);
        return view;
    }

    @Override
    protected void initData() {

    }

    private void initScheduleSetting(final View view) {
        //setting
        initScheduleSetting();

        //view
        fragmentContainer = (RelativeLayout) view.findViewById(R.id.schedule_container);
        final PercentFrameLayout percentFrameLayout = (PercentFrameLayout) view.findViewById(R.id.schedule_percent_layout);
        scheduleParentLayout = percentFrameLayout;
        percentFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                // 如果SDK大于4.4，布局percent中margintop+statusbar高度
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    ScrollView scrollView = (ScrollView) view.findViewById(R.id.schedule_scrollview);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
                    layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarSize(getContext()), layoutParams.rightMargin, layoutParams.bottomMargin);
                    scrollView.setLayoutParams(layoutParams);
                    scrollView.requestLayout();
                }
            }
        });

        View floatView = view.findViewById(R.id.schedule_float_view);
        PercentLayoutHelper.PercentLayoutInfo layoutInfo = ((PercentLayoutHelper.PercentLayoutParams)floatView.getLayoutParams()).getPercentLayoutInfo();
        layoutInfo.leftMarginPercent = 0.2f*(currentDay-1);


        //show
        if (currentId != 0) {
            classList = ManageClassOBJ.getClassList(getContext(), currentId);
            ((MainActivity)getActivity()).getNiceSpinner().setSelectedIndex(currentWeek);
            prepareScheduleTable(currentWeek);
        }
    }

    //从这里开始是schedule页面的函数
    /**
     * 初始化设置
     */
    public void initScheduleSetting() {
        currentDay = Util.today()-1;
        if (currentDay==0){
            currentDay = 7;
        }
        currentId = ManageSetting.getLongSetting(getContext(), "id");
        currentWeek = ManageSetting.getIntSetting(getContext(), "currentWeek") + getIncrement(ManageSetting.getLongSetting(getContext(), "time"), System.currentTimeMillis());
        scheduleColored = ManageSetting.getBoolSetting(getContext(),"scheduleColored");
    }

    /**
     * add view
     *
     * @param parentLayout root
     * @param content      detail
     * @param day          day,start from 1
     * @param startIndex   index,start from2
     * @param length       length,1-3
     * @return view
     */
    private View addClassItem(int index, PercentFrameLayout parentLayout, String content, int day, int startIndex, int length, int colorResource) {
        PercentFrameLayout testView = new PercentFrameLayout(getContext());

        try {
            testView.setBackgroundResource(colorResource);
        } catch (Resources.NotFoundException e) {
            testView.setBackgroundColor(colorResource);
        }

        parentLayout.addView(testView);
        PercentLayoutHelper.PercentLayoutParams testParams = (PercentLayoutHelper.PercentLayoutParams) testView.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo testInfo = testParams.getPercentLayoutInfo();
        testInfo.widthPercent = 0.2f;
        testInfo.heightPercent = 0.1f * length;
        testInfo.leftMarginPercent = 0.2f * (day - 1);
        testInfo.topMarginPercent = 0.101f * (startIndex - 1);

        TextView textView = new TextView(getContext());
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(content);
        testView.addView(textView);
        testView.requestLayout();

        testView.setTag(index);
        testView.setOnClickListener(mOnClickListener);

        return testView;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View classView) {
            //OBJ
            final ClassOBJ classOBJ = classList.get((Integer) classView.getTag());

            //color content text
            final View views = LayoutInflater.from(getContext()).inflate(R.layout.class_edit_content_view, null);
            //normal content text
            final LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.class_detial_content_view, null);

            //dialog
            final MaterialDialog materialDialog = new MaterialDialog(getContext()).setCanceledOnTouchOutside(true);
            materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDialog.dismiss();
                    if (scheduleColored){
                        Toast.makeText(getContext(), "渐变颜色模式下无法应用修改", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    classOBJ.setColor(selectedColor);
                    classView.setBackgroundColor(selectedColor);
                    ManageClassOBJ.cacheClassList(getContext(), currentId, classList);
                }
            })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialog.setView(linearLayout);
                            materialDialog.getNegativeButton().setVisibility(View.INVISIBLE);
                            materialDialog.getPositiveButton().setVisibility(View.INVISIBLE);
                        }
                    });


            //set content view text
            ((TextView) linearLayout.findViewById(R.id.class_name)).setText("名称：" + classOBJ.getName());
            ((TextView) linearLayout.findViewById(R.id.class_time)).setText("时间：周"+classOBJ.getDay()+" 第" + classOBJ.getIndex() + "-" + (classOBJ.getIndex() + classOBJ.getNum() - 1) + "节");
            ((TextView) linearLayout.findViewById(R.id.class_position)).setText("地点：" + classOBJ.getWeek(currentWeek));
            ((TextView) linearLayout.findViewById(R.id.class_other)).setText("其他：" + classOBJ.getALL());
            ((AppCompatButton) linearLayout.findViewById(R.id.class_edit_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //view
                    views.setVisibility(View.INVISIBLE);

                    //dialog
                    materialDialog.setView(views);
                    materialDialog.getPositiveButton().setVisibility(View.VISIBLE);
                    materialDialog.getNegativeButton().setVisibility(View.VISIBLE);

                    //show animation
                    views.post(new Runnable() {
                        @Override
                        public void run() {
                            colorDialogAnimation(views);
                            ColorPickerView colorPickerView = (ColorPickerView) views.findViewById(R.id.color_picker_view);
                            colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
                                @Override
                                public void onColorSelected(int color) {
                                    selectedColor = color;
                                }
                            });
                        }
                    });
                }
            });

            //show
            materialDialog.setView(linearLayout);
            materialDialog.show();
            materialDialog.getNegativeButton().setVisibility(View.GONE);
            materialDialog.getPositiveButton().setVisibility(View.GONE);
        }
    };

    /**
     * colorDialog 的circle动画
     *
     * @param views views
     */
    private void colorDialogAnimation(View views) {
        views.setVisibility(View.VISIBLE);
        View view = views.findViewById(R.id.color_layout);
        // get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        System.out.println(cx);
        // get the final radius for the clipping circle
        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(view, view.getLeft() + view.getWidth(), 0, 0, (float) Math.hypot(view.getWidth(), view.getHeight()));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    /**
     * 准备一页课程表
     *
     * @param week week
     */
    public void prepareScheduleTable(int week) {
        currentWeek = week;
        if (scheduleParentLayout == null) {
            scheduleParentLayout = (PercentFrameLayout) getView().findViewById(R.id.schedule_percent_layout);
            System.out.println("recreate root view");
        }

        scheduleParentLayout.removeViewsInLayout(1, scheduleParentLayout.getChildCount() - 1);

        for (ClassOBJ obj : classList) {
            if (week < obj.getWeeks().length) {
                if (obj.getWeek(week) != null && !obj.getWeek(week).equals("")) {
                    if (scheduleColored){
                        addClassItem(classList.indexOf(obj), scheduleParentLayout, obj.getName() + "@" + obj.getWeek(week), obj.getDay(), obj.getIndex(), obj.getNum(), gradualChangedColor(Color.BLUE,Math.abs(obj.getDay()-currentDay),obj.getIndex()) /*obj.getColor()*/);
                    }else {
                        addClassItem(classList.indexOf(obj), scheduleParentLayout, obj.getName() + "@" + obj.getWeek(week), obj.getDay(), obj.getIndex(), obj.getNum(), obj.getColor());
                    }
                }
            }
        }
    }

    /**
     * 提供给activity回调
     *
     * @param currentWeek week
     * @param currentID   目标学期uuid
     */
    public void prepareScheduleTable(int currentWeek, long currentID) {
        currentId = currentID;
        classList = ManageClassOBJ.getClassList(getContext(), currentID);
        prepareScheduleTable(currentWeek);
    }

    /**
     * 渐变颜色
     * @param centerColor 中心点颜色
     * @param horizontalDistance 水平距离
     * @param verticalDistance 垂直距离
     * @return color
     */
    private int gradualChangedColor(int centerColor,int horizontalDistance,int verticalDistance){
//        int red = (centerColor&0xff0000)>>16;
//        int green = (centerColor & 0x00ff00) >> 8;
//        int blue = (centerColor & 0x0000ff);
        int red = 0;
        int green = 100;
        int blue = 255;

        return Color.rgb(red,green+(20*horizontalDistance),blue);
    }

    /**
     * refresh
     */
    public void refreshSchedule() {
        prepareScheduleTable(currentWeek);
    }

    public void setScheduleColored(Boolean scheduleColored) {
        this.scheduleColored = scheduleColored;
    }
}
