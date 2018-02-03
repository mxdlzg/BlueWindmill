package android.mxdlzg.com.bluewindmill.test;

import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.mxdlzg.com.bluewindmill.net.request.LoginRequest;
import android.mxdlzg.com.bluewindmill.net.request.TableRequest;

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
                System.out.println(message);
                TableRequest.requestScore(getContext(), "2017秋", new CommonCallback<NetResult<ScoreOBJ>>() {
                    @Override
                    public void onSuccess(NetResult<ScoreOBJ> message) {
                        System.out.println("score end!");
                    }
                });
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