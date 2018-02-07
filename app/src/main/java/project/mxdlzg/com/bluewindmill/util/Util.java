package project.mxdlzg.com.bluewindmill.util;

import android.content.Context;
import project.mxdlzg.com.bluewindmill.R;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by 廷江 on 2017/9/29.
 */

public class Util {
    /**
     * @param context context
     * @return height of statusbar
     */
    public static int getStatusBarSize(Context context){
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight2;
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

    /**
     * @return today
     */
    public static int today(){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @return color
     */
    public static int getRandomColor() {
        Random random = new Random();
        int i = random.nextInt(12);
        int color = 0;
        switch (i) {
            case 0:
                color = R.color.amber200;
                break;
            case 1:
                color = R.color.blue200;
                break;
            case 2:
                color = R.color.blue_Grey200;
                break;
            case 3:
                color = R.color.brown200;
                break;
            case 4:
                color = R.color.cyan200;
                break;
            case 5:
                color = R.color.deep_Orange200;
                break;
            case 6:
                color = R.color.deep_Purple200;
                break;
            case 7:
                color = R.color.green200;
                break;
            case 8:
                color = R.color.pink200;
                break;
            case 9:
                color = R.color.indigo200;
                break;
            case 10:
                color = R.color.light_Blue200;
                break;
            case 11:
                color = R.color.teal200;
                break;
        }
        return color;
    }
}
