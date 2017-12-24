package android.mxdlzg.com.bluewindmill.model.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/3/25.
 */

public class ManageClassOBJ {

    /**
     * 将ClassOBJ的list存储到文件中，将时间作为uid
     * @param context context
     * @param time 被存储的时间，暂时读取系统时间（可能会产生未知错误）
     * @param list 待存储的classobj list
     */
    public static void cacheClassList(Context context,long time,List<ClassOBJ> list){
        SharedPreferences.Editor editor = context.getSharedPreferences(Config.CLASSOBJ_CACHE+time,Context.MODE_PRIVATE).edit();
        editor.clear().apply();
        editor.putInt("count",list.size());
        editor.apply();
        for (int i = 0;i<list.size();i++){
            cacheOBJ(editor,list.get(i),i);
        }
    }

    /**
     * 存储一个obj
     * @param editor SharedPreferences.Editor
     * @param classOBJ 待存储的classOBJ
     * @param index 在list中的位置
     */
    public static void cacheOBJ(SharedPreferences.Editor editor,ClassOBJ classOBJ,int index){
        String config = classOBJ.getALL();
        editor.putString("config"+index,config);
        editor.apply();
        editor.putString("weeks"+index,classOBJ.getWeekString());
        editor.apply();
    }

    /**
     * @param context context
     * @param time 时间，uid
     * @return 返回存储到文件中的classOBJ的list
     */
    public static List<ClassOBJ> getClassList(Context context,long time){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.CLASSOBJ_CACHE+time,Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("count",0);
        List<ClassOBJ> list = new ArrayList<>();
        for (int i = 0;i<size;i++){
            list.add(getOBJ(sharedPreferences,i));
        }
        return list;
    }

    /**
     * @param sharedPreferences shared
     * @param index index
     * @return 返回一个ClassOBJ
     */
    public static ClassOBJ getOBJ(SharedPreferences sharedPreferences,int index){
        String[] config = sharedPreferences.getString("config"+index,"").split(";");
        String[] weeks = sharedPreferences.getString("weeks"+index,"").split(",");
        ClassOBJ classOBJ = new ClassOBJ(Integer.valueOf(config[0]),config[1],config[2],config[3],Integer.valueOf(config[4]),Integer.valueOf(config[5]),Integer.valueOf(config[6]),Integer.valueOf(config[7]),Float.valueOf(config[8]),Boolean.valueOf(config[9]),weeks,Integer.valueOf(config[10]));
        return classOBJ;
    }


}
