package project.mxdlzg.com.bluewindmill.model.process;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廷江 on 2017/5/1.
 */

public class PrepareExam {
    private List<String> result = new ArrayList<>();

    /**
     * @param examHTML exam页面源码
     * @return 返回一个考试表的数组，一行为一个考试，使用‘；’隔开属性
     */
    public List<String> getExam(String examHTML) throws Throwable {
        if (examHTML.contains("The URL has moved")){
            throw new IndexOutOfBoundsException("Http 302");
        }
        //开始解析
        Document document = Jsoup.parse(examHTML,"utf-8");
        Elements tables = document.select("table");
        //遍历tables
        for (Element table : tables){
            //如果找到
            if (table.child(0).child(0).text().equals("我的考试")){
                //初始化参数
                int index = 0;
                //遍历取每一条考试
                for (Element tr : table.child(0).children()){
                    if (tr.hasAttr("style")){
                        String name = tr.child(1).text();
                        String time = tr.child(2).text();
                        String place = tr.child(3).text();
                        String property = tr.child(4).text();
                        result.add(name+";"+time+";"+place+";"+property);
                        index++;
                    }
                }
                //遍历完毕,结束table循环
                break;
            }
        }
        return result;
    }
}
