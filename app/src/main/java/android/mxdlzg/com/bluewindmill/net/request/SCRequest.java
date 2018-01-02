package android.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import android.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;
import android.mxdlzg.com.bluewindmill.model.process.SCBaseProcess;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;

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
    public static void requestItemCount(Context context, final CommonCallback<SCInfo> callback){
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
    public static void requestSCScoreDetail(Context context, String pageNo, String pageSize, final CommonCallback<List<SCScoreDetail>> callback){
        OkGo.<List<SCScoreDetail>>post(Config.SC_SCORE_DETAIL_URL)
                .tag(context)
                .params("pageNo",pageNo)
                .params("pageSize",pageSize)
                .execute(new AbsCallback<List<SCScoreDetail>>() {
                    @Override
                    public void onSuccess(Response<List<SCScoreDetail>> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<List<SCScoreDetail>> response) {
                        callback.onError(response.getException().getMessage()+Config.codeConvertor(response.code()));
                    }

                    @Override
                    public List<SCScoreDetail> convertResponse(okhttp3.Response response) throws Throwable {
                        return SCBaseProcess.getScoreDetailList(response.body().string());
                    }
                });
    }

    /**
     * 获取上方导航栏内的几个模块的导航地址
     * @param context context
     * @param callback callback
     */
    public static void requestURLNavigation(final Context context, final CommonCallback<List<String>> callback){
        OkGo.<List<String>>get(Config.SC_INDEX_URL)
                .tag(context)
                .execute(new AbsCallback<List<String>>() {
                    @Override
                    public void onSuccess(Response<List<String>> response) {
                        if (response.body() == null){
                            callback.onFail(null);
                        }else{
                            callback.onSuccess(response.body());
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


}
