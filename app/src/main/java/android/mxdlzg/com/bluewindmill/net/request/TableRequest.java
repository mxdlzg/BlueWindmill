package android.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.UnifiedScore;
import android.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import android.mxdlzg.com.bluewindmill.model.process.PrepareExam;
import android.mxdlzg.com.bluewindmill.model.process.PrepareSchedule;
import android.mxdlzg.com.bluewindmill.model.process.ScoreProcess;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.base.Request;

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
    public static void requestScore(final Context context,String yearTerm, final CommonCallback<NetResult<List<ScoreOBJ>>> callback){
        OkGo.<NetResult<List<ScoreOBJ>>>post(Config.EMS_SCORE_URL)
                .tag(context)
                .charSet("gbk")
                .params("yearTerm",yearTerm)
                .params("studentID", ManageSetting.getStringSetting(context,Config.USER_NAME))
                .execute(new AbsCallback<NetResult<List<ScoreOBJ>>>() {
                    @Override
                    public void onSuccess(Response<NetResult<List<ScoreOBJ>>> response) {
                        if (callback != null){
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<NetResult<List<ScoreOBJ>>> response) {
                        if (callback != null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public NetResult<List<ScoreOBJ>> convertResponse(okhttp3.Response response) throws Throwable {
                        return ScoreProcess.getScoreTable(response.body().string());
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
    public static void requestScorePoint(final Context context, String st1, String st2, final CommonCallback<NetResult<String>> callback){
        OkGo.<NetResult<String>>post(Config.EMS_SCORE_POINT_URL)
                .tag(context)
                .charSet("gbk")
                .params("srTerm",st1)
                .params("srTerm2",st2)
                .params("op","list")
                .execute(new AbsCallback<NetResult<String>>() {
                    @Override
                    public void onSuccess(Response<NetResult<String>> response) {
                        if (callback != null){
                            callback.onSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<NetResult<String>> response) {
                        if (callback !=null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public NetResult<String> convertResponse(okhttp3.Response response) throws Throwable {
                        return ScoreProcess.getScorePoint(response.body().toString());
                    }
                });
    }

    /**
     * 请求校外考试成绩
     * @param context context
     * @param callback callback
     */
    public static void requestUnifiedExamScore(final Context context, final CommonCallback<NetResult<List<UnifiedScore>>> callback){
        OkGo.<NetResult<List<UnifiedScore>>>get(Config.EMS_UNIFIED_SCORE_URL)
                .tag(context)
                .execute(new AbsCallback<NetResult<List<UnifiedScore>>>() {
                    @Override
                    public void onSuccess(Response<NetResult<List<UnifiedScore>>> response) {
                        if (callback != null)callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<NetResult<List<UnifiedScore>>> response) {
                        if (callback !=null){
                            callback.onError(Config.codeConvertor(response.code()));
                        }
                    }

                    @Override
                    public NetResult<List<UnifiedScore>> convertResponse(okhttp3.Response response) throws Throwable {
                        return ScoreProcess.getUnifiedScore(response.body().toString());
                    }
                });
    }

    /**
     * 100分评教
     * @param context context
     * @param courseID courseID
     * @param teacherType teacherType
     * @param yearTerm yearTerm
     * @param teacherID teacherID
     * @param callback callback
     */
    public static void evaluateTeacher(Context context, String courseID, String teacherType, String yearTerm, String teacherID, final CommonCallback<String> callback){
        final GetRequest<String> request = OkGo.<String>get(Config.EMS_EVALUATE_URL)
                .tag(context)
                .charSet("gbk")
                .params("courseID",courseID)
                .params("teachType",teacherType)
                .params("studentID",ManageSetting.getStringSetting(context,Config.USER_NAME))
                .params("yearTerm",yearTerm)
                .params("teacher",teacherID)
                .params("items","100,100,100,100,100,100,100,100,0,0,0,0")
                .params("sucURL","eval.jsp?msg=操作成功")
                .params("failURL","eval.jsp")
                .params("op","setSheet")
                .params("_tbName","openAdmin.teacheval.EvalTeachScore");
        for (int i = 1; i <= 8; i++) {
            request.params("score"+i,"100");
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onError(Response<String> response) {
                callback.onError(Config.codeConvertor(response.code()));
            }
        });

    }

}












