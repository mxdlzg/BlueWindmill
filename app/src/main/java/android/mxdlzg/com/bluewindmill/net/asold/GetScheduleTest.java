package android.mxdlzg.com.bluewindmill.net.asold;

import android.mxdlzg.com.bluewindmill.model.entity.config.HttpMethod;

/**
 * Created by 廷江 on 2017/3/27.
 */

public class GetScheduleTest {
    public GetScheduleTest(final SuccessCallback successCallback, final FailCallback failCallback, String url,String yearTerm,String cType,String yearTerm2) {
        new NetConnection(url, HttpMethod.POST, null, null, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (successCallback != null){
                    successCallback.onSuccess(result);
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null){
                    failCallback.onFail();
                }
            }
        },null,"yearTerm",yearTerm,"cType",cType,"yearTerm2",yearTerm2);
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }
}
