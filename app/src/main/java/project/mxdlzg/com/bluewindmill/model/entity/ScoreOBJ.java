package project.mxdlzg.com.bluewindmill.model.entity;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class ScoreOBJ {
    private String lesionCode,name,credit,regular,mid,finl,total,se,seTotal;

    public ScoreOBJ(String lesionCode, String name, String credit, String regular, String mid, String finl, String total, String se, String seTotal) {
        this.lesionCode = lesionCode;
        this.name = name;
        this.credit = credit;
        this.regular = regular;
        this.mid = mid;
        this.finl = finl;
        this.total = total;
        this.se = se;
        this.seTotal = seTotal;
    }

    public String getLesionCode() {
        return lesionCode;
    }

    public void setLesionCode(String lesionCode) {
        this.lesionCode = lesionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFinl() {
        return finl;
    }

    public void setFinl(String finl) {
        this.finl = finl;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSe() {
        return se;
    }

    public void setSe(String se) {
        this.se = se;
    }

    public String getSeTotal() {
        return seTotal;
    }

    public void setSeTotal(String seTotal) {
        this.seTotal = seTotal;
    }
}
