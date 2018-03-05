package project.mxdlzg.com.bluewindmill.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.LeadingMarginSpan;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import project.mxdlzg.com.bluewindmill.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by 廷江 on 2017/9/29.
 */

public class Util {
    public static SimpleDateFormat format;


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

    public static String getWeekTime(String time) {
        if (format == null){
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        try {
            Date date = format.parse(time);
            if (DateUtils.isToday(date.getTime())){
                return "今天";
            }else {
                return DateUtils.getDayOfWeekString(Calendar.MONDAY,DateUtils.LENGTH_MEDIUM);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    }

    /**
     * Save image
     * @param name captcha
     * @param path image path
     * @param inputStream stream
     * @return isTrue
     */
    public static boolean saveImage(String name, String path, InputStream inputStream){
        try {
            OutputStream output = new FileOutputStream(path + name);
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
                return true;
            } finally {
                output.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @deprecated Can not fit with all device, Use{@link project.mxdlzg.com.bluewindmill.util.Util#getNavigationBarSize(Context)} instead
     * Check if navigation bar visible
     * @param context Context
     * @return isVisble
     */
    @Deprecated
    public static boolean isNavigationBarVisible(Context context){
        return !ViewConfiguration.get(context).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
    }

    /**
     * Get navigationbar size
     * @param context Context
     * @return Navigation Bar Size
     */
    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    private static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return size;
    }
}
