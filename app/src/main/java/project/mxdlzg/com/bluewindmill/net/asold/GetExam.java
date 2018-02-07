package project.mxdlzg.com.bluewindmill.net.asold;

import project.mxdlzg.com.bluewindmill.model.config.HttpMethod;

/**
 * Created by 廷江 on 2017/5/1.
 */

public class GetExam extends GetTemplate{
    /**
     * @param successCallback successCallback
     * @param failCallback failCallback
     * @param url url
     */
    public GetExam(final SuccessCallback successCallback, final FailCallback failCallback, String url) {
        new NetConnection(url, HttpMethod.GET, null, null, new NetConnection.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (successCallback!=null){
                    successCallback.onSuccess(result);
                }
            }
        }, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback!=null){
                    failCallback.onFail();
                }
            }
        },null);
    }
}
