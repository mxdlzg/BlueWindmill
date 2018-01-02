package android.mxdlzg.com.bluewindmill.model.process;

import android.mxdlzg.com.bluewindmill.model.entity.DataTable;

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

    private DataTable processTable(String body,String identifer){
        return process(body,identifer);
    }

    public static DataTable<Element> process(String body,String identifer){
        DataTable<Element> dataTable = new DataTable<>(null);
        Document document = Jsoup.parse(body);
        Elements elements = document.select("table");

        for (Element table:
             elements) {
            if (table.html().contains(identifer)){
                dataTable.addRow(table);
            }
        }
        return dataTable;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
