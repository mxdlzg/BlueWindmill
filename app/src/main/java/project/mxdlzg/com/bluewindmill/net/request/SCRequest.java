package project.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import project.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;
import project.mxdlzg.com.bluewindmill.model.process.SCBaseProcess;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCRequest {
    /**
     * get score item count 获取第二课堂记录数量
     * @param context context
     * @param callback callback
     */
    public static void requestScInfo(Context context, final CommonCallback<SCInfo> callback){
        OkGo.<SCInfo>get(Config.SC_SCORE_DETAIL_URL)
                .tag(context)
                .execute(new AbsCallback<SCInfo>() {
                    @Override
                    public void onSuccess(Response<SCInfo> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<SCInfo> response) {
                        callback.onError(response.getException().getMessage()+Config.codeConvertor(response.code()));
                    }

                    @Override
                    public SCInfo convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getSCInfo(response.body().string());
                    }
                });
    }

    /**
     * 获取成绩的详情，参数为页码和页码内数量
     * @param context context
     * @param pageNo page
     * @param pageSize total count / page
     * @param callback callback
     */
    public static void requestSCScoreDetail(Context context, String pageNo, String pageSize, final CommonCallback<NetResult<List<SCScoreDetail>>> callback){
        OkGo.<NetResult<List<SCScoreDetail>>>post(Config.SC_SCORE_DETAIL_URL)
                .tag(context)
                .params("pageNo",pageNo)
                .params("pageSize",pageSize)
                .execute(new AbsCallback<NetResult<List<SCScoreDetail>>>() {
                    @Override
                    public void onSuccess(Response<NetResult<List<SCScoreDetail>>> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<NetResult<List<SCScoreDetail>>> response) {
                        callback.onError(response.getException().getMessage()+Config.codeConvertor(response.code()));
                    }

                    @Override
                    public NetResult<List<SCScoreDetail>> convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getScoreDetailList(response.body().string());
                    }
                });
    }

    /**
     * 获取上方导航栏内的几个模块的导航地址
     * @param context context
     * @param callback callback
     */
    public static void requestURLNavigation(final Context context, final CommonCallback<Response<List<String>>> callback){
        OkGo.<List<String>>get(Config.SC_INDEX_URL)
                .tag(context)
                .execute(new AbsCallback<List<String>>() {
                    @Override
                    public void onSuccess(Response<List<String>> response) {
                        if (response.body() == null){
                            callback.onFail(null);
                        }else{
                            callback.onSuccess(response);
                        }
                    }

                    @Override
                    public void onError(Response<List<String>> response) {
                        callback.onError(response.getException().getMessage(),context);
                    }

                    @Override
                    public List<String> convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getNavigationUrls(response.body().string());
                    }
                });
    }

    /**
     * 获取活动list
     * @param context context
     * @param pageNo no
     * @param pageSize size / page
     * @param categoryID id
     * @param callback callback
     */
    public static void requestActivityList(final Context context,int pageNo,int pageSize, String categoryID, final CommonCallback<NetResult<List<SCActivityDetail>>> callback){
        OkGo.<NetResult<List<SCActivityDetail>>>get(Config.SC_ACTIVITY_LIST_URL)
                .tag(context)
                .params("pageNo",pageNo)
                .params("pageSize",pageSize)
                .params("categoryId",categoryID)
                .execute(new AbsCallback<NetResult<List<SCActivityDetail>>>() {
                    @Override
                    public void onSuccess(Response<NetResult<List<SCActivityDetail>>> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<NetResult<List<SCActivityDetail>>> response) {
                        callback.onError(context,response,false);
                    }

                    @Override
                    public NetResult<List<SCActivityDetail>> convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getActivityList(response.body().string());
                    }
                });
    }

    /**
     * 获取活动详情
     * @param context context
     * @param activityID acId
     * @param callback callback
     */
    public static void requestActivityDetail(final Context context, String activityID, final CommonCallback<SCActivityDetail> callback){
        OkGo.<SCActivityDetail>post(Config.SC_ACTIVITY_DETAIL_URL)
                .tag(context)
                .params("activityId",activityID)
                .execute(new AbsCallback<SCActivityDetail>() {
                    @Override
                    public void onSuccess(Response<SCActivityDetail> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<SCActivityDetail> response) {
                        callback.onError(response.getException().getMessage(),context);
                    }

                    @Override
                    public SCActivityDetail convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getActivityDetail(response.body().string());
                    }
                });
    }

}
