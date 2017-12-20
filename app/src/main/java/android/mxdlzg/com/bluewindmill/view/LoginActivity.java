package android.mxdlzg.com.bluewindmill.view;

import android.app.ProgressDialog;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.model.entity.config.Config;
import android.mxdlzg.com.bluewindmill.local.ManageSetting;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.mxdlzg.com.bluewindmill.net.request.LoginRequest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 廷江 on 2017/3/22.
 */

public class LoginActivity extends AppCompatActivity{
    private AppCompatButton buttonLogin;
    private TextInputLayout userLayout;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //user/password
        userLayout = (TextInputLayout) findViewById(R.id.login_userLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.login_passwordLayout);

        userLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    userLayout.setErrorEnabled(false);
                }
            }
        });
        passwordLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

        //login Button
        buttonLogin = (AppCompatButton) findViewById(R.id.login_btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();
                if(user.equals("")){
                    userLayout.setErrorEnabled(true);
                    userLayout.setError("学号不能为空");
                }
                if (password.equals("")){
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("密码不能为空");
                }
                if (!user.equals("")&&!password.equals("")){
                    Login(user,password);
                }
            }
        });

    }

    private void Login(String user, String password){
        final ProgressDialog dialog = ProgressDialog.show(this,"Login","Login into EMS system",true,true);

        //Storage with UserInfo
        ManageSetting.tryCacheValue(this,Config.USER_NAME,user,Config.USER_PASSWORD,password);

        //OkGo_Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.loginEMS(this, new CommonCallback<String>() {
            @Override
            public void onSuccess(String message) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
