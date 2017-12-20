package android.mxdlzg.com.bluewindmill.model.entity.config;

import java.io.Serializable;

/**
 * Created by 廷江 on 2017/3/26.
 */

public class TermOBJ implements Serializable{
    private String name;
    private int startYear,endYear,termNum;
    private long id;

    public TermOBJ(String name, int startYear, int endYear, int termNum, long id) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.termNum = termNum;
        this.id = id;
    }

    public String getAll(){
        String result = "";
        result+=(name+";");
        result+=(startYear+";");
        result+=(endYear+";");
        result+=(termNum+";");
        result+=(id+";");
        return result;
    }

    public String getName() {
        return name;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getTermNum() {
        return termNum;
    }

    public long getId() {
        return id;
    }
}
