package project.mxdlzg.com.bluewindmill.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longmai.mtoken.interfaces.kernel.service.KernelManageService;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.topsec.sslvpn.IVPNHelper;
import com.topsec.sslvpn.OnAcceptResultListener;
import com.topsec.sslvpn.OnAcceptSysLogListener;
import com.topsec.sslvpn.datadef.BaseAccountInfo;
import com.topsec.sslvpn.datadef.BaseCaptchaInfo;
import com.topsec.sslvpn.datadef.BaseConfigInfo;
import com.topsec.sslvpn.datadef.BaseResourceInfo;
import com.topsec.sslvpn.datadef.ServiceAuthCfg;
import com.topsec.sslvpn.datadef.VPNStaus;
import com.topsec.sslvpn.datadef.eExtraCodeType;
import com.topsec.sslvpn.datadef.eLoginType;
import com.topsec.sslvpn.datadef.eOperateType;
import com.topsec.sslvpn.datadef.eProtocolType;
import com.topsec.sslvpn.datadef.eVerifyType;
import com.topsec.sslvpn.datadef.na.BaseACLInfo;
import com.topsec.sslvpn.datadef.na.NetCardConfigInfo;
import com.topsec.sslvpn.datadef.pf.ResourceInfoForConnect;
import com.topsec.sslvpn.lib.VPNService;
import com.topsec.sslvpn.util.FeatureCodeHelper;
import com.topsec.sslvpn.util.Loger;
import com.topsec.topsap.C0319R;
import com.topsec.topsap.application.DataVector;
import com.topsec.topsap.application.GlobalApp;
import com.topsec.topsap.application.VPNApplication;
import com.topsec.topsap.manager.Shell;
import com.topsec.topsap.manager.VpnProfile;
import com.topsec.topsap.manager.VpnType;
import com.topsec.topsap.view.SysApplication;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import project.mxdlzg.com.bluewindmill.R;

public class VPNActivity extends AppCompatActivity implements OnAcceptSysLogListener, OnAcceptResultListener {
    private static /* synthetic */ int[] f46xd8531af = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$topsec$sslvpn$datadef$eLoginType = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType = null;
    static final int AUTH_CERT = 1;
    static final int AUTH_HW_CERT = 3;
    static final int AUTH_HW_SMS = 5;
    static final int AUTH_HW_TWOFACTOR = 4;
    static final int AUTH_PASSWORD = 0;
    static final int AUTH_TWOFACTOR = 2;
    public static String ExchangeLoginAddress = "";
    public static boolean ExchangeLoginMsg = false;
    static boolean ForgetPW = false;
    public static boolean KickUserGidMsg = false;
    private static final int MY_PASSWORD_DIALOG_ID = 99;
    public static boolean OtherAppUsingTag = false;
    private static final String TAG = "Login";
    public static String VPNaddress;
    static final ServiceConnection conn = new C03201();
    public static VpnProfile mActiveProfile;
    public static BaseACLInfo[] m_BaseACLInfo = null;
    public static NetCardConfigInfo m_NetCardConfigInfo = null;
    private static String m_TmpRSADerKeyFile = "tmprsader.key";
    private static String m_TmpRSAPemKeyFile = "tmprsapem.key";
    private static String m_TmpSM2PEMKeyFile = "tmpsm2pem.key";
    public static boolean m_bLMBluetooth = false;
    public static BaseResourceInfo[] m_briArrayResInfo = null;
    public static int m_iWorkModule = 2;
    public static IVPNHelper m_ihVPNService = null;
    public static ResourceInfoForConnect[] m_rifcpArrayConnectResInfo = null;
    public static ServiceAuthCfg m_sacAuthCfgInfo = null;
    protected static String m_strCallerID;
    protected static String m_strOtherAppScheme;
    private static int selectedIpNum;
    static ProgressDialog waitdialog;
    private TextView About;
    private String CertName_Cert;
    private String CertName_DoubleCert;
    private String CertPassWord_DoubleCert;
    private String FileName_Cert;
    private String FileName_DoubleCert;
    Dialog ForgetPWWaitingDlg;
    private TextView ForgetPassword;
    private ImageView ImgGID;
    private String Ip;
    private ImageButton Login;
    private int LoginCategoryType_Cert;
    private int LoginCategoryType_DoubleCert;
    private String LoginPassWord_DoubleCert;
    private int LoginType = 0;
    private ImageButton Login_down;
    private AlertDialog OtherAppDialog;
    private boolean OtherAppLoginTag = false;
    private TextView OtherLogin;
    private int PaperNum = 0;
    private String PassWord;
    private String PassWord_Cert;
    private int ProtocolIndex_Cert;
    private int ProtocolIndex_DoubleCert;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private String UserName;
    private String UserName_DoubleCert;
    private VPNApplication app;
    int cbTop1 = 0;
    SharedPreferences.Editor editor;
    private String etGID;
    private MaterialEditText etVPN;
    private boolean flag = true;
    private List<Fragment> fragmentList = new ArrayList();
    FragmentTransaction fragmentTransaction;
    private String index = "";
    String info;
    private ArrayList<String> li;
    private LinearLayout logo;
    private Handler mHandler;
    private AlertDialog mShowingDialog;
    private boolean m_bCapOpenState = false;
    protected boolean m_bIsAutoShutdown = true;
    private boolean m_bKickCapOpenState = false;
    private boolean m_bOutErrInfo = false;
    int m_nCaoIndex = 0;
    private String m_strPackageName = "";
    boolean operation_get_resource = true;
    boolean operation_start_service = true;
    private String port;
    SharedPreferences preferences;
    private boolean three_login_false = false;
    ProgressDialog waitdialogforres;
    private String strPackageName;

    static /* synthetic */ int[] $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType() {
        int[] iArr = $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType;
        if (iArr == null) {
            iArr = new int[eOperateType.values().length];
            try {
                iArr[eOperateType.OPERATION_AUTH_LOGININFO.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[eOperateType.OPERATION_CHECK_NETSTATUS.ordinal()] = 14;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[eOperateType.OPERATION_CLOSE_SERVICE.ordinal()] = 13;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[eOperateType.OPERATION_FORGET_PASSWD.ordinal()] = 23;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_CAPTCHA.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_KEEPSTATUS.ordinal()] = 10;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_RESOURCE.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_SERVERCFG.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[eOperateType.OPERATION_LOGIN_SYSTEM.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[eOperateType.OPERATION_LOGOUT_SYSTEM.ordinal()] = 9;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_EMAIL.ordinal()] = 22;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_PASSWORD.ordinal()] = 20;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_TELPHONE.ordinal()] = 21;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[eOperateType.OPERATION_NETCONFIG_NETACCESS.ordinal()] = 18;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[eOperateType.OPERATION_PREPARE_NETACCESS.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[eOperateType.OPERATION_START_NETACCESS.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[eOperateType.OPERATION_START_PFPROXY.ordinal()] = 7;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[eOperateType.OPERATION_START_SERVICE.ordinal()] = 11;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_NETACCESS.ordinal()] = 17;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_PFPROXY.ordinal()] = 8;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_SERVICE.ordinal()] = 12;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[eOperateType.OPERATION_TRYFIX_VPNTUNNEL.ordinal()] = 19;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[eOperateType.OPERATION_UPLOAD_FEATURECODE.ordinal()] = 5;
            } catch (NoSuchFieldError e23) {
            }
            $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType = iArr;
        }
        return iArr;
    }
    static /* synthetic */ int[] m49xd8531af() {
        int[] iArr = f46xd8531af;
        if (iArr == null) {
            iArr = new int[ServiceAuthCfg.eCaptchaType.values().length];
            try {
                iArr[ServiceAuthCfg.eCaptchaType.GID_TYPE_AUTO.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ServiceAuthCfg.eCaptchaType.GID_TYPE_OFF.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ServiceAuthCfg.eCaptchaType.GID_TYPE_ON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            f46xd8531af = iArr;
        }
        return iArr;
    }
    static class C03201 implements ServiceConnection {
        C03201() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("onServiceConnected");
        }

        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected");
        }
    }
    class C04808 extends VpnProfile {
        C04808() {
        }

        public VpnType getType() {
            return null;
        }
    }
    private AppCompatButton loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vpn);

        loginButton = findViewById(R.id.vpn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOP();
            }
        });
    }

    private void doOP(){
        try {
            //Loger.Initialize(getBaseContext(), null, 0, 2048, 2097152, "yyyy-MM-dd HH:mm:ss", false);
            m_ihVPNService = VPNService.getVPNInstance(getApplicationContext());
            if (m_ihVPNService == null){
                throw new Exception("Service init fail!");
            }
        } catch (Exception Ex) {
            Log.i("TopVPNSdkDemo", Ex.toString());
            return;
        }
        m_ihVPNService.setOnAcceptResultListener(this);
        m_ihVPNService.setOnAcceptSysLogListener(this);
        this.m_strPackageName = getPackageName();
        DataVector.GetDataVector().SetPackageName(this.m_strPackageName);

        initUI();

        File destDir = new File("/mnt/sdcard/sslvpn");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        String strKeyFirms = "/mnt/sdcard/sslvpn/KeyFirms";
        File filedir = new File(strKeyFirms);
        if (!filedir.exists()) {
            try {
                filedir.createNewFile();
                FileWriter fw = new FileWriter(strKeyFirms, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("3");
                bw.flush();
                bw.close();
                fw.close();
            } catch (Exception e) {
            }
        }
        if (19 <= Build.VERSION.SDK_INT) {
            getExternalFilesDirs("/");
        }
        PutClientProg(m_TmpRSAPemKeyFile);
        PutClientProg(m_TmpRSADerKeyFile);
        PutClientProg(m_TmpSM2PEMKeyFile);
        //setScode();

        startService(new Intent(this, KernelManageService.class));

        DoLoginOperation();
    }

    private void initUI() {

    }

    private boolean PutClientProg(String sfilename) {
        Context appContext = getApplicationContext();
        try {
            InputStream is = super.getAssets().open(sfilename);
            String filepath = new StringBuilder(String.valueOf(appContext.getFilesDir().getPath())).append("/").append(sfilename).toString();
            FileOutputStream fos = new FileOutputStream(filepath);
            byte[] buffer = new byte[8192];
            int total = 0;
            while (true) {
                int count = is.read(buffer);
                if (count <= 0) {
                    fos.close();
                    is.close();
                    new Shell("SAM_TEST", "chmod 755 " + filepath, false).start();
                    return true;
                }
                fos.write(buffer, 0, count);
                total += count;
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            return false;
        }
    }

    private void DoLoginOperation() {
        boolean FingerPrint_msg = false;
        try {
            this.preferences = getSharedPreferences("FingerPrint", 0);
            FingerPrint_msg = this.preferences.getBoolean("FingerPrint_msg", false);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
//        if (FingerPrint_msg) {
//            showFingerPrintDialog();
//        } else {
        AutoLogin();
//        }
    }

    private void AutoLogin() {
        boolean autologin = true;
        boolean no_autologin_only = true;
        try {
            this.preferences = getSharedPreferences("AutoLogin", 0);
            autologin = this.preferences.getBoolean("autologin", true);
            no_autologin_only = this.preferences.getBoolean("no_autologin_only", true);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        if (no_autologin_only && autologin) {
            this.index = this.preferences.getString("index", "nothing");
                VPNaddress = this.preferences.getString("VPNaddress", "210.35.64.3");
                this.UserName = this.preferences.getString("UserName_1", "1510400212");
                this.PassWord = this.preferences.getString("PassWord_1", "222222");
            connectVPN(VPNaddress);
        }
    }

    private void connectVPN(String strAddr) {
        if ("".equals(strAddr)) {
            Toast.makeText(getApplicationContext(), C0319R.string.vpn_id_empty, Toast.LENGTH_LONG).show();
        } else if (-1 < strAddr.indexOf("http://")) {
            Toast.makeText(getApplicationContext(), C0319R.string.vpn_not_support_http, Toast.LENGTH_LONG).show();
        } else {
            int iStart = strAddr.indexOf("https://");
            if (-1 < iStart) {
                strAddr = strAddr.substring(iStart + 8);
            }
            String[] strArr = strAddr.split(":");
            int iRemotePort = 443;
            if (2 == strArr.length) {
                if (1 > strArr[0].length() || 1 > strArr[1].length()) {
                    Toast.makeText(getApplicationContext(), C0319R.string.illegal_vpn_gateway, Toast.LENGTH_LONG).show();
                    return;
                }
                strAddr = strArr[0];
                try {
                    iRemotePort = Integer.parseInt(strArr[1], 10);
                } catch (NumberFormatException e) {
                    iRemotePort = -1;
                }
                if (iRemotePort < 0) {
                    Toast.makeText(getApplicationContext(), C0319R.string.illegal_vpn_gateway, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            BaseConfigInfo bciInfo = new BaseConfigInfo();
            bciInfo.m_iLogLevel = 0;
            bciInfo.m_blAutoReConnect = true;
            bciInfo.m_iRetryCount = 10;
            bciInfo.m_iTimeOut = 5;
            bciInfo.m_iEnableModule = 2;
            bciInfo.m_strVPNIP = strAddr;
            bciInfo.m_iServerPort = iRemotePort;
            bciInfo.m_iWorkMode = 0;
            bciInfo.m_strExchangeData = "";
            m_ihVPNService.setConfigInfo(bciInfo);
            DataVector dv = DataVector.GetDataVector();
            dv.SetIP(bciInfo.m_strVPNIP);
            dv.SetPort(bciInfo.m_iServerPort);
            mActiveProfile = new C04808();
            this.Ip = strAddr;
            this.port = String.valueOf(iRemotePort);
            mActiveProfile.setServerName(this.Ip);
            mActiveProfile.setServerPort(this.port);
            connect(mActiveProfile);
        }
    }

    private void connect(VpnProfile p) {
        showwaitdlg("Loading", "获取登录参数");
    }

    public void showwaitdlg(String strTitle, String strInfo) {
        waitdialog = new ProgressDialog(this);
        //DataVector.GetDataVector().SetWelcomeProgressDialog(waitdialog);
        waitdialog.setProgressStyle(0);
        waitdialog.setTitle(strTitle);
        waitdialog.setMessage(strInfo);
        waitdialog.show();
    }

    public void getLoginMessage() {
        DataVector dv;
        BaseAccountInfo arg;
        if (this.index.equals("tab1")) {
            dv = DataVector.GetDataVector();
            arg = new BaseAccountInfo();
            arg.m_iLoginType = eLoginType.LOGIN_TYPE_CODEWORD.value();
            arg.m_strAccount = this.UserName;
            arg.m_strLoginPasswd = this.PassWord;
            if (this.etGID != null && this.etGID.length() > 0) {
                arg.m_iExtraCodeType = eExtraCodeType.EXTRA_CODE_CAPTCHA.value();
            }
            arg.m_strExtraCode = this.etGID;
            if (arg.m_strPhoneFeatureCode == null) {
                arg.m_strPhoneFeatureCode = FeatureCodeHelper.getPhoneFeatureCode(getApplicationContext());
            }
            dv.SetPwd(this.PassWord);
            m_ihVPNService.loginVOne(arg);
            if (waitdialog.isShowing()) {
                waitdialog.setTitle(getString(C0319R.string.wait));
                waitdialog.setMessage(getString(C0319R.string.login_wait));
            } else {
                showwaitdlg(getString(C0319R.string.wait), getString(C0319R.string.login_wait));
            }
            this.LoginType = 0;
        } else if (this.index.equals("tab2")) {
            dv = DataVector.GetDataVector();
            arg = new BaseAccountInfo();
            strPackageName = dv.GetPackageName();
            if (this.LoginType == 1) {
                if (this.cbTop1 == 0) {
                    arg.m_iLoginType = eLoginType.LOGIN_TYPE_CERT.value();
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_SOFTCERT.value();
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_INTERN.value();
                }
            } else if (this.LoginType == 3) {
                if (this.cbTop1 == 0) {
                    arg.m_iLoginType = eLoginType.LOGIN_TYPE_CERT.value();
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_HARDCERT.value();
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_INTERN.value();
                    arg.m_strPackageName = strPackageName;
                } else {
                    arg.m_iLoginType = eLoginType.LOGIN_TYPE_CERT.value();
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_HARDCERT.value();
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_CLOSE.value();
                    arg.m_strPackageName = strPackageName;
                }
            }
            arg.m_strCerPath = this.CertName_Cert;
            arg.m_strCerPasswd = this.PassWord_Cert;
            if (arg.m_strPhoneFeatureCode == null) {
                arg.m_strPhoneFeatureCode = FeatureCodeHelper.getPhoneFeatureCode(getApplicationContext());
            }
            m_ihVPNService.loginVOne(arg);
            if (waitdialog.isShowing()) {
                waitdialog.setTitle(getString(C0319R.string.wait));
                waitdialog.setMessage(getString(C0319R.string.login_wait));
                return;
            }
            showwaitdlg(getString(C0319R.string.wait), getString(C0319R.string.login_wait));
        } else if (this.index.equals("tab3")) {
            dv = DataVector.GetDataVector();
            arg = new BaseAccountInfo();
            strPackageName = dv.GetPackageName();
            arg.m_iLoginType = eLoginType.LOGIN_TYPE_DOUBLEFACTOR.value();
            arg.m_strPackageName = strPackageName;
            if (this.LoginType == 2) {
                if (this.cbTop1 == 0) {
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_INTERN.value();
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_SOFTCERT.value();
                }
            } else if (this.LoginType == 4) {
                if (this.cbTop1 == 0) {
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_INTERN.value();
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_HARDCERT.value();
                } else {
                    arg.m_iAuthType = eVerifyType.VERIFY_TYPE_HARDCERT.value();
                    arg.m_iProtocolType = eProtocolType.PROTOCOL_TYPE_CLOSE.value();
                }
            }
            arg.m_strCerPath = this.CertName_DoubleCert;
            arg.m_strAccount = this.UserName_DoubleCert;
            arg.m_strLoginPasswd = this.LoginPassWord_DoubleCert;
            arg.m_strCerPasswd = this.CertPassWord_DoubleCert;
            if (arg.m_strPhoneFeatureCode == null) {
                arg.m_strPhoneFeatureCode = FeatureCodeHelper.getPhoneFeatureCode(getApplicationContext());
            }
            m_ihVPNService.loginVOne(arg);
            if (waitdialog.isShowing()) {
                waitdialog.setTitle(getString(C0319R.string.wait));
                waitdialog.setMessage(getString(C0319R.string.login_wait));
                return;
            }
            showwaitdlg(getString(C0319R.string.wait), getString(C0319R.string.login_wait));
        }
    }

    private void SaveAutoLogin() {
        boolean autologin = true;
        try {
            this.preferences = getSharedPreferences("AutoLogin", 0);
            autologin = this.preferences.getBoolean("autologin", true);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        this.editor = this.preferences.edit();
        this.editor.putBoolean("no_autologin_only", false);
        if (autologin) {
            this.editor.putBoolean("autologin", true);
        }
        if (this.index.equals("tab1")) {
            this.editor.putString("UserName_1", this.UserName);
            this.editor.putString("PassWord_1", this.PassWord);
            this.editor.putString("VPNaddress", VPNaddress);
            this.editor.putString("index", this.index);
            this.editor.commit();
        } else if (this.index.equals("tab2")) {
            this.editor.putString("VPNaddress", VPNaddress);
            this.editor.putString("index", this.index);
            this.editor.putInt("LoginCategoryType_2", this.LoginCategoryType_Cert);
            if (this.LoginCategoryType_Cert == 0) {
                this.editor.putString("FileName_2", this.FileName_Cert);
                this.editor.putString("CertName_2", this.CertName_Cert);
                this.editor.putInt("LoginType", this.LoginType);
                this.editor.putString("PassWord_2", this.PassWord_Cert);
            } else if (this.LoginCategoryType_Cert == 1) {
                this.editor.putInt("cbTop1", this.cbTop1);
                this.editor.putInt("LoginType", this.LoginType);
                this.editor.putString("PassWord_2", this.PassWord_Cert);
            }
            this.editor.commit();
        } else if (this.index.equals("tab3")) {
            this.editor.putString("VPNaddress", VPNaddress);
            this.editor.putString("index", this.index);
            this.editor.putInt("LoginCategoryType_3", this.LoginCategoryType_Cert);
            if (this.LoginCategoryType_DoubleCert == 0) {
                this.editor.putString("FileName_3", this.FileName_Cert);
                this.editor.putString("CertName_3", this.CertName_Cert);
                this.editor.putInt("LoginType", this.LoginType);
                this.editor.putString("CertPassWord_3", this.CertPassWord_DoubleCert);
                this.editor.putString("LoginPassWord_3", this.LoginPassWord_DoubleCert);
                this.editor.putString("UserName_3", this.UserName_DoubleCert);
            } else if (this.LoginCategoryType_DoubleCert == 1) {
                this.editor.putInt("cbTop1", this.cbTop1);
                this.editor.putInt("LoginType", this.LoginType);
                this.editor.putString("CertPassWord_3", this.CertPassWord_DoubleCert);
                this.editor.putString("LoginPassWord_3", this.LoginPassWord_DoubleCert);
                this.editor.putString("UserName_3", this.UserName_DoubleCert);
            }
            this.editor.commit();
        }
    }

    private void SaveExchangeLogin() {
        SharedPreferences spPreferences = getSharedPreferences("ExchangeLogin", 0);
        SharedPreferences.Editor eEditor = spPreferences.edit();
        int num = spPreferences.getInt("AddressNum", 0);
        if (num != 0) {
            int i;
            String[] SaveAddress = new String[num];
            for (i = 0; i < num; i++) {
                SaveAddress[i] = spPreferences.getString("VPNaddress_" + i, "nothing");
            }
            i = 0;
            while (i < num && !VPNaddress.equals(SaveAddress[i])) {
                if (i == num - 1) {
                    eEditor.putInt("AddressNum", num + 1);
                    eEditor.putString("VPNaddress_" + num, VPNaddress);
                }
                i++;
            }
        } else {
            eEditor.putInt("AddressNum", num + 1);
            eEditor.putString("VPNaddress_" + num, VPNaddress);
        }
        eEditor.putString(VPNaddress + "index", this.index);
        if (this.index.equals("tab1")) {
            eEditor.putString(VPNaddress + "UserName_1", this.UserName);
            eEditor.putString(VPNaddress + "PassWord_1", this.PassWord);
            eEditor.commit();
        } else if (this.index.equals("tab2")) {
            eEditor.putInt(VPNaddress + "LoginCategoryType_2", this.LoginCategoryType_Cert);
            if (this.LoginCategoryType_Cert == 0) {
                eEditor.putString(VPNaddress + "FileName_2", this.FileName_Cert);
                eEditor.putString(VPNaddress + "CertName_2", this.CertName_Cert);
                eEditor.putInt(VPNaddress + "LoginType", this.LoginType);
                eEditor.putString(VPNaddress + "PassWord_2", this.PassWord_Cert);
            } else if (this.LoginCategoryType_Cert == 1) {
                eEditor.putInt(VPNaddress + "cbTop1", this.cbTop1);
                eEditor.putInt(VPNaddress + "LoginType", this.LoginType);
                eEditor.putString(VPNaddress + "PassWord_2", this.PassWord_Cert);
            }
            eEditor.commit();
        } else if (this.index.equals("tab3")) {
            eEditor.putInt(VPNaddress + "LoginCategoryType_3", this.LoginCategoryType_DoubleCert);
            if (this.LoginCategoryType_DoubleCert == 0) {
                eEditor.putString(VPNaddress + "FileName_3", this.FileName_DoubleCert);
                eEditor.putString(VPNaddress + "CertName_3", this.CertName_DoubleCert);
                eEditor.putInt(VPNaddress + "LoginType", this.LoginType);
                eEditor.putString(VPNaddress + "CertPassWord_3", this.CertPassWord_DoubleCert);
                eEditor.putString(VPNaddress + "LoginPassWord_3", this.LoginPassWord_DoubleCert);
                eEditor.putString(VPNaddress + "UserName_3", this.UserName_DoubleCert);
            } else if (this.LoginCategoryType_DoubleCert == 1) {
                eEditor.putInt(VPNaddress + "cbTop1", this.cbTop1);
                eEditor.putInt(VPNaddress + "LoginType", this.LoginType);
                eEditor.putString(VPNaddress + "CertPassWord_3", this.CertPassWord_DoubleCert);
                eEditor.putString(VPNaddress + "LoginPassWord_3", this.LoginPassWord_DoubleCert);
                eEditor.putString(VPNaddress + "UserName_3", this.UserName_DoubleCert);
            }
            eEditor.commit();
        }
    }

    public void showPhoneMessageDialog(Activity act) {
        Toast.makeText(act, "ShowPhoneMessageDialog", Toast.LENGTH_SHORT).show();
    }

    public void showKickUserDialog(Activity activity){
        Toast.makeText(activity, "ShowKickUserDialog", Toast.LENGTH_SHORT).show();
    }

    public void ChangPasswordOnClickListener() {
        waitdialog.dismiss();
        //ResourceSettingFragment.ChangePwDialog(getWindow(), false);
    }

    private void getExchangeLoginMsg(String address, Boolean Login_or_not) {
        VPNaddress = address;
        this.etVPN.setText(address);
        if (Login_or_not.booleanValue()) {
            connectVPN(address);
        }
    }

    public static void CallBackToOtherApp(Context cContext, String strReturn) {
        CallBackToOtherAppEx(cContext, strReturn, false);
    }

    public static void CallBackToOtherAppEx(Context cContext, String strReturn, boolean bUseBroadCast) {
        String strUriInfo = "";
        if (strReturn == null || 1 > strReturn.length()) {
            strUriInfo = String.format("%s://ReturnValue=Succeed", new Object[]{m_strOtherAppScheme});
        } else {
            String strReadyEncode;
            if (m_ihVPNService == null) {
                strReadyEncode = "尚未实例化或者APP已被异常关闭";
            } else {
                strReadyEncode = strReturn;
            }
            try {
                strUriInfo = String.format("%s://ReturnValue=Failed?ErrorReason=%s", new Object[]{m_strOtherAppScheme, URLEncoder.encode(strReadyEncode, HTTP.UTF_8)});
            } catch (UnsupportedEncodingException e) {
                strUriInfo = String.format("%s://ReturnValue=Failed?ErrorReason=%s", new Object[]{m_strOtherAppScheme, "Sorry, UrlEncoder process data failed"});
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(m_strCallerID);
        intent.setData(Uri.parse(strUriInfo));
        if (bUseBroadCast) {
            try {
                cContext.sendBroadcast(intent);
                return;
            } catch (ActivityNotFoundException ex) {
                ex.printStackTrace();
                return;
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        cContext.startActivity(intent);
    }

    @Override
    public void onAcceptExecResultListener(int iOperationID, int iRetValue, Object objExtralInfo, Object objReserved) {
        String strNotice = null;
        System.out.println("iRetValue" + iRetValue);
        System.out.println(eOperateType.valueOf(iOperationID));
        ProgressDialog Res_dialog;
        DataVector dv;
        String sTitle;
        String sInfo;
        switch ($SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType()[eOperateType.valueOf(iOperationID).ordinal()]) {
            case 1:
                if (iRetValue != 0) {
                    if (!ExchangeLoginMsg) {
                        strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                        this.m_bOutErrInfo = true;
                        break;
                    }
//                    Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                    if (Res_dialog.isShowing()) {
//                        Res_dialog.dismiss();
//                    }
                    //Resource.ResourceActivity.finish();
                    strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                    this.m_bOutErrInfo = true;
                    break;
                }
                int ii;
                m_briArrayResInfo = (BaseResourceInfo[]) objExtralInfo;
                m_rifcpArrayConnectResInfo = (ResourceInfoForConnect[]) objReserved;
                int iCount = 0;
                if (m_briArrayResInfo != null) {
                    String strKey = "default_null";
                    if (1 == m_iWorkModule) {
                        strKey = "pf";
                    } else {
                        strKey = "na";
                    }
                    for (BaseResourceInfo baseResourceInfo : m_briArrayResInfo) {
                        if (strKey.equalsIgnoreCase(baseResourceInfo.m_strModule)) {
                            iCount++;
                        }
                    }
                }
                int rcCount = 0;
                if (m_rifcpArrayConnectResInfo != null) {
                    for (ii = 0; ii < m_rifcpArrayConnectResInfo.length; ii++) {
                        rcCount++;
                    }
                }
                if (iCount + rcCount <= 0) {
                    if (ExchangeLoginMsg) {
                        ExchangeLoginMsg = false;
//                        Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                        if (Res_dialog.isShowing()) {
//                            Res_dialog.dismiss();
//                        }
                        //Resource.ResourceActivity.finish();
                        strNotice = getString(C0319R.string.exchange_user_false);
                        this.m_bOutErrInfo = true;
                    } else {
                        strNotice = getString(C0319R.string.no_resource);
                        this.m_bOutErrInfo = true;
                    }
                    this.operation_get_resource = false;
                    m_ihVPNService.logoutVOne();
                    if (waitdialog.isShowing()) {
                        waitdialog.dismiss();
                        break;
                    }
                }
                dv = DataVector.GetDataVector();
                dv.SetNAResourceCount(iCount);
                dv.SetBaseResourceInfo(m_briArrayResInfo);
                dv.SetResourceInfoForConnect(m_rifcpArrayConnectResInfo);
                dv.SetFCResourceCount(rcCount);
                if (2 == m_iWorkModule) {
                    m_ihVPNService.startService(this, null);
                    break;
                }
                break;
            case 2:
                if (objExtralInfo == null) {
                    if (!ExchangeLoginMsg) {
                        strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                        this.m_bOutErrInfo = true;
                        break;
                    }
                    ExchangeLoginMsg = false;
//                    Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                    if (Res_dialog.isShowing()) {
//                        Res_dialog.dismiss();
//                    }
                    //Resource.ResourceActivity.finish();
                    strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                    this.m_bOutErrInfo = true;
                    break;
                }
                BaseCaptchaInfo bciTmp = (BaseCaptchaInfo) objExtralInfo;
                if (BitmapFactory.decodeByteArray(bciTmp.m_btData, 0, bciTmp.m_iLength) != null) {
                    dv = DataVector.GetDataVector();
                    dv.SetBaseCaptchaInfo(bciTmp);
                    boolean refresh = false;
                    if (this != null && dv.GetRefreshCap()) {
                        if (waitdialog.isShowing()) {
                            waitdialog.dismiss();
                        }
                        //ShowScaledGid(true);
                        dv.SetRefreshCap(false);
                        refresh = true;
                    }
                    if (!KickUserGidMsg) {
                        if (!ForgetPW) {
                            if (this.m_bCapOpenState) {
                                if (waitdialog.isShowing()) {
                                    waitdialog.dismiss();
                                }
                                if (this.m_bCapOpenState && (this.index.equals("tab1") || this.three_login_false)) {
                                    if (ExchangeLoginMsg) {
//                                        Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                                        if (Res_dialog.isShowing()) {
//                                            Res_dialog.dismiss();
//                                        }
                                        Toast.makeText(this, "GetScaledGid1", Toast.LENGTH_SHORT).show();
                                        //getScaledGid(ResourceSettingFragment.getStaticActivity());
                                    } else {
                                        Toast.makeText(this, "GetScaledGid2", Toast.LENGTH_SHORT).show();
                                        //getScaledGid(this);
                                    }
                                }
                                this.m_bCapOpenState = false;
                                this.m_bKickCapOpenState = true;
                                break;
                            }
                        }
                        if (!refresh) {
                            if (waitdialog.isShowing()) {
                                waitdialog.dismiss();
                            }
                            Toast.makeText(this, "忘记密码Dialog3", Toast.LENGTH_SHORT).show();
                            //showForgetPassworddlg();
                        }
                        this.m_bCapOpenState = false;
                        break;
                    } else if (!ExchangeLoginMsg) {
                        if (this.m_bKickCapOpenState) {
                            Toast.makeText(this, "GetScaledGid4", Toast.LENGTH_SHORT).show();
//                            getScaledGid(this);
                            this.m_bKickCapOpenState = false;
                            break;
                        }
                    } else if (this.m_bKickCapOpenState) {
                       // Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                        if (Res_dialog.isShowing()) {
//                            Res_dialog.dismiss();
//                        }
                        Toast.makeText(this, "GetScaledGid5", Toast.LENGTH_SHORT).show();
//                        getScaledGid(ResourceSettingFragment.getStaticActivity());
                        this.m_bKickCapOpenState = false;
                        break;
                    }
                }
                strNotice = getString(C0319R.string.gid_format_wrong);
                this.m_bOutErrInfo = true;
                break;
            case 3:
                if (iRetValue != 0) {
                    if (-18 != iRetValue) {
                        if (!ExchangeLoginMsg) {
                            strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                            this.m_bOutErrInfo = true;
                            break;
                        }
                        ExchangeLoginMsg = false;
//                        Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                        if (Res_dialog.isShowing()) {
//                            Res_dialog.dismiss();
//                        }
//                        Resource.ResourceActivity.finish();
                        strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                        this.m_bOutErrInfo = true;
                        break;
                    }
                    Toast.makeText(this, "TryToFixTunnel", Toast.LENGTH_SHORT).show();

//                    TryToFixTunnel(iRetValue);
                    return;
                }
                if (objExtralInfo != null) {
                    m_sacAuthCfgInfo = (ServiceAuthCfg) objExtralInfo;
                    switch (m49xd8531af()[m_sacAuthCfgInfo.m_ectCaptchaType.ordinal()]) {
                        case 2:
                            this.m_bCapOpenState = true;
                            break;
                        case 3:
                            if (this.m_nCaoIndex > 2) {
                                this.m_bCapOpenState = true;
                                this.three_login_false = true;
                                break;
                            }
                            break;
                    }
                }
                strNotice = "objExtralInfo is null!";
                this.m_bOutErrInfo = true;
                dv = DataVector.GetDataVector();
                dv.SetAuthPwd(m_sacAuthCfgInfo.m_bEnableCodeWord);
                dv.SetAuthCert(m_sacAuthCfgInfo.m_bEnableCert);
                dv.SetAuthTwoFactor(m_sacAuthCfgInfo.m_bEnableTwoFactor);
                dv.SetAuthTwoFactor_Hide(m_sacAuthCfgInfo.m_bTwoFactorHide);
                dv.SetGid(m_sacAuthCfgInfo.m_ectCaptchaType);
                for (int i = 0; i < this.li.size(); i++) {
                    if (((String) this.li.get(i)).equals(VPNaddress)) {
                        this.li.remove(i);
                    }
                }
                this.li.add(0, VPNaddress);
                if (!this.OtherAppLoginTag) {
                    saveVPNaddress();
                }
                if (!ForgetPW) {
                    if (!this.m_bCapOpenState || !this.index.equals("tab1")) {
                        if (this.m_bCapOpenState && this.m_nCaoIndex > 2) {
                            m_ihVPNService.requestCaptcha();
                            sTitle = getString(C0319R.string.loading);
                            sInfo = getString(C0319R.string.get_gid);
                            waitdialog.setTitle(sTitle);
                            waitdialog.setMessage(sInfo);
                            break;
                        }
                        getLoginMessage();
                        break;
                    }
                    m_ihVPNService.requestCaptcha();
                    sTitle = getString(C0319R.string.loading);
                    sInfo = getString(C0319R.string.get_gid);
                    waitdialog.setTitle(sTitle);
                    waitdialog.setMessage(sInfo);
                    break;
                }
                m_ihVPNService.requestCaptcha();
                waitdialog.setTitle(getString(C0319R.string.wait));
                waitdialog.setMessage(getString(C0319R.string.get_gid));
                break;
            case 4:
                dv = DataVector.GetDataVector();
                if (iRetValue != 0) {
                    this.m_nCaoIndex++;
                    break;
                }
                if (objReserved != null && ((String) objReserved).length() > 0) {
                    Toast.makeText(getBaseContext(), "Token:" + ((String) objReserved), Toast.LENGTH_LONG).show();
                }
                if (!this.OtherAppLoginTag) {
                    SaveAutoLogin();
                    SaveExchangeLogin();
                }
                sTitle = getString(C0319R.string.loading);
                sInfo = getString(C0319R.string.get_resource);
                waitdialog.setTitle(sTitle);
                waitdialog.setMessage(sInfo);
                this.m_nCaoIndex = 0;
                dv.SetCapIndex(this.m_nCaoIndex);
                break;
            case 5:
                if (iRetValue != 0) {
                    strNotice = getString(C0319R.string.scode_false);
                    this.m_bOutErrInfo = true;
                    break;
                }
                strNotice = getString(C0319R.string.scode_success);
                break;
            case 6:
                if (iRetValue != 0) {
                    if (!(m_sacAuthCfgInfo == null || ServiceAuthCfg.eCaptchaType.GID_TYPE_OFF == m_sacAuthCfgInfo.m_ectCaptchaType)) {
                        m_ihVPNService.requestCaptcha();
                    }
                    if (iRetValue != -40024) {
                        if (iRetValue != -40077) {
                            if (iRetValue != -40039) {
                                if (!ExchangeLoginMsg) {
                                    strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                                    this.m_bOutErrInfo = true;
                                    break;
                                }
                                ExchangeLoginMsg = false;
//                                Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                                if (Res_dialog.isShowing()) {
//                                    Res_dialog.dismiss();
//                                }
//                                Resource.ResourceActivity.finish();
                                strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                                this.m_bOutErrInfo = true;
                                break;
                            } else if (ExchangeLoginMsg) {
//                                Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                                if (Res_dialog.isShowing()) {
//                                    Res_dialog.dismiss();
//                                }
//                                showPhoneMessageDialog(ResourceSettingFragment.getStaticActivity());
                                return;
                            } else {
                                if (waitdialog.isShowing()) {
                                    waitdialog.dismiss();
                                }
                                showPhoneMessageDialog(this);
                                return;
                            }
                        } else if (ExchangeLoginMsg) {
//                            Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                            if (Res_dialog.isShowing()) {
//                                Res_dialog.dismiss();
//                            }
//                            showKickUserDialog(ResourceSettingFragment.getStaticActivity());
                            return;
                        } else {
                            showKickUserDialog(this);
                            return;
                        }
                    }
                    ChangPasswordOnClickListener();
                    break;
                }
                DataVector.GetDataVector().SetSessionID(objExtralInfo.toString());
                m_ihVPNService.requestVPNResInfo();
                break;
            case 9:
                if (iRetValue == 0) {
                    if (!OtherAppUsingTag) {
                        if (waitdialog.isShowing()) {
                            waitdialog.dismiss();
                        }
                        if (this.operation_start_service) {
                            if (this.operation_get_resource) {
                                if (ExchangeLoginMsg) {
                                    getExchangeLoginMsg(ExchangeLoginAddress, Boolean.valueOf(true));
                                } else {
                                    Toast.makeText(this, "成功登出VPN系统", Toast.LENGTH_SHORT).show();
                                    boolean no_autologin_only = true;
                                    try {
                                        this.preferences = getSharedPreferences("AutoLogin", 0);
                                        no_autologin_only = this.preferences.getBoolean("no_autologin_only", true);
                                    } catch (ClassCastException ex) {
                                        ex.printStackTrace();
                                    }
                                    if (no_autologin_only) {
//                                        Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                                        if (Res_dialog.isShowing()) {
//                                            Res_dialog.dismiss();
//                                        }
//                                        Resource.ResourceActivity.finish();
                                    } else {
                                        SysApplication.getInstance().exit();
                                    }
                                }
                                this.operation_start_service = true;
                                this.operation_get_resource = true;
                                break;
                            }
                        }
                        if (this.operation_start_service && this.operation_get_resource) {
                            if (ExchangeLoginMsg) {
                                ExchangeLoginMsg = false;
//                                Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                                if (Res_dialog.isShowing()) {
//                                    Res_dialog.dismiss();
//                                }
//                                Resource.ResourceActivity.finish();
                                strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                                this.m_bOutErrInfo = true;
                            } else {
                                strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                                this.m_bOutErrInfo = true;
                            }
                            if (waitdialog.isShowing()) {
                                waitdialog.dismiss();
                            }
                        }
                        this.operation_start_service = true;
                        this.operation_get_resource = true;
                        break;
                    }
//                    if (Resource.ResourceActivity != null) {
//                        Resource.ResourceActivity.finish();
//                    }
                    System.exit(0);
                    break;
                }
                //Resource.ResourceActivity.finish();
                break;
            case 10:
                if (!OtherAppUsingTag) {
                    strNotice = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(getString(C0319R.string.vpn_state) + (VPNStaus.IsUserLoggedin(iRetValue) ? getString(C0319R.string.state_login_success) : getString(C0319R.string.state_login_false)))).append("，").toString())).append(VPNStaus.IsVPNServiceRunning(iRetValue) ? getString(C0319R.string.state_vpn_running) : getString(C0319R.string.state_vpn_not_running)).toString();
                    break;
                }
                int LoginState;
                int ServiceState;
                if (VPNStaus.IsUserLoggedin(iRetValue)) {
                    LoginState = 1;
                } else {
                    LoginState = 0;
                }
                if (VPNStaus.IsVPNServiceRunning(iRetValue)) {
                    ServiceState = 1;
                } else {
                    ServiceState = 0;
                }
                String VPNReturnMsg = "TopSAP://ReturnValue=Succeed&LoginState=" + LoginState + "&ServiceState=" + ServiceState;
                Intent intent = new Intent("TopSAP");
                intent.putExtra("OtherAppReturnMsg", VPNReturnMsg);
                sendBroadcast(intent);
                break;
            case 11:
                if (iRetValue != 0) {
                    if (ExchangeLoginMsg) {
                        ExchangeLoginMsg = false;
//                        Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                        if (Res_dialog.isShowing()) {
//                            Res_dialog.dismiss();
//                        }
//                        Resource.ResourceActivity.finish();
                        strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                        this.m_bOutErrInfo = true;
                    } else {
                        strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                        this.m_bOutErrInfo = true;
                    }
                    this.operation_start_service = false;
                    m_ihVPNService.logoutVOne();
                    if (waitdialog.isShowing()) {
                        waitdialog.dismiss();
                        break;
                    }
                }
                this.m_bIsAutoShutdown = true;
                if (1 == m_iWorkModule) {
                    Log.i(TAG, "data:" + m_ihVPNService.getExchangeDataFromMode(0));
                }
                dv = DataVector.GetDataVector();
                if (waitdialog.isShowing()) {
                    waitdialog.dismiss();
                }
                this.preferences = getSharedPreferences("AutoLogin", 0);
                this.editor = this.preferences.edit();
                this.editor.putBoolean("no_autologin_only", false);
                Intent intent2;
                Bundle bundle;
                if (!this.OtherAppLoginTag) {
                    if (!ExchangeLoginMsg) {
                        intent2 = new Intent();
                        bundle = new Bundle();
                        if (m_sacAuthCfgInfo.m_bEnableBootstrap) {
                            bundle.putBoolean("NewResource_or_not", true);
                            intent2.putExtras(bundle);
                        } else {
                            bundle.putBoolean("NewResource_or_not", false);
                            intent2.putExtras(bundle);
                        }
                        //intent2.setClass(this, Resource.class);
                        startActivity(intent2);
                        break;
                    }
                    ExchangeLoginMsg = false;
                    //Resource.showAclItemList();
                    if (m_sacAuthCfgInfo.m_bEnableBootstrap) {
                        //Resource.initFragment(Boolean.valueOf(true));
                    } else {
                        //Resource.initFragment(Boolean.valueOf(false));
                    }
//                    Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                    if (Res_dialog.isShowing()) {
//                        Res_dialog.dismiss();
//                    }
//                    this.app = (VPNApplication) getApplication();
//                    this.mHandler = this.app.getMainHandler();
//                    Message msg = new Message();
//                    msg.what = 592;
//                    this.mHandler.sendMessage(msg);
                    Toast.makeText(this, "切换账户成功", Toast.LENGTH_LONG).show();
                    break;
                }
                OtherAppUsingTag = true;
                this.OtherAppLoginTag = false;
                intent2 = new Intent();
                bundle = new Bundle();
                if (m_sacAuthCfgInfo.m_bEnableBootstrap) {
                    bundle.putBoolean("NewResource_or_not", true);
                    intent2.putExtras(bundle);
                } else {
                    bundle.putBoolean("NewResource_or_not", false);
                    intent2.putExtras(bundle);
                }
                //intent2.setClass(this, Resource.class);
//                if (Resource.ResourceActivity != null) {
//                    Resource.ResourceActivity.LeaveAMessageForOtherApp("");
//                }
                startActivity(intent2);
                break;
            case 13:
                if (iRetValue != 0) {
                    if (this.operation_start_service && this.operation_get_resource) {
                        if (ExchangeLoginMsg) {
                            ExchangeLoginMsg = false;
//                            Res_dialog = ResourceSettingFragment.exchangeLoginDialog;
//                            if (Res_dialog.isShowing()) {
//                                Res_dialog.dismiss();
//                            }
//                            Resource.ResourceActivity.finish();
                            strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                            this.m_bOutErrInfo = true;
                        } else {
                            ExchangeLoginMsg = false;
                           // Resource.ResourceActivity.finish();
                            strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                            this.m_bOutErrInfo = true;
                        }
                        if (waitdialog.isShowing()) {
                            waitdialog.dismiss();
                        }
                    }
                    this.operation_start_service = true;
                    this.operation_get_resource = true;
                    break;
                }
                int n = m_ihVPNService.logoutVOne();
                if (this.m_bIsAutoShutdown) {
                    Toast.makeText(this, "SendBroadCastToOtherAppWhenAutoShutodown", Toast.LENGTH_SHORT).show();
                    //SendBroadCastToOtherAppWhenAutoShutodown(this);
                    break;
                }
                break;
            case 14:
                strNotice = getString(C0319R.string.internet_state) + m_ihVPNService.getErrorInfoByCode(iRetValue);
                break;
            case 18:
                if (objExtralInfo != null) {
                    m_NetCardConfigInfo = (NetCardConfigInfo) objExtralInfo;
                    m_BaseACLInfo = (BaseACLInfo[]) objReserved;
                    dv = DataVector.GetDataVector();
                    dv.SetNetCardConfigInfo(m_NetCardConfigInfo);
                    dv.SetBaseACLInfo(m_BaseACLInfo);
                    break;
                }
                break;
            case 23:
                if (this.ForgetPWWaitingDlg.isShowing()) {
                    this.ForgetPWWaitingDlg.dismiss();
                }
                if (iRetValue != 0) {
                    Toast.makeText(this, C0319R.string.get_password_false + m_ihVPNService.getErrorInfoByCode(iRetValue), Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Toast.makeText(this, getString(C0319R.string.get_password_success) + ((String) objExtralInfo), Toast.LENGTH_SHORT).show();
                    break;
                }
            default:
                if (iRetValue != 0) {
                    strNotice = m_ihVPNService.getErrorInfoByCode(iRetValue);
                    this.m_bOutErrInfo = true;
                    break;
                }
                break;
        }
        if (!(iRetValue >= 0 || -40024 == iRetValue || -40077 == iRetValue || -40039 == iRetValue)) {
            if (objReserved == null || "".equals(objReserved)) {
                CallBackToOtherApp(this, m_ihVPNService.getErrorInfoByCode(iRetValue));
            } else {
                strNotice = new StringBuilder(String.valueOf(strNotice)).append("&").toString();
                try {
                    strNotice = new StringBuilder(String.valueOf(strNotice)).append(m_ihVPNService.getErrorInfoByCode(Integer.parseInt((String) objReserved))).toString();
                } catch (NumberFormatException e) {
                    strNotice = new StringBuilder(String.valueOf(strNotice)).append((String) objReserved).toString();
                }
                CallBackToOtherApp(this, strNotice);
            }
        }
        if (this.m_bOutErrInfo) {
            if (waitdialog.isShowing()) {
                waitdialog.dismiss();
            }
            ProgressDialog pr = DataVector.GetDataVector().GetResProgressDialog();
            if (pr != null && pr.isShowing()) {
                pr.dismiss();
            }
            Toast.makeText(getApplicationContext(), strNotice+HttpStatus.SC_INTERNAL_SERVER_ERROR,Toast.LENGTH_SHORT ).show();
            this.m_bOutErrInfo = false;
        }
    }

    @Override
    public void onAcceptSysLogInfo(int i, String str, String str2) {

    }

    private void saveVPNaddress() {
        Throwable th;
        ObjectOutputStream out = null;
        GlobalApp.setAlist(this.li);
        try {
            ObjectOutputStream out2 = new ObjectOutputStream(openFileOutput("address.txt", Context.MODE_WORLD_READABLE));
            try {
                if (GlobalApp.isNULL) {
                    this.li = new ArrayList();
                    out2.writeObject(this.li);
                } else {
                    out2.writeObject(this.li);
                }
                if (out2 != null) {
                    try {
                        out2.close();
                        out = out2;
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                out = out2;
            } catch (Exception e2) {
                out = out2;
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                out = out2;
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e4) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th3) {
            th = th3;
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //throw th;
        }
    }
}
