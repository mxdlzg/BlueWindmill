package project.mxdlzg.com.bluewindmill.view.activity;

import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import project.mxdlzg.com.bluewindmill.model.entity.ScoreOBJ;
import project.mxdlzg.com.bluewindmill.model.entity.UnifiedScore;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.LoginRequest;
import project.mxdlzg.com.bluewindmill.net.request.SCRequest;
import project.mxdlzg.com.bluewindmill.net.request.TableRequest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by 廷江 on 2018/1/4.
 */

public class NetTest extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_net_test);

        Button btnLogin = (Button) findViewById(R.id.net_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.loginEMS(NetTest.this, "",new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(NetTest.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Button btnScore = (Button) findViewById(R.id.net_score);
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRequest.requestScore(NetTest.this, "2017秋", new CommonCallback<NetResult<List<ScoreOBJ>>>() {
                    @Override
                    public void onSuccess(NetResult<List<ScoreOBJ>> message) {
                        Toast.makeText(NetTest.this, "score Success!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        super.onError(message);
                        Toast.makeText(NetTest.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_point).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRequest.requestScorePoint(NetTest.this, "2017秋", "2017秋", new CommonCallback<NetResult<String>>() {
                    @Override
                    public void onSuccess(NetResult<String> message) {
                        Toast.makeText(NetTest.this, "success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_unifiedScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRequest.requestUnifiedExamScore(NetTest.this, new CommonCallback<NetResult<List<UnifiedScore>>>() {
                    @Override
                    public void onSuccess(NetResult<List<UnifiedScore>> message) {
                        Toast.makeText(NetTest.this, "success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_sc_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest.loginSC(NetTest.this, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(NetTest.this, "login success into SC", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_sc_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCRequest.requestScInfo(NetTest.this, new CommonCallback<SCInfo>() {
                    @Override
                    public void onSuccess(SCInfo message) {
                        Toast.makeText(NetTest.this, "item Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_sc_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SCRequest.requestSCScoreDetail(NetTest.this, "1", "20", new CommonCallback<List<SCScoreDetail>>() {
//                    @Override
//                    public void onSuccess(List<SCScoreDetail> message) {
//                    }
//                });
            }
        });

        findViewById(R.id.net_sc_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCRequest.requestURLNavigation(NetTest.this, new CommonCallback<Response<List<String>>>() {
                    @Override
                    public void onSuccess(Response<List<String>> message) {
                        Toast.makeText(NetTest.this, "nav Success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_sc_ac_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCRequest.requestActivityList(NetTest.this, 1, 20, "001", new CommonCallback<NetResult<List<SCActivityDetail>>>() {
                    @Override
                    public void onSuccess(NetResult<List<SCActivityDetail>> message) {
                        Toast.makeText(NetTest.this, "ac list success", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.net_sc_ac_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCRequest.requestActivityDetail(NetTest.this, "1044601", new CommonCallback<SCActivityDetail>() {
                    @Override
                    public void onSuccess(SCActivityDetail message) {
                        Toast.makeText(NetTest.this, "ac detail Success!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
