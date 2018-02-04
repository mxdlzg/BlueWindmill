package android.mxdlzg.com.bluewindmill.model.entity;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class SCScoreDetail {
    private String activity,type,num,date,score,honest,remark;

    public SCScoreDetail(String activity, String type, String num, String date, String score, String honest, String remark) {
        this.activity = activity;
        this.type = type;
        this.num = num;
        this.date = date;
        this.score = score;
        this.honest = honest;
        this.remark = remark;
    }


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHonest() {
        return honest;
    }

    public void setHonest(String honest) {
        this.honest = honest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
