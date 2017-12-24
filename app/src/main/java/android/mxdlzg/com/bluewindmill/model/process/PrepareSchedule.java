package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.entity.ClassOBJ;
import android.mxdlzg.com.bluewindmill.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 廷江 on 2017/3/15.
 */

public class PrepareSchedule {
    /**
     * 解析网页返回的html源码，返回list
     * @param result string网页返回结果
     * @param override 同位置后方是否覆盖前方
     * @return classList
     */
    public List<ClassOBJ> getList(String result,boolean override) {
        Map<String,Integer> colorDict = new HashMap<>();
        List<ClassOBJ> classOBJList = new ArrayList<>();
        boolean table[][] = new boolean[12][7];

        Document document = Jsoup.parse(result,"utf-8");
        Elements elements = document.select("tr[bgcolor]");

        for (int indexs = 0;indexs<14;indexs++){
            if (indexs<2) {
                continue;
            }
            Elements classes = elements.get(indexs).children();
            int count = classes.size();
            int cursor = 1;
            for (int days = 0;days<7;days++){
                if (table[indexs-2][days]){
                    //如果这个格子已经被写入信息,横向下一个
//                    cursor++;
                    continue;
                }else {
                    //否则，读取一个
                    table[indexs-2][days] = true;
                    Elements smash = classes.get(cursor).select("div[name='d1']");
                    if (smash.size()==0){
                        //如果信息依然为空,处理完毕，横向下一个
                        cursor++;
                        continue;
                    }else {
                        //临时变量
                        int tempCursor = cursor;
                        cursor++;
                        //否则确定为由碎片,处理这些信息
                        List<String> names = new ArrayList<>(); //唯一性信息判据
                        List<String> teachers = new ArrayList<>(); //唯一性信息判据
                        Map<String,Integer> list_obj_pair = new HashMap<>(); //唯一性信息组

                        //临时变量
                        String name = "",week = "",position = "",teacher = "";
                        int start = 0,end = 0;

                        //开始处理碎片
                        for (Element sms:smash){
                            //基本信息
                            name = sms.childNode(0).attr("text");
                            teacher = sms.childNode(4).attr("text").split(" ")[1];
                            week = sms.childNode(2).attr("text").split(" ")[0];
                            position = sms.childNode(2).attr("text").split(" ")[1];

                            //匹配周数
                            String regexs = "(?<=(第)).*?(?=(周))";
                            Pattern patterns = Pattern.compile(regexs);
                            Matcher matchers = patterns.matcher(week);
                            if (matchers.find()){
                                // TODO: 2017/10/3 start end 区间获取错误 
                                start = Integer.valueOf(matchers.group().split("-")[0]);
                                if (matchers.group().length() > 1 && matchers.group().split("-").length>1){
                                    end = Integer.valueOf(matchers.group().split("-")[1]);
                                }else {
                                    end = start;
                                }
                            }

                            //开始构造Object,如果是构造则新建，否则追加信息
                            if (!names.contains(name) && !teachers.contains(teacher)){ //确定是已经存在（唯一性）
                                //虚拟table对位补齐
                                for (int i =0;i<Integer.valueOf(classes.get(tempCursor).attr("rowspan"));i++){
                                    table[indexs-2+i][days] = true;
                                }
                                //新建Object
                                ClassOBJ newClass = new ClassOBJ();
                                newClass.setName(name);
                                newClass.setTecher(teacher);
                                newClass.setStart(start);
                                newClass.setEnd(end);
                                newClass.setDay(days+1);
                                newClass.setIndex(indexs-1);
                                newClass.setWeeks(new String[20]);
                                newClass.setNum(Integer.valueOf(classes.get(tempCursor).attr("rowspan")));
                                //设置第一个碎片得到的数据
                                if (sms.html().contains("**")){
                                    //如果是双周
                                    for (int i = (start%2==0?start:start+1);i<=end;i+=2){
                                        newClass.setWeek(i-1,position);
                                    }
                                }else if (sms.html().contains("*")){
                                    //如果是单周
                                    for (int i = (start%2==0?start+1:start);i<=end;i+=2){
                                        newClass.setWeek(i-1,position);
                                    }
                                }else {
                                    //如果是正常
                                    for (int i = start;i<=end;i++){
                                        newClass.setWeek(i-1,position);
                                    }
                                }
                                if (colorDict.containsKey(newClass.getName())){
                                    newClass.setColor(colorDict.get(newClass.getName()));
                                }else {
                                    newClass.setColor(Util.getRandomColor());
                                    colorDict.put(newClass.getName(),newClass.getColor());
                                }
                                //将新课程信息写入链表
                                classOBJList.add(newClass);
                                names.add(name);
                                teachers.add(teacher);
                                list_obj_pair.put(sms.childNode(0).attr("text"),classOBJList.size()-1);
                            }else {
                                ClassOBJ newClass;
                                try{
                                    newClass = classOBJList.get(list_obj_pair.get(name));
                                }catch (Exception e){
                                    newClass = classOBJList.get(classOBJList.size()-1);
                                }
//                                ClassOBJ newClass = classOBJList.get(list_obj_pair.get(name));
                                //设置接下来分析得到的碎片信息
                                if (sms.html().contains("**")){
                                    //如果是双周
                                    for (int i = (start%2==0?start:start+1);i<=end;i+=2){
                                        if (override){
                                            newClass.setWeek(i-1,position);
                                        }else {
                                            if (newClass.getWeeks()[i-1] == null){
                                                newClass.setWeek(i-1,position);
                                            }
                                        }
                                    }
                                }else if (sms.html().contains("*")){
                                    //如果是单周
                                    for (int i = (start%2==0?start+1:start);i<=end;i+=2){
                                        if (override){
                                            newClass.setWeek(i-1,position);
                                        }else {
                                            if (newClass.getWeeks()[i-1] == null){
                                                newClass.setWeek(i-1,position);
                                            }
                                        }
                                    }
                                }else {
                                    //如果是正常
                                    for (int i = start;i<=end;i++){
                                        if (override){
                                            newClass.setWeek(i-1,position);
                                        }else {
                                            if (newClass.getWeeks()[i-1] == null){
                                                newClass.setWeek(i-1,position);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }

        return classOBJList;
    }
}

// TODO: 2017/4/5 全新解析思路：使用二维数组保存当前table的信息，上层解析过的rowspan后在二维数组对应位置true 
// TODO: 2017/4/5 解析指针由x7，y12来控制，扫描每一个空格。解析一个位置的空格时优先判定是否因为上层rowspan而被改变 
// TODO: 2017/4/5 如果改变则day加一，越过此格；否则继续读取一个td判断smash（此时一定要顺序读取td），分析smash完毕后无论是否有信息day都加一，继续解析下个格子 