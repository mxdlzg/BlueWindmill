package android.mxdlzg.com.bluewindmill.util;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by 廷江 on 2017/4/3.
 */

public class wdUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale+0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取今天是周几
     * @return 周几
     */
    @NonNull
    public static int getWeekInt(){
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return 0;
        }
    }

    /**
     * 获取今天是周几
     * @return 周几
     */
    @NonNull
    public static String getWeek(){
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 获取week增量
     * @param oldTime 选择当前周的那个日期
     * @param currentTime 当前日期
     * @return 要对currentWeek做的增量
     */
    public static int getIncrement(Long oldTime,Long currentTime){
        Long timeStamp = currentTime-oldTime;
        if (timeStamp <= 0 || oldTime == 0){
            return 0;
        }else {
            timeStamp = timeStamp/(1000*60*60*24);
            return Integer.valueOf(String.valueOf(timeStamp/7));
        }
    }


}
