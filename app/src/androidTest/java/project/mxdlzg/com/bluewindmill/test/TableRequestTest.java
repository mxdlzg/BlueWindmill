package project.mxdlzg.com.bluewindmill.test;

import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.LoginRequest;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by 廷江 on 2018/1/4.
 */
public class TableRequestTest {
    @Test
    public void requestScore() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.loginEMS(getContext(), new CommonCallback<String>() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onFail(String message) {
                System.out.println(message);
            }
        });
    }

    @Test
    public void requestScorePoint() throws Exception {
    }

    @Test
    public void requestUnifiedExamScore() throws Exception {
    }

}