package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.entity.Cell;
import android.mxdlzg.com.bluewindmill.model.entity.ColumnLocation;
import android.mxdlzg.com.bluewindmill.model.entity.NetResult;
import android.mxdlzg.com.bluewindmill.model.entity.Table;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class BaseProcess {
    private String body;

    public BaseProcess(String body) {
        this.body = body;
    }

//
//    private NetResult processTable(String body,String identifer){
//        return process(body,identifer);
//    }

    public static NetResult<Element> process(String body, String identifer){
        NetResult<Element> netResult = new NetResult<>(null);
        Document document = Jsoup.parse(body);
        Elements elements = document.select("table");

        for (Element table:
             elements) {
            if (table.html().contains(identifer)){
                netResult.setData(table);
            }
        }
        return netResult;
    }

    public static Table processTable(String body,String identifer){
        //doc
        Document document = Jsoup.parse(body,"utf-8");

        //elements
        Elements tables = document.select(identifer);
        Elements rows = tables.get(0).select("tr");
        Elements cells = null;
        Table table = null;

        for (int i = 0; i < rows.size(); i++) {
            cells = rows.get(i).select("td");
            if (cells.size() == 0){
                cells = rows.get(i).select("th");
            }
            if (table  == null){
                if (cells.size() == 0){
                    table = new Table(rows.size(),1);
                }else {
                    table = new Table(rows.size(),cells.size());
                }
            }

            for (int j = 0; j < cells.size(); j++) {
                //Append Col
                if (j>=table.getColNumber()){
                    table.addColumn(ColumnLocation.RIGHT);
                }
                //add row
                table.setCell(new Cell(cells.get(j).text()),i,j);
            }
        }

        return table;
    }

    /**
     * Analysis UL
     * @param content body
     * @param identifer ident
     * @return table
     * <NOTE>Return html with label</NOTE>
     */
    public static Table processUL(String content,String identifer){
        //doc
        Document document = Jsoup.parse(content,"utf-8");

        //elements
        Elements tables = document.select(identifer);
        Elements rows = tables.get(0).select("li");
        Elements cells = null;
        Table table = null;

        cells = rows.get(0).getAllElements();
        if (cells.size() == 0){
            table = new Table(rows.size(),1);
        }else {
            table = new Table(rows.size(),cells.size()-1);
        }

        for (int i = 0; i < rows.size(); i++) {
            cells = rows.get(i).getAllElements();

            //add row
            for (int j = 1; j < cells.size(); j++) {
                if (j>=table.getColNumber()){
                    table.addColumn(ColumnLocation.RIGHT);
                }
                table.setCell(new Cell(cells.get(j).html()),i,j);
            }
        }

        return table;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
