package android.mxdlzg.com.bluewindmill.view;

import android.content.Intent;
import android.graphics.Color;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.view.adapter.MainViewPagerAdapter;
import android.mxdlzg.com.bluewindmill.model.entity.config.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.config.TermOBJ;
import android.mxdlzg.com.bluewindmill.view.fragment.MainFragment;
import android.mxdlzg.com.bluewindmill.local.ManageCookie;
import android.mxdlzg.com.bluewindmill.local.ManageSetting;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import org.angmarch.views.NiceSpinner;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 廷江 on 2017/9/28.
 */

public class MainActivity extends AppCompatActivity {
    public Toolbar getToolbar() {
        return toolbar;
    }

    private Toolbar toolbar;
    private AHBottomNavigation bottomNavigation;

    public AHBottomNavigationViewPager getViewPager() {
        return viewPager;
    }

    private AHBottomNavigationViewPager viewPager;

    public MainViewPagerAdapter getNavigationAdapter() {
        return navigationAdapter;
    }



    private MainViewPagerAdapter navigationAdapter;

    public NiceSpinner getNiceSpinner() {
        return niceSpinner;
    }

    private NiceSpinner niceSpinner;

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public void setMainFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    private MainFragment mainFragment;

    //Parameter
    private Long currentId; //当前课程表的uuid
    private int currentWeek; //当前周,1开始

    private List<ClassOBJ> classList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //theme
        setTheme(R.style.AppTheme);

        //create
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        DateFormat format = new SimpleDateFormat("MM");
        toolbar.setTitle(format.format(System.currentTimeMillis())+"月");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more_horiz_black_24dp));

        //init widgets
        initBottomNavigation();
        initViewPager();
        initNiceSpinner();
    }

    private void initNiceSpinner() {
        //niceSpinner设置
        niceSpinner = (NiceSpinner) findViewById(R.id.schedule_niceSpinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("第1周", "第2周", "第3周", "第4周", "第5周", "第6周", "第7周", "第8周", "第9周", "第10周", "第11周", "第12周", "第13周", "第14周", "第15周", "第16周", "第17周", "第18周", "第19周", "第20周"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setTextColor(getColor(R.color.white));
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainFragment == null){
                    mainFragment = navigationAdapter.getCurrentFragment();
                }
                mainFragment.prepareScheduleTable(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        menu.getItem(1).setChecked(ManageSetting.getBoolSetting(this,"scheduleColored"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_more:
                startActivityForResult(new Intent(this,AddScheduleActivity.class), Config.START_ADD_SCHEDULE);
                break;
            case R.id.menu_colored:
                item.setChecked(!item.isChecked());
                ManageSetting.addBoolSetting(this,"scheduleColored",item.isChecked());
                if (mainFragment == null){
                    mainFragment = navigationAdapter.getCurrentFragment();
                }
                mainFragment.setScheduleColored(item.isChecked());
                mainFragment.refreshSchedule();
                break;
            case  R.id.menu_setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Config.ADD_SCHEDULE_OK:
                TermOBJ obj = (TermOBJ) data.getSerializableExtra("termOBJ");
                System.out.println(obj.getName()+","+obj.getId());
                if (mainFragment == null){
                    mainFragment = navigationAdapter.getCurrentFragment();
                }
                mainFragment.prepareScheduleTable(currentWeek,obj.getId());
                ManageSetting.addLongSetting(this,"id",obj.getId());
                niceSpinner.setSelectedIndex(currentWeek);
                break;
            default:
                break;
        }
    }

    private void initViewPager() {
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(4);
        navigationAdapter = new MainViewPagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(navigationAdapter);
        mainFragment = navigationAdapter.getCurrentFragment();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNavigation(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.main_bottomview);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("item1",R.drawable.ic_local_library_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("item2",R.drawable.ic_toys_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("item3",R.drawable.ic_person_black_24dp);

        item1.setColor(getColor(R.color.blue900));
        item2.setColor(getColor(R.color.green900));
        item3.setColor(getColor(R.color.brown900));
        bottomNavigation.setAccentColor(getColor(R.color.blue400));
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (mainFragment == null){
                    mainFragment = navigationAdapter.getCurrentFragment();
                }
                if (mainFragment != null){
                    mainFragment.willBeHidden(position);
                }
//                viewPager.setCurrentItem(position,false);
//                if (mainFragment == null){
//                    return  true;
//                }
//                mainFragment = navigationAdapter.getCurrentFragment();
//                mainFragment.willBeDisplay();
                return true;
            }
        });
    }

    public void updateBottomNavigationColor(boolean isColored){
        bottomNavigation.setColored(isColored);
    }

    public AHBottomNavigation getBottomNavigation() {
        return bottomNavigation;
    }

    public void showOrHideBottomNavigation(boolean show){
        if (show){
            bottomNavigation.restoreBottomNavigation(true);
        }else   {
            bottomNavigation.hideBottomNavigation(true);
        }
    }

    public void setBottomItemTitleHide(boolean hide){
        bottomNavigation.setTitleState(hide? AHBottomNavigation.TitleState.ALWAYS_HIDE: AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }

    public int getBottomItemCount(){
        return bottomNavigation.getItemsCount();
    }
}
