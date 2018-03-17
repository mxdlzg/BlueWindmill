package project.mxdlzg.com.bluewindmill.view.activity;

import android.app.ProgressDialog;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import project.mxdlzg.com.bluewindmill.net.request.LoginRequest;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.L;

import java.io.File;

/**
 * Created by 廷江 on 2017/3/22.
 */

public class LoginActivity extends AppCompatActivity{
    @BindView(R.id.login_btn_login)
    public AppCompatButton buttonLogin;
    @BindView(R.id.login_userLayout)
    public TextInputLayout userLayout;
    @BindView(R.id.login_passwordLayout)
    public TextInputLayout passwordLayout;
    @BindView(R.id.login_captchalayout)
    LinearLayout captchaLayout;
    @BindView(R.id.login_captchaView)
    ImageView captchaView;
    @BindView(R.id.login_verification)
    EditText captchaEditText;
    @BindView(R.id.login_verificationLayout)
    TextInputLayout captchaInputlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        //ButterKnife
        ButterKnife.bind(this);

        //Text input
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
                if (captchaLayout.getVisibility()==View.VISIBLE && captchaEditText.getText().length()==0){
                    captchaInputlayout.setErrorEnabled(true);
                    captchaInputlayout.setError("密码不能为空");
                    return;
                }
                if (!user.equals("")&&!password.equals("")){
                    Login(user,password);
                }
            }
        });

        setData();
    }

    /**
     * Login into system
     * @param user userid
     * @param password pass
     */
    private void Login(String user, String password){
        //View Dialog
        final ProgressDialog dialog = ProgressDialog.show(this,"登录","正在登录...",true,true);

        //Storage with UserInfo
        ManageSetting.tryCacheValue(this, Config.USER_NAME,user,Config.USER_PASSWORD,password);

        //Send Captcha
        if (captchaLayout.getVisibility()==View.VISIBLE){
            login(dialog,captchaEditText.getText().toString());
        }else {
            login(dialog,"");
        }
    }

    private void login(final ProgressDialog dialog,String Yzm){
        //OkGo_Login
        LoginRequest.login(this,Yzm, new CommonCallback<String>() {
            @Override
            public void onSuccess(String message) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }

            @Override
            public void onError(String message) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String message) {
                dialog.dismiss();
                if (message.equals(getString(R.string.loginCheckCaptchaError))){
                    refreshCaptcha();
                }
            }
        });

    }

    /**
     * Read user info from storage and display on edittext
     */
    private void setData(){
        String user = ManageSetting.getStringSetting(this, Config.USER_NAME);
        String pass = ManageSetting.getStringSetting(this, Config.USER_NAME);

        if (!TextUtils.isEmpty(user) && userLayout.getEditText() != null){
            userLayout.getEditText().setText(user);
        }
        if (!TextUtils.isEmpty(pass) && passwordLayout.getEditText() != null){
            passwordLayout.getEditText().setText(pass);
        }
    }

    private void refreshCaptcha(){
        if (captchaLayout.getVisibility()==View.INVISIBLE){
            captchaLayout.setVisibility(View.VISIBLE);
        }
        LoginRequest.refershCaptchaAsBitmap(this, 0, new CommonCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap message) {
                Toast.makeText(LoginActivity.this, "请填写验证码", Toast.LENGTH_SHORT).show();
                captchaView.setImageBitmap(message);
            }

            @Override
            public void onFail(Bitmap message) {
                Toast.makeText(LoginActivity.this, "刷新验证码失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
