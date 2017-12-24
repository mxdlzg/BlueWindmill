package android.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.DataTable;
import android.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.UnifiedScore;
import android.mxdlzg.com.bluewindmill.model.process.PrepareExam;
import android.mxdlzg.com.bluewindmill.model.process.PrepareSchedule;
import android.mxdlzg.com.bluewindmill.model.process.PrepareScore;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by 廷江 on 2017/12/18.
 */

public class TableRequest {
    /**
     * Get schedule list
     * <Exception>由于OkGo库的原因，post不支持gbk编码，需要使用二进制上传</Exception>
     * <Notifiy>gbk编码问题已通过修改并重新编译OkGo库修正，2017年12月24日21:47:22</Notifiy>
     */
    public static void requestSchedule(final Context context, String yearTerm, String cType, String yearTerm2, final CommonCallback<List<ClassOBJ>> callback){
        OkGo.<List<ClassOBJ>>post(Config.EMS_SCHEDULE_URL)
                .tag(context)
                .charSet("gbk")
                .params("yearTerm",yearTerm)
                .params("cType",cType)
                .params("yearTerm2",yearTerm2)
                .execute(new AbsCallback<List<ClassOBJ>>() {
                    @Override
                    public void onStart(Request<List<ClassOBJ>, ? extends Request> request) {
                    }

                    @Override
                    public void onSuccess(Response<List<ClassOBJ>> response) {
                        System.out.println("net success"+Thread.currentThread().getId());
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<List<ClassOBJ>> response) {
                        callback.onFail(null);
                    }

                    @Override
                    public List<ClassOBJ> convertResponse(okhttp3.Response response) throws Throwable {
                        //Child Thread
                        System.out.println("convert"+Thread.currentThread().getId());
                        //Return
                        return new PrepareSchedule().getList(response.body().string(),true);
                    }
                });

    }

    /**
     * 请求考试安排
     * @param context context
     * @param callback callback
     */
    public static void requestExam(final Context context, final CommonCallback<List<String>> callback){
        OkGo.<List<String>>get(Config.EMS_EXAM_URL)
                .tag(context)
                .execute(new AbsCallback<List<String>>() {
                    @Override
                    public void onSuccess(Response<List<String>> response) {
                        if (response.isFromCache()){
                            Toast.makeText(context, "获取失败，从缓存读取", Toast.LENGTH_SHORT).show();
                        }
                        if (callback != null){
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<List<String>> response) {
                        if (callback != null){
                            Toast.makeText(context, Config.codeConvertor(response.code()), Toast.LENGTH_SHORT).show();
                            callback.onFail(null);
                        }
                    }

                    @Override
                    public List<String> convertResponse(okhttp3.Response response) throws Throwable {
                        return new PrepareExam().getExam(response.body().string());
                    }
                });
    }

    /**
     * 请求成绩界面
     * @param context context
     * @param yearTerm year
     * @param callback callback
     */
    public static void requestScore(final Context context,String yearTerm, final CommonCallback<DataTable<ScoreOBJ>> callback){
        OkGo.<DataTable<ScoreOBJ>>post(Config.EMS_SCORE_URL)
                .tag(context)
                .charSet("gbk")
                .params("yearTerm",yearTerm)
                .params("studentID",Config.USER_NAME)
                .execute(new AbsCallback<DataTable<ScoreOBJ>>() {
                    @Override
                    public void onSuccess(Response<DataTable<ScoreOBJ>> response) {
                        if (callback != null){
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<DataTable<ScoreOBJ>> response) {
                        if (callback != null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public DataTable<ScoreOBJ> convertResponse(okhttp3.Response response) throws Throwable {
                        return PrepareScore.getScoreTable(response.body().string());
                    }
                });
    }

    /**
     * 请求绩点
     * @param context context
     * @param st1 start
     * @param st2 end
     * @param callback callback
     */
    public static void requestScorePoint(final Context context, String st1, String st2, final CommonCallback<String> callback){
        OkGo.<String>post(Config.EMS_SCORE_POINT_URL)
                .tag(context)
                .charSet("gbk")
                .params("srTerm",st1)
                .params("srTerm2",st2)
                .params("op","list")
                .execute(new AbsCallback<String>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (callback != null){
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        if (callback !=null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        throw new UnsupportedOperationException();
                    }
                });
    }

    /**
     * 请求校外考试成绩
     * @param context context
     * @param callback callback
     */
    public static void requestUnifiedExamScore(final Context context, final CommonCallback<DataTable<UnifiedScore>> callback){
        OkGo.<DataTable<UnifiedScore>>get(Config.EMS_UNIFIED_SCORE_URL)
                .tag(context)
                .execute(new AbsCallback<DataTable<UnifiedScore>>() {
                    @Override
                    public void onSuccess(Response<DataTable<UnifiedScore>> response) {
                        if (callback != null)callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<DataTable<UnifiedScore>> response) {
                        if (callback !=null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public DataTable<UnifiedScore> convertResponse(okhttp3.Response response) throws Throwable {
                        throw new UnsupportedOperationException();
                    }
                });
    }

}












