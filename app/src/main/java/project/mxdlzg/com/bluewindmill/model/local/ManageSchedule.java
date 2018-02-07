package project.mxdlzg.com.bluewindmill.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.TermOBJ;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by 廷江 on 2017/3/28.
 */

public class ManageSchedule {
    /**
     * 直接对文件进行操作，没有动态
     */
    public ManageSchedule() {

    }

    /**
     * 向文件追加一个新的schedule记录
     * @param context
     * @param termOBJ
     */
    public static void addSchedule(Context context, TermOBJ termOBJ){
        SharedPreferences sharedPreferences = context.getSharedPreferences("scheduleList",Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(termOBJ.getName(),termOBJ.getAll());
        editor.apply();
        int count = sharedPreferences.getInt("count",0);
        editor.putInt("count",count+1);
        editor.apply();
    }

    /**
     * 删除一条schedule记录
     * @param context context
     * @param name 记录的key
     * @param removeFile 是否移除对应的文件
     */
    public boolean removeSchedule(Context context,String name,Long id,boolean removeFile){
        SharedPreferences sharedPreferences = context.getSharedPreferences("scheduleList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(name);
        editor.apply();
        int count = sharedPreferences.getInt("count",0);
        editor.putInt("count",count-1);
        editor.apply();
        if (removeFile){
            File file = new File("/data/data/"+ context.getPackageName() +"/shared_prefs", Config.CLASSOBJ_CACHE+id.toString());
            if (file.exists()){
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 清空
     * @param context context
     */
    public void clearAll(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("scheduleList",Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * @param context context
     * @param list 传入一个list
     */
    public static void getAll(Context context,List<TermOBJ> list){
        SharedPreferences sharedPreferences = context.getSharedPreferences("scheduleList",Context.MODE_PRIVATE);
        Map<String, ?> allSchedule = sharedPreferences.getAll();
        for (Map.Entry<String,?> entry:allSchedule.entrySet()){
            if (!entry.getKey().equals("count")){
                String[] config = entry.getValue().toString().split(";");
                int start = Integer.valueOf(config[1]);
                int end = Integer.valueOf(config[2]);
                int num = Integer.valueOf(config[3]);
                Long id = Long.valueOf(config[4]);
                TermOBJ termOBJ = new TermOBJ(config[0],start,end,num,id);
                list.add(termOBJ);
            }
        }
    }
}
