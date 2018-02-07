package project.mxdlzg.com.bluewindmill.model.local;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/5/1.
 */

public class ManageExam {
    /**
     * 存储exams
     * @param context context
     * @param exams exam list
     */
    public static void cacheExam(Context context, List<String> exams){
        SharedPreferences.Editor editor = context.getSharedPreferences("exam",Context.MODE_PRIVATE).edit();
        //清空
        editor.clear();
        editor.apply();
        //写入count
        editor.putInt("count",exams.size());
        editor.apply();
        //循环写入
        for (int i = 0;i<exams.size();i++){
            editor.putString(String.valueOf(i),exams.get(i));
        }
        editor.apply();
    }

    /**
     * @param context context
     * @return 返回exam List<String>
     */
    public static List<String> getExamList(Context context){
        List<String> result = new ArrayList<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("exam",Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("count",0);
        for (int i=0;i<size;i++){
            result.add(sharedPreferences.getString(String.valueOf(i),null));
        }
        return result;
    }

}
