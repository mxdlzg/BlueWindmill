package project.mxdlzg.com.bluewindmill.model.process;

import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.Cell;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.entity.SCActivityDetail;
import project.mxdlzg.com.bluewindmill.model.entity.SCInfo;
import project.mxdlzg.com.bluewindmill.model.entity.SCScoreDetail;
import project.mxdlzg.com.bluewindmill.model.entity.Table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCBaseProcess extends BaseProcess{
    public final static String[] pats = new String[]{
            "活动编号：\\d+",
            "活动地点：.*?(?=&nbsp)",
            "活动时长：.*?(?=<br)",
            "负责人：.*?(?=&nbsp)",
            "负责人电话：.*?(?=&nbsp)",
            "主办方：.*?(?=&nbsp)",
            "承办方：.*?(?=&nbsp)",
            "\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D\\d{1,2}\\D\\d{1,2}",
    };


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
        Pattern urlsPat = Pattern.compile("categoryId=.*\\n");

        Matcher matcher = pattern.matcher(content);
        Matcher matcherPat = totalPagesPat.matcher(content);
        Matcher totalMatcher = totalPattern.matcher(content);
        Matcher detailMatcher = detailPattern.matcher(content);
        Matcher urlsMatcher = urlsPat.matcher(content);

        int count = 0,totalPages = 0;
        float[] scScore = new float[3];
        float[] scPresentation = new float[5];
        Map<String,String> urlKV = new HashMap<>();

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
        if (urlsMatcher.find()){
            String str = null;
            for (int i = 0; i < urlsMatcher.groupCount(); i++) {
                str = urlsMatcher.group(i);
                urlKV.put(str.substring(10,str.indexOf("\"")),str.substring(str.indexOf("<span>"),str.lastIndexOf("</span>")));
            }
        }

        return new SCInfo(count,totalPages,scScore,scPresentation,urlKV);
    }

    /**
     * Get Sc Score Detail
     * @param content content
     * @return netResult
     */
    public static NetResult<List<SCScoreDetail>> getScoreDetailList(String content){
        //Table
        Table table = processTable(content,"tbody");

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

    @Deprecated
    public static List<String> getNavigationUrls(String content) {
        throw new UnsupportedOperationException("此操作功能整合到SCinfo的获取中");
    }

    /**
     * @param content body
     * @return Sc Ac Detail
     */
    public static SCActivityDetail getActivityDetail(String content) {
        content = content.substring(6000);

        String[] strings = new String[10];

        Pattern pattern = null;Matcher matcher = null;
        for (int i = 0; i < pats.length; i++) {
            pattern = Pattern.compile(pats[i]);
            matcher = pattern.matcher(content);
            if (matcher.find()){
                strings[i] = matcher.group(0);
                if (i == pats.length-1){
                    strings[i+1] = matcher.group(1);
                    strings[i+2] = matcher.group(2);
                }
            }
        }
        return new SCActivityDetail(strings);
    }

    public static List<SCActivityDetail> getActivityList(String content) {
        //Table
        Table table = processUL(content,"ul[class=ul_7]");

        //Result
        List<SCActivityDetail> list = new ArrayList<>();

        Pattern idPat = Pattern.compile("(?<=activityId=).*?(?=>)");

        //IF
        if (table != null){
            for (int i = 0; i < table.getRowNumber(); i++) {
                Cell cell = table.getCell(i,0);
                Element element = Jsoup.parse(cell.getName());

                list.add(new SCActivityDetail(idPat.matcher(cell.getName()).group(0),element.text(),
                        table.getCell(i,1).getName().replace("<span>","").replace("</span>","")));
            }
        }
        return list;
    }

}
