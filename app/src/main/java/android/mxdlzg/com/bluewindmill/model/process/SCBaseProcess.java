package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import android.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import android.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;
import android.mxdlzg.com.bluewindmill.model.entity.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCBaseProcess extends BaseProcess{

    public SCBaseProcess(String body) {
        super(body);
    }

    /**
     * Get Sc Info
     * @param content body
     * @return Scinfo
     */
    public static SCInfo getSCInfo(String content){
        Pattern pattern = Pattern.compile("\\d+(?=</b)");
        Pattern totalPagesPat = Pattern.compile("\\d+(?=</span>页)");
        Pattern totalPattern = Pattern.compile("\\d+(\\.\\d+)?(?=</font)");
        Pattern detailPattern = Pattern.compile("\\d+(\\.\\d+)?(?=\\()");

        Matcher matcher = pattern.matcher(content);
        Matcher matcherPat = totalPagesPat.matcher(content);
        Matcher totalMatcher = totalPattern.matcher(content);
        Matcher detailMatcher = detailPattern.matcher(content);

        int count = 0,totalPages = 0;
        float[] scScore = new float[3];
        float[] scPresentation = new float[5];
        if (matcher.find()){
            count = Integer.parseInt(matcher.group(0));
        }
        if (totalMatcher.find()){
            scScore[0] = Float.parseFloat(totalMatcher.group(0));
            scScore[1] = Float.parseFloat(totalMatcher.group(1));
            scScore[2] = Float.parseFloat(totalMatcher.group(2));
        }
        if (detailMatcher.find()){
            scPresentation[0] = Float.parseFloat(detailMatcher.group(0));
            scPresentation[1] = Float.parseFloat(detailMatcher.group(1));
            scPresentation[2] = Float.parseFloat(detailMatcher.group(2));
            scPresentation[3] = Float.parseFloat(detailMatcher.group(3));
            scPresentation[4] = Float.parseFloat(detailMatcher.group(4));
        }
        if (matcherPat.find()){
            totalPages = Integer.parseInt(matcherPat.group(0));
        }

        return new SCInfo(count,totalPages,scScore,scPresentation);
    }

    /**
     * Get Sc Score Detail
     * @param content content
     * @return netResult
     */
    public static NetResult<List<SCScoreDetail>> getScoreDetailList(String content){
        //Table
        Table table = BaseProcess.processTable(content,"tbody");

        //Result
        NetResult<List<SCScoreDetail>> result = new NetResult<>(null, Config.NET_RESULT_DEFAULT_ERROR_CODE);
        List<SCScoreDetail> list = null;

        if (table != null){
            list = new ArrayList<>(table.getRowNumber());
            for (int i = 0; i < table.getRowNumber(); i++) {
                list.add(new SCScoreDetail(table.getCell(i,0).getName(),
                        table.getCell(i,1).getName(),
                        table.getCell(i,2).getName(),
                        table.getCell(i,3).getName(),
                        table.getCell(i,4).getName(),
                        table.getCell(i,5).getName(),
                        table.getCell(i,6).getName()
                        ));
            }
            result.setData(list);
        }

        return result;
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
