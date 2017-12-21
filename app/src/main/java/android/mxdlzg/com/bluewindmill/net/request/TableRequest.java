package android.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import android.mxdlzg.com.bluewindmill.model.entity.config.ClassOBJ;
import android.mxdlzg.com.bluewindmill.model.entity.config.Config;
import android.mxdlzg.com.bluewindmill.model.process.PrepareSchedule;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

/**
 * Created by 廷江 on 2017/12/18.
 */

public class TableRequest {
    /**
     * Get schedule list
     * <Exception>由于OkGo库的原因，post不支持gbk编码，需要使用二进制上传</Exception>
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

}
