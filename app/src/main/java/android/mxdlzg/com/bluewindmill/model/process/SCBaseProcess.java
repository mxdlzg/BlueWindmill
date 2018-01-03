package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import android.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import android.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCBaseProcess extends BaseProcess{

    public SCBaseProcess(String body) {
        super(body);
    }

    public static SCInfo getSCInfo(String content){
        throw new UnsupportedOperationException();
        // TODO: 2018/1/2  
    }

    public static List<SCScoreDetail> getScoreDetailList(String content){
        throw new UnsupportedOperationException();// TODO: 2018/1/2
    }

    public static List<String> getNavigationUrls(String content) {
        List<String> list = null;
        // TODO: 2018/1/2  
        return list;
    }

    public static SCActivityDetail getActivityDetail(String content) {
        throw new UnsupportedOperationException();
    }

    public static List<SCActivityDetail> getActivityList(String content) {
        throw new UnsupportedOperationException();
    }
}
