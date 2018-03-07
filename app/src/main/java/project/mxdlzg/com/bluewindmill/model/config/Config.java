package project.mxdlzg.com.bluewindmill.model.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.lzy.okgo.model.HttpParams;

import java.util.List;

/**
 * Created by 廷江 on 2017/3/11.
 */

public class Config {
    public static final String USER_NAME = "userName";
    public static final String USER_PASSWORD = "password";
    public static final String AUTH_TYPE = "2";
    public static final int NOT_LOGIN = 50;
    public static final int LOGIN = 51;

    public static final int CALENDAR_MAXHEIGHT = 350;
    public static final int CALENDAR_MINHEIGHT = 30;
    public static final int GET_COOKIE_ERROR = -1;
    public static final int NET_RESULT_DEFAULT_CODE = 0;
    public static final int NET_RESULT_DEFAULT_ERROR_CODE = -50;

    //cache
    public static final String NET_COOKIE_CACHE = "netCookie";
    public static final String COOKIE_CACHE = "cookie";
    public static final String CLASSOBJ_CACHE = "classList";
    public static final String SETTING = "setting";
    public static final String HTTP_PARAMS = "httpParams";

    //activity Result
    public static final int LOGIN_OK = 200;
    public static final int LOGIN_FAIL = 400;
    public static final int LOGIN_CANCEL = 204;
    public static final int ADD_SCHEDULE_OK = 100;

    //requestCode
    public static final int START_ADD_SCHEDULE = 150;

    //url
    public static final String EMS_URL = "http://ems.sit.edu.cn:85/";
    public static final String EMS_URL_1 = "http://ems1.sit.edu.cn:85/";
    public static final String EMS_LOGIN_URL = "http://ems.sit.edu.cn:85/login.jsp";
    public static final String EMS_SCHEDULE_URL = "http://ems.sit.edu.cn:85/student/selCourse/syllabuslist.jsp";
    public static final String EMS_EXAM_URL = "http://ems.sit.edu.cn:85/student/main.jsp";
    public static final String EMS_SCORE_URL = "http://ems.sit.edu.cn:85/student/graduate/scorelist.jsp";
    public static final String EMS_SCORE_POINT_URL = "http://ems.sit.edu.cn:85/student/graduate/scorepoint.jsp";
    public static final String EMS_UNIFIED_SCORE_URL = "http://ems.sit.edu.cn:85/student/unifiedExamScore.jsp";
    public static final String SC_LOGIN_URL = "http://sc.sit.edu.cn/j_spring_security_check";
    public static final String SC_SCORE_DETAIL_URL = "http://sc.sit.edu.cn/public/pcenter/scoreDetail.action";
    public static final String SC_ACTIVITY_DETAIL_URL = "http://sc.sit.edu.cn/public/activity/activityDetail.action";
    public static final String SC_INDEX_URL = "http://sc.sit.edu.cn/public/init/index.action";
    public static final String SC_ACTIVITY_LIST_URL = "http://sc.sit.edu.cn/public/activity/activityList.action";
    public static final String EMS_EVALUATE_URL = "http://ems.sit.edu.cn:85/student/EvalTeachScore.action";
    public static final String SC_LOGIN_PASS_URL = "http://my.sit.edu.cn/userPasswordValidate.portal";
    public static final String SC_LOGIN_SUCCESS_URL = "http://my.sit.edu.cn/loginSuccess.portal";
    public static final String SC_LOGIN_FAIL_URL = "http://my.sit.edu.cn/loginFailure.portal";
    public static final String EMS_CAPTCHA_URL = "http://ems.sit.edu.cn:85/GetImageCode";
    public static final String SC_CAPTCHA_URL = "http://my.sit.edu.cn/captchaGenerate.portal";
    public static final String MAIN_INDEX = "http://my.sit.edu.cn/index.portal";
    public static final String SC_PORTAL = "http://sc.sit.edu.cn/portal.jsp";

    //var
    public static List<HttpParams> httpParamsList;
    private SharedPreferences httpParamsPreferences;
    private SharedPreferences settingPre;
    private Context context;
    private static Config instance = new Config();

    /**
     * Single mode
     * @return this
     */
    public static Config getInstance() {
        return instance;
    }

    /**
     * Cons
     */
    public Config() {
    }

    /**
     * @param context application
     */
    public void init(Context context){
        this.context = context;
    }


    /**
     * Delay loading while first use
     */
    private HttpParams readParamsSettingFromXML(String key){
        // TODO: 2017/12/18 由于Http Params类直接序列化(write object)，确认序列化文件存在后读取目标key对应的HttpParams
        // TODO:由于SharedPreferences直接被载入内存，所以可以保证之后的http请求读取params时的速度
        if (httpParamsPreferences == null){
            httpParamsPreferences = context.getSharedPreferences(Config.HTTP_PARAMS,Context.MODE_PRIVATE);
        }else {
            // TODO: 2017/12/18 暂时没必要保存params，暂时停止此处开发
        }
        return null;
    }

    public static String codeConvertor(int code){
        return "错误代码："+String.valueOf(code);
        // TODO: 2017/12/18 Translate code into string
    }
}
