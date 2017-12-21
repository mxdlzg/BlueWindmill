package android.mxdlzg.com.bluewindmill.model.entity.config;

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
    public static final String EMS_SCHEDULE_URL = "http://ems.sit.edu.cn:85/student/selCourse/syllabuslist.jsp";
    public static final String EMS_EXAM_URL = "http://ems.sit.edu.cn:85/student/main.jsp";
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

    public static final int CALENDAR_MAXHEIGHT = 350;
    public static final int CALENDAR_MINHEIGHT = 30;

    public static final int GET_COOKIE_ERROR = -1;


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
    public static final String EMS_LOGIN_URL = "http://ems.sit.edu.cn:85/login.jsp";



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
